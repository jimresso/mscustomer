package com.nttdata.customer.mscustomer.service.impl;

import com.nttdata.customer.mscustomer.util.CustomerMapper;
import com.nttdata.customer.mscustomer.model.CustomerDTO;
import com.nttdata.customer.mscustomer.repository.CustomerRepository;
import com.nttdata.customer.mscustomer.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.openapitools.model.Customer;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.stream.Stream;

@RequiredArgsConstructor
@Service
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;
    private static final Logger logger = LoggerFactory.getLogger(CustomerServiceImpl.class);

    @Override
    public Mono<ResponseEntity<Flux<Customer>>> listCustomers() {
        Flux<CustomerDTO> customerRepositoryAll = customerRepository.findAll();
        Flux<Customer> customers = customerRepositoryAll.map(customerMapper::mapToCustomer);
        return customers
                .collectList()
                .flatMap(customerList -> {
                    if (customerList.isEmpty()) {
                        logger.warn("No customers were found in the database.");
                    } else {
                        logger.info("{} clients found", customerList.size());
                    }
                    return Mono.just(ResponseEntity.ok().body(Flux.fromIterable(customerList)));
                })
                .onErrorResume(e -> {
                    logger.error("Error retrieving customers from the database", e);
                    return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
                });
    }

    @Override
    public Mono<ResponseEntity<Void>> addCustomer(Mono<Customer> customer) {
        return customer
                .flatMap(c -> {
                    if (hasInvalidFields(c)) {
                        logger.warn("Invalid customer data: missing required fields");
                        return Mono.just(ResponseEntity.badRequest().<Void>build());
                    }
                    return customerRepository.save(customerMapper.toOpenApiCustomer(c))
                            .flatMap(savedCustomer -> {
                                logger.info("Client successfully created with ID: {}", savedCustomer.getId());
                                return Mono.just(ResponseEntity.status(HttpStatus.CREATED).<Void>build());
                            });
                }).onErrorResume(e -> {
            logger.error("Error creating customer: {}", e.getMessage(), e);
            return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).<Void>build());
        });

    }

    @Override
    public Mono<ResponseEntity<Void>> modifyCustomer(String id, Mono<Customer> customer) {
        return customerRepository.findById(id)
                .flatMap(existingCustomerDTO -> customer
                        .flatMap(updatedCustomer -> {
                            CustomerDTO customerDTO = customerMapper.toOpenApiCustomer(updatedCustomer);
                            existingCustomerDTO.setDni(customerDTO.getDni());
                            existingCustomerDTO.setCustomerId(customerDTO.getCustomerId());
                            existingCustomerDTO.setEmail(customerDTO.getEmail());
                            existingCustomerDTO.setLastname(customerDTO.getLastname());
                            existingCustomerDTO.setName(customerDTO.getName());
                            existingCustomerDTO.setCompany(customerDTO.getCompany());
                            return customerRepository.save(existingCustomerDTO)
                                    .doOnSuccess(savedCustomer ->
                                            logger.info("Client successfully updated with ID {}.", id))
                                    .then(Mono.just(ResponseEntity.ok().<Void>build()));
                        }))
                .switchIfEmpty(Mono.fromRunnable(() -> logger.warn("Client not found for update with ID {}.", id))
                        .then(Mono.just(ResponseEntity.notFound().<Void>build())))
                .onErrorResume(e -> {
                    logger.error("Error updating client with ID{}: {}", id, e.getMessage());
                    return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).<Void>build());
                });
    }

    @Override
    public Mono<ResponseEntity<Void>> removeCustomer(String id) {
        return customerRepository.findById(id)
                .flatMap(existingCustomer ->
                        customerRepository.delete(existingCustomer)
                                .then(Mono.fromRunnable(() -> logger.info("Deleted client with ID:{}", id)))
                                .thenReturn(ResponseEntity.noContent().<Void>build())
                )
                .switchIfEmpty(Mono.fromRunnable(() -> logger.warn("Customer not found with ID {}", id))
                        .then(Mono.just(ResponseEntity.notFound().<Void>build())))
                .onErrorResume(e -> {
                    logger.error("Error deleting client with ID {}: {}", id, e.getMessage());
                    return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).<Void>build());
                });
    }

    private boolean hasInvalidFields(Customer c) {
        return Stream.of(c.getName(), c.getLastname(), c.getDni(), c.getEmail())
                .anyMatch(field -> field == null || field.isEmpty());
    }
}

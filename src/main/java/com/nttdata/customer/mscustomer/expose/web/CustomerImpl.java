package com.nttdata.customer.mscustomer.expose.web;


import com.nttdata.customer.mscustomer.service.CustomerService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.openapitools.api.ApiApi;

import org.openapitools.model.Customer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
public class CustomerImpl implements ApiApi {
    private final CustomerService customerService;
    private static final Logger logger = LoggerFactory.getLogger(CustomerImpl.class);

    @Override
    public Mono<ResponseEntity<Flux<Customer>>> getAllCustomers(ServerWebExchange exchange) {
        logger.info("Starting get Clients");
        return customerService.listCustomers();
    }

    @Override
    public Mono<ResponseEntity<Void>> updateCustomer(String id, Mono<Customer> customer, ServerWebExchange exchange) {
        return customerService.modifyCustomer(id, customer);
    }

    @Override
    public Mono<ResponseEntity<Void>> deleteCustomerByCustomerId(String customerId, ServerWebExchange exchange) {
        return customerService.removeCustomerId(customerId);
    }

    @Override
    public Mono<ResponseEntity<Void>> createCustomer(
            @Parameter(name = "Customer",
                    description = "",
                    required = true)
            @Valid @RequestBody Mono<Customer> customer,
            @Parameter(hidden = true) final ServerWebExchange exchange
    ) {
        logger.info("Starting create of client");
        return customerService.addCustomer(customer);
    }

    @Override
    public Mono<ResponseEntity<Void>> deleteCustomer(String id, ServerWebExchange exchange) {
        return customerService.removeCustomer(id);
    }
}
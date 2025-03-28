package com.nttdata.customer.mscustomer.service.impl;

import static org.mockito.Mockito.*;

import com.nttdata.customer.mscustomer.util.CustomerMapper;
import com.nttdata.customer.mscustomer.model.Company;
import com.nttdata.customer.mscustomer.model.CustomerDTO;
import com.nttdata.customer.mscustomer.repository.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.openapitools.model.Customer;
import org.openapitools.model.CustomerCompany;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
class CustomerServiceImplTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private CustomerMapper customerMapper;

    @InjectMocks
    private CustomerServiceImpl customerService;

    @Test
    void listCustomers_ShouldReturnFluxOfCustomers() {
        CustomerCompany company = new CustomerCompany();
        company.setRuc("123456789");
        company.setSocialreason("Tech Corp");
        Company companyDto =  new Company();
        companyDto.setRuc("123456789");
        companyDto.setSocialreason("Tech Corp");
        CustomerDTO customerDTO = new CustomerDTO("1", "John", "Doe", "12345678", "email@example.com", "Company", companyDto);
        Customer customer = new Customer()
                .id("1")
                .name("John")
                .lastname("Doe")
                .dni("12345678")
                .email("email@example.com")
                .company(company)
                .customertype("Type");
        when(customerRepository.findAll()).thenReturn(Flux.just(customerDTO));
        when(customerMapper.mapToCustomer(customerDTO)).thenReturn(customer);

        StepVerifier.create(customerService.listCustomers())
                .expectNextMatches(response -> response.getStatusCode().is2xxSuccessful())
                .verifyComplete();
    }

    @Test
    void addCustomer_ShouldCreateCustomer() {
        CustomerCompany company = new CustomerCompany();
        company.setRuc("123456789");
        company.setSocialreason("Tech Corp");
        Company companyDto =  new Company();
        companyDto.setRuc("123456789");
        companyDto.setSocialreason("Tech Corp");
        CustomerDTO customerDTO = new CustomerDTO("1", "John", "Doe", "12345678", "email@example.com", "Company", companyDto);
        Customer customer = new Customer()
                .id("1")
                .name("John")
                .lastname("Doe")
                .dni("12345678")
                .email("email@example.com")
                .company(company)
                .customertype("Type");
        when(customerMapper.toOpenApiCustomer(customer)).thenReturn(customerDTO);
        when(customerRepository.save(customerDTO)).thenReturn(Mono.just(customerDTO));

        StepVerifier.create(customerService.addCustomer(Mono.just(customer)))
                .expectNextMatches(response -> response.getStatusCode().is2xxSuccessful())
                .verifyComplete();
    }

    @Test
    void modifyCustomer_ShouldUpdateCustomer() {
        String customerId = "1";
        Company companyDto =  new Company();
        companyDto.setRuc("123456789");
        companyDto.setSocialreason("Tech Corp");
        CustomerCompany company = new CustomerCompany();
        company.setRuc("123456789");
        company.setSocialreason("Tech Corp");
        Customer customer = new Customer()
                .id("1")
                .name("John")
                .lastname("Doe")
                .dni("12345678")
                .email("email@example.com")
                .company(company)
                .customertype("Type");
        CustomerDTO existingCustomerDTO = new CustomerDTO(customerId, "Old Name", "Old Lastname", "12345678", "oldemail@example.com", "OldCompany", companyDto);
        CustomerDTO updatedCustomerDTO = new CustomerDTO(customerId, "John", "Doe", "12345678", "email@example.com", "Company", companyDto);

        when(customerRepository.findById(customerId)).thenReturn(Mono.just(existingCustomerDTO));
        when(customerMapper.toOpenApiCustomer(customer)).thenReturn(updatedCustomerDTO);
        when(customerRepository.save(existingCustomerDTO)).thenReturn(Mono.just(updatedCustomerDTO));

        StepVerifier.create(customerService.modifyCustomer(customerId, Mono.just(customer)))
                .expectNextMatches(response -> response.getStatusCode().is2xxSuccessful())
                .verifyComplete();
    }

    @Test
    void removeCustomer_ShouldDeleteCustomer() {
        String customerId = "1";
        Company companyDto =  new Company();
        companyDto.setRuc("123456789");
        companyDto.setSocialreason("Tech Corp");
        CustomerDTO customerDTO = new CustomerDTO(customerId, "John", "Doe", "12345678", "email@example.com", "Company", companyDto);

        when(customerRepository.findById(customerId)).thenReturn(Mono.just(customerDTO));
        when(customerRepository.delete(customerDTO)).thenReturn(Mono.empty());

        StepVerifier.create(customerService.removeCustomer(customerId))
                .expectNextMatches(response -> response.getStatusCode().is2xxSuccessful())
                .verifyComplete();
    }
}

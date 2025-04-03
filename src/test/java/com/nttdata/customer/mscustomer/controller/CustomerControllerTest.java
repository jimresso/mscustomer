package com.nttdata.customer.mscustomer.controller;

import com.nttdata.customer.mscustomer.service.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.openapitools.model.Customer;
import org.openapitools.model.CustomerCompany;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomerControllerTest {

    @Mock
    private CustomerService customerService;

    @InjectMocks
    private CustomerController customerController;

    private WebTestClient webTestClient;

    @BeforeEach
    void setUp() {
        webTestClient = WebTestClient.bindToController(customerController).build();
    }

    @Test
    void createCustomer_ShouldReturnCreatedStatus() {
        CustomerCompany company = new CustomerCompany().ruc("123456789").socialreason("Tech Corp");
        Customer customer = new Customer()
                .id("1")
                .name("John")
                .lastname("Doe")
                .dni("12345678")
                .email("email@example.com")
                .company(company)
                .customerId("Type");
        when(customerService.addCustomer(any())).thenReturn(Mono.just(ResponseEntity.status(HttpStatus.CREATED).build()));
        webTestClient.post().uri("/api/customers")
                .bodyValue(customer)
                .exchange()
                .expectStatus().isCreated();
    }
    @Test
    void getAllCustomers_ShouldReturnCustomers() {
        CustomerCompany company = new CustomerCompany().ruc("123456789").socialreason("Tech Corp");
        Customer customer = new Customer()
                .id("1")
                .name("John")
                .lastname("Doe")
                .dni("12345678")
                .email("email@example.com")
                .company(company)
                .customerId("Type");
        when(customerService.listCustomers())
                .thenReturn(Mono.just(ResponseEntity.ok(Flux.just(customer))));
        webTestClient.get().uri("/api/getcustomers")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Customer.class)
                .hasSize(1)
                .contains(customer);
    }
    @Test
    void updateCustomer_ShouldReturnOkStatus() {
        CustomerCompany company = new CustomerCompany().ruc("123456789").socialreason("Tech Corp");
        Customer customer = new Customer()
                .id("1")
                .name("John")
                .lastname("Doe")
                .dni("12345678")
                .email("email@example.com")
                .company(company)
                .customerId("Type");
        when(customerService.modifyCustomer(eq("1"), any())).thenReturn(Mono.just(ResponseEntity.ok().build()));
        webTestClient.put().uri("/api/customers/1")
                .bodyValue(customer)
                .exchange()
                .expectStatus().isOk();
    }
    @Test
    void deleteCustomer_ShouldReturnNoContentStatus() {
        when(customerService.removeCustomer("1")).thenReturn(Mono.just(ResponseEntity.noContent().build()));
        webTestClient.delete().uri("/api/customers/1")
                .exchange()
                .expectStatus().isNoContent();
    }

}
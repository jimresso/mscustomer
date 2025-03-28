package com.nttdata.customer.mscustomer.service;

import org.openapitools.model.Customer;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CustomerService  {
    Mono<ResponseEntity<Flux<Customer>>> listCustomers();
    Mono<ResponseEntity<Void>> addCustomer(Mono<Customer> customer);
    Mono<ResponseEntity<Void>> modifyCustomer(String id, Mono<Customer> customer);
    Mono<ResponseEntity<Void>> removeCustomer(String id);
}

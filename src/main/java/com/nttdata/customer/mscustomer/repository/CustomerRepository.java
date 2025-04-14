package com.nttdata.customer.mscustomer.repository;





import com.nttdata.customer.mscustomer.model.CustomerDTO;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface CustomerRepository extends ReactiveMongoRepository<CustomerDTO, String> {
    Mono<CustomerDTO> findById(String id);
    Mono<CustomerDTO> findByCustomerId(String customerId);
}

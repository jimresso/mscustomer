package com.nttdata.customer.mscustomer.service.impl;

import static org.junit.jupiter.api.Assertions.assertNotNull;
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
import org.springframework.http.HttpStatus;
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
                .customerId("Type");
        assertNotNull(customerDTO);
        when(customerRepository.findAll()).thenReturn(Flux.just(customerDTO));
        when(customerMapper.mapToCustomer(customerDTO)).thenReturn(customer);
        StepVerifier.create(customerService.listCustomers())
                .expectNextMatches(response -> response.getStatusCode().is2xxSuccessful())
                .verifyComplete();
    }

    @Test
    void listCustomers_ShouldReturnEmptyCustomers() {
        when(customerRepository.findAll()).thenReturn(Flux.empty());
        StepVerifier.create(customerService.listCustomers())
                .expectNextMatches(response -> response.getStatusCode().is2xxSuccessful())
                .verifyComplete();
    }
    @Test
    void listCustomers_ShouldHandleError() {
        when(customerRepository.findAll()).thenReturn(Flux.error(new RuntimeException("Database error")));
        StepVerifier.create(customerService.listCustomers())
                .expectNextMatches(response -> response.getStatusCode().equals(HttpStatus.INTERNAL_SERVER_ERROR))
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
                .customerId("Type");
        when(customerMapper.toOpenApiCustomer(customer)).thenReturn(customerDTO);
        when(customerRepository.save(customerDTO)).thenReturn(Mono.just(customerDTO));
        StepVerifier.create(customerService.addCustomer(Mono.just(customer)))
                .expectNextMatches(response -> response.getStatusCode().is2xxSuccessful())
                .verifyComplete();
    }
    @Test
    void addCustomer_ShouldReturnInternalServerError_WhenExceptionOccurs() {
        Company companyDto =  new Company();
        companyDto.setRuc("123456789");
        companyDto.setSocialreason("Tech Corp");
        Customer validCustomer = new Customer();
        validCustomer.setId("1");
        validCustomer.setName("John");
        validCustomer.setEmail("email@example.com");
        validCustomer.setLastname("algo");
        validCustomer.setDni("74451");
        validCustomer.setCustomerId("algo");
         when(customerRepository.save(any(CustomerDTO.class))).thenReturn(Mono.error(new RuntimeException("Database error")));
        StepVerifier.create(customerService.addCustomer(Mono.just(validCustomer)))
                .expectNextMatches(response -> response.getStatusCode() == HttpStatus.INTERNAL_SERVER_ERROR)
                .verifyComplete();
    }
    @Test
    void addCustomer_ShouldReturnBadReques_WhenExceptionOccurs() {
        Company companyDto =  new Company();
        companyDto.setRuc("123456789");
        companyDto.setSocialreason("Tech Corp");
        Customer validCustomer = new Customer();
        validCustomer.setId("1");
        validCustomer.setEmail("email@example.com");
        validCustomer.setLastname("algo");
        validCustomer.setDni("74451");
        validCustomer.setCustomerId("algo");
        StepVerifier.create(customerService.addCustomer(Mono.just(validCustomer)))
                .expectNextMatches(response -> response.getStatusCode() == HttpStatus.BAD_REQUEST)
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
                .customerId("Type");
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
    void modifyCustomer_ShouldReturnInternalServerError_WhenExceptionOccurs() {
        CustomerDTO customerDTO = new CustomerDTO("1", "John", "Doe", "12345678", "email@example.com", "Company", null);
        Customer validCustomer = new Customer();
        validCustomer.setId("1");
        validCustomer.setName("John");
        validCustomer.setEmail("email@example.com");
        when(customerRepository.findById("1")).thenReturn(Mono.just(customerDTO));
        lenient().when(customerRepository.save(any(CustomerDTO.class))).thenReturn(Mono.error(new RuntimeException("Database error")));
        StepVerifier.create(customerService.modifyCustomer("1", Mono.just(validCustomer)))
                .expectNextMatches(response -> response.getStatusCode() == HttpStatus.INTERNAL_SERVER_ERROR)
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
    @Test
    void removeCustomer_ShouldEmptyCustomer() {
        String customerId = "1";
        Company companyDto =  new Company();
        companyDto.setRuc("123456789");
        companyDto.setSocialreason("Tech Corp");
        when(customerRepository.findById(customerId)).thenReturn(Mono.empty());
        StepVerifier.create(customerService.removeCustomer(customerId))
                .expectNextMatches(response -> response.getStatusCode() ==HttpStatus.NOT_FOUND)
                .verifyComplete();
    }

    @Test
    void removeCustomer_ShouldReturnInternalServerError_WhenExceptionOccurs() {
        String customerId = "1";
        CustomerDTO customerDTO = new CustomerDTO("1", "John", "Doe", "12345678", "email@example.com", "Company", null);
        when(customerRepository.findById(customerId)).thenReturn(Mono.just(customerDTO));
        when(customerRepository.delete(customerDTO)).thenReturn(Mono.error(new RuntimeException("Database error")));
        StepVerifier.create(customerService.removeCustomer(customerId))
                .expectNextMatches(response -> response.getStatusCode() == HttpStatus.INTERNAL_SERVER_ERROR)
                .verifyComplete();
    }

}

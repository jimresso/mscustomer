package com.nttdata.customer.mscustomer.util;

import com.nttdata.customer.mscustomer.model.Company;
import com.nttdata.customer.mscustomer.model.CustomerDTO;
import org.junit.jupiter.api.Test;
import org.openapitools.model.Customer;
import org.openapitools.model.CustomerCompany;

import static org.junit.jupiter.api.Assertions.*;

class CustomerMapperTest {

    private final CustomerMapper customerMapper = new CustomerMapper();
    @Test
    void mapToCustomer_ShouldMapCustomerDTOToCustomer() {
        Company companyDto = new Company();
        companyDto.setRuc("123456789");
        companyDto.setSocialreason("Tech Corp");
        CustomerDTO customerDTO = new CustomerDTO("1", "John", "Doe", "12345678", "email@example.com", "Company", companyDto);
        Customer customer = customerMapper.mapToCustomer(customerDTO);
        assertNotNull(customer);
        assertEquals(customerDTO.getId(), customer.getId());
        assertEquals(customerDTO.getName(), customer.getName());
        assertEquals(customerDTO.getLastname(), customer.getLastname());
        assertEquals(customerDTO.getDni(), customer.getDni());
        assertEquals(customerDTO.getEmail(), customer.getEmail());
        assertNotNull(customer.getCompany());
        assertEquals(customerDTO.getCompany().getRuc(), customer.getCompany().getRuc());
        assertEquals(customerDTO.getCompany().getSocialreason(), customer.getCompany().getSocialreason());
    }
    @Test
    void toOpenApiCustomer_ShouldMapCustomerToCustomerDTO() {
        CustomerCompany company = new CustomerCompany();
        company.setRuc("123456789");
        company.setSocialreason("Tech Corp");
        Customer customer = new Customer();
        customer.setId("1");
        customer.setCustomertype("Company");
        customer.setName("John");
        customer.setLastname("Doe");
        customer.setEmail("email@example.com");
        customer.setDni("12345678");
        customer.setCompany(company);
        CustomerDTO customerDTO = customerMapper.toOpenApiCustomer(customer);
        assertNotNull(customerDTO);
        assertEquals(customer.getId(), customerDTO.getId());
        assertEquals(customer.getName(), customerDTO.getName());
        assertEquals(customer.getLastname(), customerDTO.getLastname());
        assertEquals(customer.getDni(), customerDTO.getDni());
        assertEquals(customer.getEmail(), customerDTO.getEmail());
        assertNotNull(customerDTO.getCompany());
        assertEquals(customer.getCompany().getRuc(), customerDTO.getCompany().getRuc());
        assertEquals(customer.getCompany().getSocialreason(), customerDTO.getCompany().getSocialreason());
    }
}
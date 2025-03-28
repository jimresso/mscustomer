package com.nttdata.customer.mscustomer.Util;

import com.nttdata.customer.mscustomer.model.Company;
import com.nttdata.customer.mscustomer.model.CustomerDTO;
import org.openapitools.model.Customer;
import org.openapitools.model.CustomerCompany;
import org.springframework.stereotype.Service;

@Service
public class CustomerMapper {

    public Customer mapToCustomer(CustomerDTO customerDTO) {
        Customer customer = new Customer();
        customer.setId(customerDTO.getId());
        customer.setCustomertype(customerDTO.getCustomertype());
        customer.setName(customerDTO.getName());
        customer.setLastname(customerDTO.getLastname());
        customer.setEmail(customerDTO.getEmail());
        customer.setDni(customerDTO.getDni());
        if (customerDTO.getCompany() != null) {
            CustomerCompany company = new CustomerCompany();
            company.setRuc(customerDTO.getCompany().getRuc());
            company.setSocialreason(customerDTO.getCompany().getSocialreason());
            customer.setCompany(company);
        }
        return customer;
    }
    public  CustomerDTO toOpenApiCustomer(Customer customer) {
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setId(customer.getId());
        customerDTO.setDni(customer.getDni());
        customerDTO.setCustomertype(customer.getCustomertype());
        customerDTO.setEmail(customer.getEmail());
        customerDTO.setLastname(customer.getLastname());
        customerDTO.setName(customer.getName());

        if (customer.getCompany() != null) {
            Company company = new Company();
            company.setRuc(customer.getCompany().getRuc());
            company.setSocialreason(customer.getCompany().getSocialreason());
            customerDTO.setCompany(company);
        }
        return customerDTO;
    }
}
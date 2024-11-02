package com.kim.customer.service;

import com.kim.customer.dto.CustomerDto;
import com.kim.customer.entity.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;


public interface ICustomerService {
    void createCustomer(CustomerDto customerDto);
    boolean updateCustomer(String mobileNumber, CustomerDto customerDto);;
    boolean deleteCustomer(String mobileNumber);
    CustomerDto fetchCustomer(String mobileNumber);
    List<Customer> searchCustomers(String searchTerm);
    Page<CustomerDto> findAllCustomers(Pageable pageable);

}

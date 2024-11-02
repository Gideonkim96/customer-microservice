package com.kim.customer.service.impl;

import com.kim.customer.dto.CustomerDto;
import com.kim.customer.entity.Customer;
import com.kim.customer.exception.CustomerAlreadyExistsException;
import com.kim.customer.exception.ResourceNotFoundException;
import com.kim.customer.mapper.CustomerMapper;
import com.kim.customer.repository.CustomerRepository;
import com.kim.customer.service.ICustomerService;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;



import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CustomerServiceImpl implements ICustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public void createCustomer(CustomerDto customerDto) {
        Customer customer = CustomerMapper.mapToCustomer(customerDto, new Customer());
        Optional<Customer> optionalCustomer = customerRepository.findByMobileNumber(customerDto.getMobileNumber());
        if (optionalCustomer.isPresent()) {
            throw new CustomerAlreadyExistsException("Customer already registered with given mobileNumber "
                    + customerDto.getMobileNumber());
        }
        customerRepository.save(customer);
    }

    @Override
    public CustomerDto fetchCustomer(String mobileNumber) {
        Customer customer = customerRepository.findByMobileNumber(mobileNumber).orElseThrow(
                () -> new ResourceNotFoundException("Customer", "mobileNumber", mobileNumber)
        );

        CustomerDto customerDto = CustomerMapper.mapToCustomerDto(customer, new CustomerDto());

        return customerDto;
    }
    @Override
    public boolean updateCustomer(String mobileNumber, CustomerDto customerDto) {
        Customer customer = customerRepository.findByMobileNumber(mobileNumber).orElseThrow(
                () -> new ResourceNotFoundException("Customer", "mobileNumber", mobileNumber)
        );

        CustomerMapper.mapToCustomer(customerDto, customer);
        customerRepository.save(customer);

        return true;
    }


    @Override
    public boolean deleteCustomer(String mobileNumber) {
        Customer customer = customerRepository.findByMobileNumber(mobileNumber).orElseThrow(
                () -> new ResourceNotFoundException("Customer", "mobileNumber", mobileNumber)
        );
       customerRepository.deleteByCustomerId(customer.getCustomerId());
            return true;
    }


    @Override
    public Page<CustomerDto> findAllCustomers(Pageable pageable) {
        Page<Customer> customerPage = customerRepository.findAll(pageable);
        List<CustomerDto> customerDtoList = customerPage.getContent().stream()
                .map(customer -> {
                    CustomerDto customerDto = new CustomerDto();
                    BeanUtils.copyProperties(customer, customerDto);
                    return customerDto;
                })
                .collect(Collectors.toList());
        return new PageImpl<>(customerDtoList, pageable, customerPage.getTotalElements());
    }

    public List<Customer> searchCustomers(String searchTerm) {
        return customerRepository.findByNameOrPhone(searchTerm);
    }
}

package com.flipkart.customer.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.flipkart.customer.dto.CustomerRequest;
import com.flipkart.customer.dto.CustomerResponse;
import com.flipkart.customer.entity.CustomerEntity;
import com.flipkart.customer.repository.CustomerRepository;

import jakarta.transaction.Transactional;

@Service
public class CustomerService {

	@Autowired
	CustomerRepository customerRepository;

	@Transactional
	public CustomerResponse createCustomerService(CustomerRequest customerRequest) {
		
		CustomerEntity customerEntity = new CustomerEntity();
		customerEntity.setName(customerRequest.getName());
		customerEntity.setEmail(customerRequest.getEmail());
		customerEntity.setPassword(customerRequest.getPassword());

		CustomerEntity customerEntityResponse = customerRepository.save(customerEntity);

		CustomerResponse customerResponse = new CustomerResponse();
		customerResponse.setCustomerId(customerEntityResponse.getCustomerId());
		customerResponse.setName(customerEntityResponse.getName());
		customerResponse.setEmail(customerEntityResponse.getEmail());

		return customerResponse;

	}
}

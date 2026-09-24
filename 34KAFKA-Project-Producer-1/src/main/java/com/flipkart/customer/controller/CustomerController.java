package com.flipkart.customer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.flipkart.customer.dto.CustomerRequest;
import com.flipkart.customer.dto.CustomerResponse;
import com.flipkart.customer.service.CustomerService;

@RestController
@RequestMapping("flipkart")
public class CustomerController {

	@Autowired
	CustomerService customerService;

	@PostMapping("create/customer")
	public ResponseEntity<CustomerResponse> createCustomer(@RequestBody CustomerRequest customerRequest) {
		CustomerResponse customerResponse = customerService.createCustomerService(customerRequest);
		return ResponseEntity.status(HttpStatus.OK).body(customerResponse);
	}
}

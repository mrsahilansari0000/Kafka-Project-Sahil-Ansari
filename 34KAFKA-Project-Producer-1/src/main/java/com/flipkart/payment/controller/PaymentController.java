package com.flipkart.payment.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.flipkart.payment.dto.PaymentRequest;
import com.flipkart.payment.dto.PaymentResponse;
import com.flipkart.payment.service.PaymentService;

@RestController
@RequestMapping("flipkart")
public class PaymentController {
	
	@Autowired
	PaymentService paymentService;
	
	@PostMapping("payment")
	public PaymentResponse doPaymentContoller(@RequestBody PaymentRequest paymentRequest) {
		PaymentResponse paymentResponse = paymentService.paymentProcessService(paymentRequest);
		return paymentResponse;
	}
}

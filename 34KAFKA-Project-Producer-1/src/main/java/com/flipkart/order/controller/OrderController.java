package com.flipkart.order.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.flipkart.order.dto.OrderRequest;
import com.flipkart.order.dto.OrderResponse;
import com.flipkart.order.service.OrderService;

@RestController
@RequestMapping("flipkart")
public class OrderController {

	@Autowired
	OrderService orderService;

	@PostMapping("place/order")
	public ResponseEntity<OrderResponse> placeOrder(@RequestBody OrderRequest orderRequest) {
		OrderResponse orderResponse = orderService.placeOrderService(orderRequest);
		return ResponseEntity.status(HttpStatus.OK).body(orderResponse);
	}
}

package com.flipkart.delivery.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.flipkart.delivery.dto.DeliveryRequest;
import com.flipkart.delivery.dto.DeliveryResponse;
import com.flipkart.delivery.service.DeliveryService;

@RestController
@RequestMapping("flipkart")
public class DeliveryController {

	@Autowired
	DeliveryService deliveryService;

	@PostMapping("delivery")
	public DeliveryResponse confirmDelivery(@RequestBody DeliveryRequest deliveryRequest) {
		DeliveryResponse deliveryResponse = deliveryService.deliveryProcessService(deliveryRequest);
		return deliveryResponse;
	}
}

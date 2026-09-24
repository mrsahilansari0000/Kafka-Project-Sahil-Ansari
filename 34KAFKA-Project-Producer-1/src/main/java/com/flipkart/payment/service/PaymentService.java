package com.flipkart.payment.service;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.flipkart.order.event.OrderCreatedEvent;
import com.flipkart.payment.dto.PaymentRequest;
import com.flipkart.payment.dto.PaymentResponse;
import com.flipkart.payment.entity.PaymentEntity;
import com.flipkart.payment.event.PaymentEvent;
import com.flipkart.payment.kafka.PaymentEventProducer;
import com.flipkart.payment.repository.PaymentRepository;

import jakarta.transaction.Transactional;

@Service
public class PaymentService {

	@Autowired
	PaymentEventProducer paymentEventProducer;

	@Autowired
	PaymentRepository paymentRepository;

	@KafkaListener(topics = "flipkart-order", groupId = "payment-group")
	public void paymentProcessor(OrderCreatedEvent orderEvent) throws InterruptedException {
		System.out.println("Payment Under Processing.....📡📡📡");
		Thread.sleep(3000);
		System.out.println("📡📡........");
		System.out.println();
		// OrderCreatedEvent → PaymentRequest
		PaymentRequest paymentRequest = new PaymentRequest();
		paymentRequest.setOrderId(orderEvent.getOrderId());
		paymentRequest.setCustomerId(orderEvent.getCustomerId());
		paymentRequest.setAmount(orderEvent.getAmount());
		paymentRequest.setPaymentMethod(orderEvent.getPaymentMethod());
		paymentRequest.setDeliveryAddress(orderEvent.getDeliveryAddress());
		

		// process
		paymentProcessService(paymentRequest);
	}

	@Transactional
	public PaymentResponse paymentProcessService(PaymentRequest paymentRequest) {
		PaymentEntity paymentEntity = new PaymentEntity();
		String paymentEventId = generaterPaymentEventId();
		String paymentId = generaterPaymentId();
		paymentEntity.setEventId(paymentEventId);
		paymentEntity.setEventType("PAYMENT_SUCCESS");
		paymentEntity.setOrderId(paymentRequest.getOrderId());
		paymentEntity.setCustomerId(paymentRequest.getCustomerId());
		paymentEntity.setAmount(paymentRequest.getAmount());
		paymentEntity.setPaymentId(paymentId);
		paymentEntity.setPaymentMethod(paymentRequest.getPaymentMethod());
		paymentEntity.setPaymentStatus("SUCCESS");
		paymentEntity.setCreatedAt(LocalDateTime.now());

		// SAVE TO DB
		PaymentEntity paymentResponseEntity = paymentRepository.save(paymentEntity);

		// FOR RESPONSE
		PaymentResponse paymentResponse = new PaymentResponse();
		paymentResponse.setEventId(paymentEventId);
		paymentResponse.setEventType("PAYMENT_SUCCESS");
		paymentResponse.setOrderId(paymentResponseEntity.getOrderId());
		paymentResponse.setCustomerId(paymentResponseEntity.getCustomerId());
		paymentResponse.setAmount(paymentResponseEntity.getAmount());
		paymentResponse.setPaymentId(paymentResponseEntity.getPaymentId());
		paymentResponse.setPaymentMethod(paymentResponseEntity.getPaymentMethod());
		paymentResponse.setPaymentStatus(paymentResponseEntity.getPaymentStatus());
		paymentResponse.setPaymentTime(paymentResponseEntity.getCreatedAt());

		PaymentEvent paymentEvent = new PaymentEvent();
		paymentEvent.setEventId(paymentEventId);
		paymentEvent.setEventType("PAYMENT_SUCCESS");
		paymentEvent.setOrderId(paymentRequest.getOrderId());
		paymentEvent.setCustomerId(paymentRequest.getCustomerId());
		paymentEvent.setAmount(paymentRequest.getAmount());
		paymentEvent.setPaymentId(paymentId);
		paymentEvent.setPaymentStatus("SUCCESS");
		paymentEvent.setPaymentMethod(paymentResponseEntity.getPaymentMethod());
		paymentEvent.setDeliveryAddress(paymentRequest.getDeliveryAddress());
		
		paymentEventProducer.sendPaymentEvent(paymentEvent);
		
		return paymentResponse;
		
	}

	private String generaterPaymentEventId() {
		Random random = new Random();
		return "PAY-" + (random.nextInt(1000) + 9000);
	}

	private String generaterPaymentId() {
		Random random = new Random();
		return "TXN-" + (random.nextInt(1000) + 9000);
	}
}
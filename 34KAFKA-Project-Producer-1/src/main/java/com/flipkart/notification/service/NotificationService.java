package com.flipkart.notification.service;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.flipkart.delivery.event.DeliveryEvent;
import com.flipkart.order.event.OrderCreatedEvent;
import com.flipkart.payment.event.PaymentEvent;

@Service
public class NotificationService {

	// ================= 1. ORDER CREATED =================
	@KafkaListener(topics = "flipkart-order", groupId = "notification-group", properties = {
			"spring.json.value.default.type=com.flipkart.order.event.OrderCreatedEvent" })
	public void onOrderCreated(OrderCreatedEvent event) {
		System.out.println("📧 SMS SENT");
		System.out.println("Customer: " + event.getCustomerId());
		System.out.println("Order   : " + event.getOrderId());
		System.out.println("Message : Your order has been placed successfully. Amount: Rs." + event.getAmount());
		System.out.println("------------------------------------------------");
	}

	// ================= 2. PAYMENT SUCCESS / FAILED =================
	@KafkaListener(topics = "flipkart-payment", groupId = "notification-group", properties = {
			"spring.json.value.default.type=com.flipkart.payment.event.PaymentEvent" })
	public void onPayment(PaymentEvent event) {
		if ("SUCCESS".equals(event.getPaymentStatus())) {
			System.out.println("📧 SMS SENT");
			System.out.println("Customer: " + event.getCustomerId());
			System.out.println("Order   : " + event.getOrderId());
			System.out.println("Message : Your payment of Rs." + event.getAmount() + " was successful.");
		} else {
			System.out.println("📧 SMS SENT");
			System.out.println("Customer: " + event.getCustomerId());
			System.out.println("Order   : " + event.getOrderId());
			System.out.println("Message : Your payment of Rs." + event.getAmount() + " failed. Please retry.");
		}
		System.out.println("------------------------------------------------");
	}

	// ================= 3. DELIVERY EVENTS =================
	@KafkaListener(topics = "delivery-created", groupId = "notification-group", properties = {
			"spring.json.value.default.type=com.flipkart.delivery.event.DeliveryEvent" })
	public void onDelivery(DeliveryEvent event) {
		if ("DELIVERY_CREATED".equals(event.getEventType())) {
			System.out.println("📧 SMS SENT");
			System.out.println("Customer: " + event.getCustomerId());
			System.out.println("Order   : " + event.getOrderId());
			System.out.println("Message : Your order has been shipped. Tracking No: " + event.getTrackingNumber());
		} else if ("OUT_FOR_DELIVERY".equals(event.getEventType())) {
			System.out.println("📧 SMS SENT");
			System.out.println("Customer: " + event.getCustomerId());
			System.out.println("Order   : " + event.getOrderId());
			System.out.println("Message : Your order is out for delivery today!");
		} else if ("ORDER_DELIVERED".equals(event.getEventType())) {
			System.out.println("📧 SMS SENT");
			System.out.println("Customer: " + event.getCustomerId());
			System.out.println("Order   : " + event.getOrderId());
			System.out.println("Message : Your order has been delivered. Thank you!");
		}
		System.out.println("------------------------------------------------");
	}
}

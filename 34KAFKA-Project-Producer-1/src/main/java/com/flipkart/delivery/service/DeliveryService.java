package com.flipkart.delivery.service;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.flipkart.delivery.dto.DeliveryRequest;
import com.flipkart.delivery.dto.DeliveryResponse;
import com.flipkart.delivery.entity.DeliveryEntity;
import com.flipkart.delivery.event.DeliveryEvent;
import com.flipkart.delivery.kafka.DeliveryEventProducer;
import com.flipkart.delivery.repository.DeliveryRepository;
import com.flipkart.order.entity.OrderEntity;
import com.flipkart.order.repository.OrderRepository;
import com.flipkart.payment.event.PaymentEvent;

import jakarta.transaction.Transactional;

@Service
public class DeliveryService {

	@Autowired
	DeliveryEventProducer deliveryEventProducer;

	@Autowired
	DeliveryRepository deliveryRepository;
	

	@KafkaListener(topics = "flipkart-payment", groupId = "delivery-group")
	public void deliveryProcessor(PaymentEvent paymentEvent) throws InterruptedException {
		System.out.println("Delivery Under Processing.....🚚🚚🚚");
		Thread.sleep(3000);
		System.out.println("🚚🚚........");
		System.out.println();
	

		// PaymentEvent → DeliveryRequest
		DeliveryRequest deliveryRequest = new DeliveryRequest();
		deliveryRequest.setOrderId(paymentEvent.getOrderId());
		deliveryRequest.setCustomerId(paymentEvent.getCustomerId());
		deliveryRequest.setDeliveryAddress(paymentEvent.getDeliveryAddress());

		// process
		deliveryProcessService(deliveryRequest);
	}

	@Transactional
	public DeliveryResponse deliveryProcessService(DeliveryRequest deliveryRequest) {
		DeliveryEntity deliveryEntity = new DeliveryEntity();
		String deliveryEventId = generaterDeliveryEventId();
		String trackingNumber = generaterTrackingNumber();
		deliveryEntity.setEventId(deliveryEventId);
		deliveryEntity.setEventType("DELIVERY_CREATED");
		deliveryEntity.setOrderId(deliveryRequest.getOrderId());
		deliveryEntity.setCustomerId(deliveryRequest.getCustomerId());
		deliveryEntity.setTrackingNumber(trackingNumber);
		deliveryEntity.setDeliveryAddress(deliveryRequest.getDeliveryAddress());
		deliveryEntity.setDeliveryStatus("CREATED");
		deliveryEntity.setCreatedAt(LocalDateTime.now());

		// SAVE TO DB
		DeliveryEntity deliveryResponseEntity = deliveryRepository.save(deliveryEntity);

		// FOR RESPONSE
		DeliveryResponse deliveryResponse = new DeliveryResponse();
		deliveryResponse.setOrderId(deliveryResponseEntity.getOrderId());
		deliveryResponse.setCustomerId(deliveryResponseEntity.getCustomerId());
		deliveryResponse.setTrackingNumber(deliveryResponseEntity.getTrackingNumber());
		deliveryResponse.setDeliveryAddress(deliveryResponseEntity.getDeliveryAddress());
		deliveryResponse.setDeliveryStatus(deliveryResponseEntity.getDeliveryStatus());
		deliveryResponse.setCreatedAt(deliveryResponseEntity.getCreatedAt());

		DeliveryEvent deliveryEvent = new DeliveryEvent();
		deliveryEvent.setEventId(deliveryEventId);
		deliveryEvent.setEventType("DELIVERY_CREATED");
		deliveryEvent.setOrderId(deliveryRequest.getOrderId());
		deliveryEvent.setCustomerId(deliveryRequest.getCustomerId());
		deliveryEvent.setTrackingNumber(trackingNumber);
		deliveryEvent.setDeliveryAddress(deliveryResponseEntity.getDeliveryAddress());
		deliveryEvent.setDeliveryStatus("CREATED");
		deliveryEvent.setDeliveryAddress(deliveryResponseEntity.getDeliveryAddress());

		deliveryEventProducer.sendDeliveryEvent(deliveryEvent);

		return deliveryResponse;
	}

	private String generaterDeliveryEventId() {
		Random random = new Random();
		return "DEL-" + (random.nextInt(1000) + 10000);
	}

	private String generaterTrackingNumber() {
		Random random = new Random();
		return "TRK-" + (random.nextInt(1000) + 50000);
	}
}

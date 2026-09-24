package com.flipkart.order.kafaka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.flipkart.order.event.OrderCreatedEvent;

@Service
public class OrderEventProducer {
	
	private static final String topic = "flipkart-order";

	@Autowired
	KafkaTemplate<String, OrderCreatedEvent> kafkaTemplate;
	
	
	public void sendOrderCreatedMessage(OrderCreatedEvent event) {
		kafkaTemplate.send(topic, event.getOrderId(), event);
	}
	
	public void sendOrderFailedMessage(OrderCreatedEvent event) {
		kafkaTemplate.send(topic, event);
	}
	
	
}

package com.flipkart.delivery.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.flipkart.delivery.event.DeliveryEvent;

@Service
public class DeliveryEventProducer {

	private static final String topic = "delivery-created";

	@Autowired
	KafkaTemplate<String, DeliveryEvent> kafkaTemplate;

	public void sendDeliveryEvent(DeliveryEvent event) {
		kafkaTemplate.send(topic, event.getOrderId(), event);
	}
}
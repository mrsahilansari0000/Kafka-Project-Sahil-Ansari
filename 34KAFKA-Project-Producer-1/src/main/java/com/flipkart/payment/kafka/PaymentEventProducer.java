package com.flipkart.payment.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.flipkart.payment.event.PaymentEvent;

@Service
public class PaymentEventProducer {

	private static final String topic = "flipkart-payment";
	 
	@Autowired
	KafkaTemplate<String, PaymentEvent> kafkaTemplate;
	
	public void sendPaymentEvent(PaymentEvent event) {
		kafkaTemplate.send(topic, event.getPaymentId(), event);
		System.out.println("\n========================================");
		System.out.println("        💳 PAYMENT SUCCESSFUL");
		System.out.println("========================================");
		System.out.println("Payment Method : "+event.getPaymentMethod());
		System.out.println("Payment Status : SUCCESS");
		System.out.println("Transaction ID : " + event.getPaymentId());
		System.out.println("Amount         : ₹" + event.getAmount());
		System.out.println("----------------------------------------");
		System.out.println("        ✅ Payment Completed");
		System.out.println("     Thank you for your payment!");
		System.out.println("========================================\n");
	}

}

package com.flipkart.order.service;

import java.util.Optional;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.flipkart.customer.entity.CustomerEntity;
import com.flipkart.customer.repository.CustomerRepository;
import com.flipkart.order.dto.OrderRequest;
import com.flipkart.order.dto.OrderResponse;
import com.flipkart.order.entity.OrderEntity;
import com.flipkart.order.event.OrderCreatedEvent;
import com.flipkart.order.kafaka.OrderEventProducer;
import com.flipkart.order.repository.OrderRepository;

import jakarta.transaction.Transactional;

@Service
public class OrderService {

	@Autowired
	CustomerRepository customerRepository;

	@Autowired
	OrderRepository orderRepository;

	@Autowired
	OrderEventProducer orderEventProducer;

	@Transactional
	public OrderResponse placeOrderService(OrderRequest orderRequest) {
		CustomerEntity customer = customerRepository.findById(orderRequest.getCustomerId()).get();
		OrderEntity orderEntity = new OrderEntity();
		orderEntity.setCustomerEntity(customer);
		orderEntity.setCutomerName(orderRequest.getCustomerName()); // note: typo in your entity field
		orderEntity.setProductId(orderRequest.getProductId());
		orderEntity.setProductName(orderRequest.getProductName());
		orderEntity.setQuantity(orderRequest.getQuantity());
		orderEntity.setAmount(orderRequest.getAmount());
		orderEntity.setDeliveryAddress(orderRequest.getDeliveryAddress());

		OrderEntity orderEntityResponse = orderRepository.save(orderEntity);

		OrderResponse orderResponse = new OrderResponse();

		String orderId = generateOrderId();

		int customerId = orderEntityResponse.getCustomerEntity().getCustomerId();
		OrderCreatedEvent oreCreatedEvent = new OrderCreatedEvent(generateEventId(), "ORDER_CREATED", orderId,
				customerId, orderEntity.getAmount(), orderRequest.getPaymentMethod(), orderEntity.getDeliveryAddress());

		OrderCreatedEvent orderFailedEvent = new OrderCreatedEvent(generateEventId(), "ORDER_FAILED", orderId,
				customerId, orderEntity.getAmount(), orderRequest.getPaymentMethod(), orderEntity.getDeliveryAddress());

		if (orderEntityResponse.getId() > 0) {
			orderResponse.setOrderId(orderId);
			orderResponse.setStatus("PLACED");
			orderEventProducer.sendOrderCreatedMessage(oreCreatedEvent);
			System.out.println("Order Created Succesfully ✅");
			System.out.println("Order Id :"+orderResponse.getOrderId());
			return orderResponse;
		}
		orderResponse.setOrderId(orderId);
		orderResponse.setStatus("FAILED");
		orderEventProducer.sendOrderFailedMessage(orderFailedEvent);
		return orderResponse;
	}

	private String generateOrderId() {
		Random random = new Random();
		String orderId = "ORD" + random.nextInt(1000) + 9000;
		return orderId;
	}

	private String generateEventId() {
		Random random = new Random();
		String eventId = "EVT-" + random.nextInt(1000) + 9000;
		return eventId;
	}

}

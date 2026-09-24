package com.flipkart.order.event;

public class OrderCreatedEvent {

	private String eventId;
	private String eventType;
	private String orderId;
	private int customerId;
	private long amount;
	private String paymentMethod;
	private String deliveryAddress;

	public OrderCreatedEvent() {

	}

	public OrderCreatedEvent(String eventId, String eventType, String orderId, int customerId, long amount,
			String paymentMethod, String deliveryAddress) {
		super();
		this.eventId = eventId;
		this.eventType = eventType;
		this.orderId = orderId;
		this.customerId = customerId;
		this.amount = amount;
		this.paymentMethod = paymentMethod;
		this.deliveryAddress = deliveryAddress;
	}

	public String getEventId() {
		return eventId;
	}

	public void setEventId(String eventId) {
		this.eventId = eventId;
	}

	public String getEventType() {
		return eventType;
	}

	public void setEventType(String eventType) {
		this.eventType = eventType;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public int getCustomerId() {
		return customerId;
	}

	public void setCustomerId(int customerId) {
		this.customerId = customerId;
	}

	public long getAmount() {
		return amount;
	}

	public void setAmount(long amount) {
		this.amount = amount;
	}

	public String getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	public String getDeliveryAddress() {
		return deliveryAddress;
	}

	public void setDeliveryAddress(String deliveryAddress) {
		this.deliveryAddress = deliveryAddress;
	}

}

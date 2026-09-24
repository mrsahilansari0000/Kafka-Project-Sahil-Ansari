package com.flipkart.delivery.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.flipkart.delivery.entity.DeliveryEntity;
import com.flipkart.order.entity.OrderEntity;

@Repository
public interface DeliveryRepository extends JpaRepository<DeliveryEntity , Long> {
	
}

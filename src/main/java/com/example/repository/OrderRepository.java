package com.example.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.entity.Order;
import com.example.entity.OrderStatus;
import com.example.entity.Painting;
import com.example.entity.PaymentMethod;
import com.example.entity.User;

public interface OrderRepository extends JpaRepository<Order,Long> {

	List<Order> findByUser(User user);
	
	List<Order> findByStatusAndPaymentMethodAndOrderDateBefore(OrderStatus status, PaymentMethod paymentMethod, LocalDateTime timeLimit);
		
	boolean existsByPaintingAndStatusNot(Painting painting, OrderStatus order);
}

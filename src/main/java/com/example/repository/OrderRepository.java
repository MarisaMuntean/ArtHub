package com.example.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.entity.Order;
import com.example.entity.User;

public interface OrderRepository extends JpaRepository<Order,Long> {

	List<Order> findByUser(User user);
}

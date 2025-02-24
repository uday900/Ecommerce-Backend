package com.uday.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.uday.entity.Order;
import com.uday.entity.User;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUser(User user);
}

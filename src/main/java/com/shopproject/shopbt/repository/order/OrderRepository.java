package com.shopproject.shopbt.repository.order;

import com.shopproject.shopbt.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Set;

public interface OrderRepository extends JpaRepository<Order, Long>{
    Set<Order> findOrdersByUser_Userid(Long id);

    Set<Order> findOrdersByCreateAtBetween(LocalDateTime start, LocalDateTime end);
}

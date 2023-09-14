package com.shopproject.shopbt.service.order;

import com.shopproject.shopbt.dto.OrdersDTO;

import java.time.LocalDateTime;
import java.util.Set;

public interface OrderService {
    void create_Order(OrdersDTO ordersDTO);

    OrdersDTO findOrderById(Long id);

    void update_Order(OrdersDTO ordersDTO);

    void delete_OrderById(Long id);

    Set<OrdersDTO> findOrdersByUserId(Long id);

    Set<OrdersDTO> findOrdersByToday(LocalDateTime startDate, LocalDateTime endDate);
}

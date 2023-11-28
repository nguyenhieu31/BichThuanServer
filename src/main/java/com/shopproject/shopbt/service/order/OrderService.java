package com.shopproject.shopbt.service.order;

import com.shopproject.shopbt.dto.OrdersDTO;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public interface OrderService {
    OrdersDTO create_Order(OrdersDTO ordersDTO);

    OrdersDTO findOrderById(Long id);

    void update_Order(OrdersDTO ordersDTO);

    void delete_OrderById(Long id);

    Set<OrdersDTO> findOrdersByUserId(Long id);

    List<OrdersDTO> findLatestOrders(Pageable pageable);

    Set<OrdersDTO> findALLByOrderToday();

    Set<OrdersDTO> findAllOrderBy7Day();
}

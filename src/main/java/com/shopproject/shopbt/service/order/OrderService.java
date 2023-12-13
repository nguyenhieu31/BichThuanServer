package com.shopproject.shopbt.service.order;

import com.shopproject.shopbt.dto.OrdersDTO;
import com.shopproject.shopbt.entity.Order;
import com.shopproject.shopbt.entity.User;
import com.shopproject.shopbt.request.CreateOrderRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public interface OrderService {
    OrdersDTO create_Order(CreateOrderRequest request, User user) throws Exception;

    OrdersDTO findOrderById(Long id);

    void update_Order(OrdersDTO ordersDTO);

    void delete_OrderById(Long id);

    List<Order> findOrdersByUserId(Long id);

    List<OrdersDTO> findLatestOrders(Pageable pageable);

    Set<OrdersDTO> findALLByOrderToday();

    Set<OrdersDTO> findAllOrderBy7Day();

    OrdersDTO findStatusByOrderId(Long id);
    Order cancelOrder(Order order, String reasonCancel) throws Exception;
    Set<Order> findOrderByStatusAndUserId(int type, Long userId) throws Exception;
}
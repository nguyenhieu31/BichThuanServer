package com.shopproject.shopbt.service.orderitem;

import com.shopproject.shopbt.dto.OrderItemsDTO;

import java.util.List;
import java.util.Set;

public interface OrderItemService {

    void create(Set<OrderItemsDTO> orderItemsDTO);

    Set<OrderItemsDTO> findAllByOrderId(Long orderId);
}
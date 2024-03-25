package com.shopproject.shopbt.service.orderitem;

import com.shopproject.shopbt.dto.OrderItemsDTO;
import com.shopproject.shopbt.response.Product_Detail_Order;

import java.util.List;
import java.util.Set;

public interface OrderItemService {

    void create(Set<OrderItemsDTO> orderItemsDTO);

    Set<Product_Detail_Order> findAllByOrderId(Long orderId);
}

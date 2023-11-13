package com.shopproject.shopbt.service.orderitem;

import com.shopproject.shopbt.dto.OrderItemsDTO;
import com.shopproject.shopbt.entity.OrderItem;
import com.shopproject.shopbt.repository.order.OrderRepository;
import com.shopproject.shopbt.repository.orderitem.OrderItemRepository;
import com.shopproject.shopbt.repository.product.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class OrderItemServiceImpl implements OrderItemService{
    @Autowired
    OrderItemRepository orderItemRepository;
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    ProductRepository productRepository;

    @Override
    public void create(Set<OrderItemsDTO> orderItemsDTOs) {
        orderItemsDTOs.forEach(orderItemsDTO -> {
            orderItemRepository.save(readOrderItemDTO(orderItemsDTO));
        });
    }

    private OrderItem readOrderItemDTO(OrderItemsDTO orderItemsDTO){
        OrderItem orderItem = new OrderItem();
        Long id = orderItemsDTO.getOrderId();
        orderItem.setOrder(orderRepository.findOrderByOrderId(id).orElseThrow(() -> new RuntimeException("Order not found by id : " + id)));
        orderItem.setProduct(productRepository.findByProductId(orderItemsDTO.getProductId()));
        orderItem.setPricePerUnit(orderItemsDTO.getPricePerUnit());
        orderItem.setQuantity(orderItemsDTO.getQuantity());

        return orderItem;
    }
    @Override
    public Set<OrderItemsDTO> findAllByOrderId(Long orderId) {
        try{
            Set<Object[]> orderItems = orderItemRepository.findALlByOrderId(orderId);
            Set<OrderItemsDTO> orderItemsDTOS = orderItems.stream()
                    .map(this::ConvertToDto)
                    .collect(Collectors.toSet());
            return orderItemsDTOS;
        } catch (IllegalArgumentException e){
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    private OrderItemsDTO ConvertToDto(Object[] orderItem){
        OrderItemsDTO orderItemsDTO = new OrderItemsDTO();
        orderItemsDTO.setPricePerUnit((BigDecimal) orderItem[0]);
        orderItemsDTO.setQuantity((int) orderItem[1]);
        orderItemsDTO.setProductId((Long) orderItem[2]);
        orderItemsDTO.setOrderId((Long) orderItem[3]);
        return orderItemsDTO;
    }
}

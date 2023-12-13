package com.shopproject.shopbt.service.orderitem;

import com.shopproject.shopbt.dto.OrderItemsDTO;
import com.shopproject.shopbt.entity.Order;
import com.shopproject.shopbt.entity.OrderItem;
import com.shopproject.shopbt.repository.order.OrderRepository;
import com.shopproject.shopbt.repository.orderitem.OrderItemRepository;
import com.shopproject.shopbt.repository.product.ProductRepository;
import com.shopproject.shopbt.response.Product_Detail_Order;
import org.modelmapper.ModelMapper;
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
        orderItem.setOrder(orderRepository.findById(orderItemsDTO.getOrderId()).get());
        Long id = orderItemsDTO.getOrderId();
        orderItem.setOrder(orderRepository.findOrderByOrderId(id).orElseThrow(() -> new RuntimeException("Order not found by id : " + id)));
        orderItem.setProduct(productRepository.findByProductId(orderItemsDTO.getProductId()));
        orderItem.setPricePerUnit(orderItemsDTO.getPricePerUnit());
        orderItem.setQuantity(orderItemsDTO.getQuantity());
        return orderItem;
    }
    @Override
    public Set<Product_Detail_Order> findAllByOrderId(Long orderId) {
        try{
            return orderItemRepository.findALlByOrderId(orderId);
        } catch (IllegalArgumentException e){
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    private OrderItemsDTO ConvertToDto(Product_Detail_Order orderItem){

        return null;
    }
}

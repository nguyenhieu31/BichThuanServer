package com.shopproject.shopbt.order;

import com.shopproject.shopbt.dto.OrdersDTO;
import com.shopproject.shopbt.service.order.OrderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.Set;

@SpringBootTest
public class TestOrder {
    @Autowired
    private OrderService orderService;

    @Test
    void create(){
        OrdersDTO ordersDTO = new OrdersDTO();
        ordersDTO.setStatus(1);
        ordersDTO.setUserId(3L);
        orderService.create_Order(ordersDTO);
    }

    @Test
    void findById(){
        Long id = 1L;
        OrdersDTO ordersDTO = orderService.findOrderById(id);
        System.out.println(ordersDTO.getStatus());
        System.out.println(ordersDTO.getUserId());
    }

    @Test
    void update(){
        Long id = 1L;
        OrdersDTO ordersDTO = orderService.findOrderById(id);
        ordersDTO.setStatus(2);
        orderService.update_Order(ordersDTO);
    }

    @Test
    void delete(){
        Long id = 1L;
        orderService.delete_OrderById(id);
    }

    @Test
    void findOrdersByUserId(){
        Long id = 2L;
        Set<OrdersDTO> ordersDTOS = orderService.findOrdersByUserId(id);
        System.out.println(ordersDTOS.size());
        ordersDTOS.forEach(ordersDTO -> {
            System.out.println(ordersDTO.getUserId());
            System.out.println(ordersDTO.getStatus());
        });
    }

    @Test
    void findOrdersByCreateToday(){
        LocalDateTime start = LocalDateTime.now().toLocalDate().atStartOfDay();
        LocalDateTime end = start.plusDays(1);
        Set<OrdersDTO> ordersDTOS = orderService.findOrdersByToday(start, end);
        ordersDTOS.forEach(ordersDTO -> {
            System.out.println(ordersDTO.getUserId());
            System.out.println(ordersDTO.getStatus());
        });
    }
}

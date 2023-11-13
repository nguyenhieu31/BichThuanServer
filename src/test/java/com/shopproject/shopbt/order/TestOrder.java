package com.shopproject.shopbt.order;

import com.shopproject.shopbt.dto.OrdersDTO;
import com.shopproject.shopbt.request.OffsetBasedPageRequest;
import com.shopproject.shopbt.service.order.OrderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;

@SpringBootTest
public class TestOrder {
    @Autowired
    private OrderService orderService;

    @Test
    void create(){
        OrdersDTO ordersDTO = new OrdersDTO();
        ordersDTO.setStatus(1);
        ordersDTO.setUserId(7L);
        ordersDTO.setAddress("175, tran nhan tong, vinh dien, dien ban, quang nam");
        orderService.create_Order(ordersDTO);
    }

    @Test
    void findById(){
//        Long id = 1L;
//        OrdersDTO ordersDTO = orderService.findOrderById(id);
//        System.out.println(ordersDTO.getStatus());
//        System.out.println(ordersDTO.getUserId());
    }

    @Test
    void update(){
//        Long id = 1L;
//        OrdersDTO ordersDTO = orderService.findOrderById(id);
//        ordersDTO.setStatus(2);
//        orderService.update_Order(ordersDTO);
    }

    @Test
    void delete(){
//        Long id = 1L;
//        orderService.delete_OrderById(id);
    }

    @Test
    void findOrdersByUserId(){
//        Long id = 2L;
//        Set<OrdersDTO> ordersDTOS = orderService.findOrdersByUserId(id);
//        System.out.println(ordersDTOS.size());
//        ordersDTOS.forEach(ordersDTO -> {
//            System.out.println(ordersDTO.getUserId());
//            System.out.println(ordersDTO.getStatus());
//        });
    }

    @Test
    void findOrdersByCreateToday(){
//        LocalDateTime start = LocalDateTime.now().toLocalDate().atStartOfDay();
//        LocalDateTime end = start.plusDays(1);
//        Set<OrdersDTO> ordersDTOS = orderService.findOrdersByToday(start, end);
//        ordersDTOS.forEach(ordersDTO -> {
//            System.out.println(ordersDTO.getUserId());
//            System.out.println(ordersDTO.getStatus());
//        });
    }

    @Test
    void findLatestOrders(){
        Pageable pageable = new OffsetBasedPageRequest(0,6, Sort.Direction.DESC, "createdAt");
        List<OrdersDTO> ordersDTOS = orderService.findLatestOrders(pageable);
        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        ordersDTOS.forEach(ordersDTO -> {
            System.out.println(ordersDTO.getCreateAt().format(format));
        });
    }

    @Test
    void findALLByOrderToday(){
        Set<OrdersDTO> orderToday = orderService.findALLByOrderToday();

        System.out.println(orderToday.size());
    }

    @Test
    void findAllOrderBy7Day(){
        Set<OrdersDTO> order_7day = orderService.findAllOrderBy7Day();

        System.out.println(order_7day.size());
    }
}

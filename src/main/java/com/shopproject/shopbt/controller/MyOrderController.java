package com.shopproject.shopbt.controller;

import com.shopproject.shopbt.dto.OrderItemsDTO;
import com.shopproject.shopbt.dto.OrdersDTO;
import com.shopproject.shopbt.dto.UsersDTO;
import com.shopproject.shopbt.entity.OrderItem;
import com.shopproject.shopbt.response.Product_MyOrder;
import com.shopproject.shopbt.service.order.OrderService;
import com.shopproject.shopbt.service.orderitem.OrderItemService;
import com.shopproject.shopbt.service.user.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@AllArgsConstructor
@RequestMapping("/web")
public class MyOrderController {

    private final OrderItemService orderItemService;
    private final OrderService orderService;
    private final UserService userService;

    @GetMapping("/myorder")
    public ResponseEntity<?> getMyOrder(){
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String username = userDetails.getUsername();
            List<UsersDTO> usersDTOs = userService.getByUserName(username);
            Set<Long> orderIds = new HashSet<>();

            usersDTOs.forEach(usersDTO -> {
                orderIds.add(usersDTO.getOrderid());
            });

            Set<OrderItemsDTO> orders_w_confirm = new HashSet<>();
            Set<OrderItemsDTO> orders_w_delivery = new HashSet<>();
            Set<OrderItemsDTO> orders_delivering = new HashSet<>();
            Set<OrderItemsDTO> orders_delivered = new HashSet<>();
            Set<OrderItemsDTO> orders_return = new HashSet<>();

            orderIds.forEach(orderId -> {
                OrdersDTO orderStatus = orderService.findStatusByOrderId(orderId);

                if (orderStatus.getStatus() == 1) {
                    Set<OrderItemsDTO> item1 = orderItemService.findAllByOrderId(orderId);
                    orders_w_confirm.addAll(item1);
                } else if (orderStatus.getStatus() == 2) {
                    Set<OrderItemsDTO> item2 = orderItemService.findAllByOrderId(orderId);
                    orders_w_delivery.addAll(item2);
                } else if (orderStatus.getStatus() == 3) {
                    Set<OrderItemsDTO> item3 = orderItemService.findAllByOrderId(orderId);
                    orders_delivering.addAll(item3);
                } else if (orderStatus.getStatus() == 4) {
                    Set<OrderItemsDTO> item4 = orderItemService.findAllByOrderId(orderId);
                    orders_delivered.addAll(item4);
                } else {
                    Set<OrderItemsDTO> item5 = orderItemService.findAllByOrderId(orderId);
                    orders_return.addAll(item5);
                }
            });


            return ResponseEntity.status(200).body(Product_MyOrder.builder()
                    .product_w_confirm(orders_w_confirm)
                    .product_w_delivery(orders_w_delivery)
                    .product_delivering(orders_delivering)
                    .product_delivered(orders_delivered)
                    .product_return(orders_return)
                    .build());
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}

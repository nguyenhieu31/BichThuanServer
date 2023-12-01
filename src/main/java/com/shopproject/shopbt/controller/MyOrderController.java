package com.shopproject.shopbt.controller;

import com.shopproject.shopbt.dto.OrderItemsDTO;
import com.shopproject.shopbt.dto.UsersDTO;
import com.shopproject.shopbt.entity.OrderItem;
import com.shopproject.shopbt.response.Product_MyOrder;
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

    private OrderItemService orderItemService;
    private UserService userService;

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

            Set<OrderItemsDTO> orderItems = new HashSet<>();

            orderIds.forEach(orderId -> {
                Set<OrderItemsDTO> item = orderItemService.findAllByOrderId(orderId);

                orderItems.addAll(item);
            });

            return ResponseEntity.status(200).body(Product_MyOrder.builder()
                    .product_myorder(orderItems)
                    .build());
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
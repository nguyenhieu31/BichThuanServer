package com.shopproject.shopbt.controller;

import com.shopproject.shopbt.dto.OrderItemsDTO;
import com.shopproject.shopbt.dto.OrdersDTO;
import com.shopproject.shopbt.dto.UsersDTO;
import com.shopproject.shopbt.request.CreateOrderRequest;
import com.shopproject.shopbt.service.order.OrderService;
import com.shopproject.shopbt.service.orderitem.OrderItemService;
import com.shopproject.shopbt.service.user.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.Set;

@RestController
@AllArgsConstructor
@RequestMapping("/web/order")
public class CreateOrderController {
    private final OrderService orderService;
    private final OrderItemService orderItemService;
    private final UserService userService;


}

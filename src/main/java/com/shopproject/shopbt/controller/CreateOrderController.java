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
@RequestMapping("/web")
public class CreateOrderController {
    private final OrderService orderService;
    private final OrderItemService orderItemService;
    private final UserService userService;

    @PostMapping("/auth/create")
    public ResponseEntity<?> CreateOrder(@RequestBody CreateOrderRequest createOrderRequest){
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String userName = userDetails.getUsername();
            UsersDTO usersDTO = userService.findUserIdByUserName(userName);
            Set<OrderItemsDTO> orderItemsDTOS = new HashSet<>();

            OrdersDTO newOrder = new OrdersDTO();
            newOrder.setStatus(1);
            newOrder.setUserId(usersDTO.getUserid());
            newOrder.setAddress(createOrderRequest.getAddressDTO().getAddress());

            OrdersDTO saveOrder = orderService.create_Order(newOrder);

            Set<OrderItemsDTO> listOrders = new HashSet<>();
            createOrderRequest.getProductsDTOs().forEach(productsRequest -> {
                OrderItemsDTO orderitem = new OrderItemsDTO();
                orderitem.setOrderId(saveOrder.getOrderId());
                orderitem.setQuantity(productsRequest.getQuantity());
                orderitem.setPricePerUnit(productsRequest.getPrice());
                orderitem.setProductId(productsRequest.getProductId());
                orderitem.setSize(productsRequest.getSize());
                orderitem.setColor(productsRequest.getColor());

                listOrders.add(orderitem);
            });

            orderItemService.create(listOrders);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Bạn đã đặt hàng thành công!!!");
        } catch (Exception ex){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }
}

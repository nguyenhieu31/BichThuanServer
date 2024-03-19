package com.shopproject.shopbt.controller.admin;

import com.google.api.Http;
import com.shopproject.shopbt.dto.OrdersDTO;
import com.shopproject.shopbt.response.OrderResponse;
import com.shopproject.shopbt.service.order.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/system")
@AllArgsConstructor
public class AdminOrderController {
    private final OrderService orderService;

    private Set<OrderResponse> extractToOrderResponse(Collection<OrdersDTO> orders) {
        Set<OrderResponse> response = new HashSet<>();
        orders.forEach(order -> {
            String code = null;
            if (order.getOrderCode() != null) {
                int findLastStringIndex = order.getOrderCode().toString().lastIndexOf("-");
                code = order.getOrderCode().toString().substring(findLastStringIndex + 1);
            }
            var result = OrderResponse.builder()
                    .orderId(order.getOrderId())
                    .orderCode(code == null ? "" : code)
                    .productImage(order.getProductImage())
                    .priceUnit(order.getPriceUnit())
                    .productName(order.getProductName())
                    .fullName(order.getFullName())
                    .size(order.getSize())
                    .userId(order.getUserId())
                    .quantity(order.getQuantity())
                    .color(order.getColor())
                    .address(order.getAddress())
                    .status(order.getStatus())
                    .createdAt(order.getCreatedAt())
                    .build();
            response.add(result);
        });
        return response;
    }

    @GetMapping("/order/all")
    public ResponseEntity<?> getAllOrder() {
        try {
            List<OrdersDTO> getAllOrders = orderService.findAllOrder();
            Set<OrderResponse> response = extractToOrderResponse(getAllOrders);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    @PutMapping("/order/handle-order")
    public ResponseEntity<?> handleOrder(@RequestBody Set<String> listOrderCode){
        try{
            return ResponseEntity.status(HttpStatus.OK).body(orderService.handleOrder(listOrderCode));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    @GetMapping("/order/status/{status}")
    public ResponseEntity<?> getOrdersByStatus(@PathVariable("status") Long status) {
        try {
            List<OrdersDTO> getOrders = orderService.getAllOrderByStatus(status);
            Set<OrderResponse> response = extractToOrderResponse(getOrders);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/order/today")
    public ResponseEntity<?> AllOrderToday() {
        try {
            Set<OrdersDTO> all_order_today = orderService.findALLByOrderToday();
            Set<OrderResponse> response = extractToOrderResponse(all_order_today);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

    @GetMapping("/order/7-days")
    public ResponseEntity<?> AllOrder7Days() {
        try {
            Set<OrdersDTO> all_order_7days = orderService.findAllOrderBy7Day();
            Set<OrderResponse> response = extractToOrderResponse(all_order_7days);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

    @PutMapping("/order/{id}")
    public ResponseEntity<?> UpdateOrder(@PathVariable("id") Long orderId, @RequestBody OrdersDTO ordersDTO) {
        try {
            ordersDTO.setOrderId(orderId);
            orderService.update_Order(ordersDTO);
            return ResponseEntity.status(HttpStatus.OK).body(ordersDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping("/order/{id}")
    public ResponseEntity<?> DeleteOrder(@PathVariable("id") Long orderId) {
        try {
            orderService.delete_OrderById(orderId);
            return ResponseEntity.status(HttpStatus.OK).body("Successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/orders")
    public ResponseEntity<?> getAllOrderOfUser() {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    @PutMapping("/order/update-status/{status}")
    public ResponseEntity<?> updateStatusOrder(@PathVariable("status") int status,  @RequestBody Set<String> listOrderCode){
        try{
            return ResponseEntity.status(HttpStatus.OK).body(null);
        }catch(Exception e){
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }
}

package com.shopproject.shopbt.controller;

import com.shopproject.shopbt.dto.OrdersDTO;
import com.shopproject.shopbt.service.order.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/system")
@AllArgsConstructor
public class AdminOrderController {
    private final OrderService orderService;

    @PutMapping("/order/{id}")
    public ResponseEntity<?> UpdateOrder(@PathVariable("id") Long orderId,@RequestBody OrdersDTO ordersDTO){
        try{
            ordersDTO.setOrderId(orderId);
            orderService.update_Order(ordersDTO);
            return ResponseEntity.status(HttpStatus.OK).body(ordersDTO);
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping("/order/{id}")
    public ResponseEntity<?> DeleteOrder(@PathVariable("id") Long orderId){
        try{
            orderService.delete_OrderById(orderId);
            return ResponseEntity.status(HttpStatus.OK).body("Successfully");
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}

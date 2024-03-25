package com.shopproject.shopbt.controller.admin;

import com.shopproject.shopbt.response.DataTodayResponse;
import com.shopproject.shopbt.service.order.OrderService;
import com.shopproject.shopbt.service.user.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequestMapping("/system")
@AllArgsConstructor
public class DataTodayController {
    private UserService userService;
    private OrderService orderService;

    @GetMapping("/data-today")
    public ResponseEntity<?> DataToday(){
        try {
            DataTodayResponse dataTodayResponse = DataTodayResponse.builder()
                    .total_income((orderService.totalIncomeToday() != null)? orderService.totalIncomeToday() : BigDecimal.ZERO)
                    .quantity_person_register((userService.quantityPersonRegisterToday() != null) ? userService.quantityPersonRegisterToday() : 0)
                    .quantity_orders((orderService.quantityOrdersToday() != null) ? orderService.quantityOrdersToday() : 0)
                    .quantity_users_in_chart(userService.quantityPersonRegisterInChart())
                    .quantity_orders_in_chart(orderService.quantityPersonOrderInChart())
                    .build();
            return ResponseEntity.status(HttpStatus.OK).body(dataTodayResponse);
        } catch (Exception ex){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }
}

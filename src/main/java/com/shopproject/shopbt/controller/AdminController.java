package com.shopproject.shopbt.controller;

import com.shopproject.shopbt.dto.CategoriesDTO;
import com.shopproject.shopbt.dto.OrdersDTO;
import com.shopproject.shopbt.dto.ProductsDTO;
import com.shopproject.shopbt.dto.UsersDTO;
import com.shopproject.shopbt.request.OffsetBasedPageRequest;
import com.shopproject.shopbt.response.Overview_Admin;
import com.shopproject.shopbt.service.catrgory.CategoryService;
import com.shopproject.shopbt.service.order.OrderService;
import com.shopproject.shopbt.service.product.ProductService;
import com.shopproject.shopbt.service.user.UserService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/system")
@AllArgsConstructor
public class AdminController {
    private CategoryService categoryService;
    private ProductService productService;
    private OrderService orderService;
    private UserService userService;
    @GetMapping("/admin")
    public ResponseEntity<?> overviewAdmin(){
        try {
            Set<CategoriesDTO> categoriesDTOS = categoryService.findAllCategory();

            Pageable pageableProduct = new OffsetBasedPageRequest(0,5, Sort.Direction.ASC,"updatedAt");
            Set<ProductsDTO> productsDTOS = productService.findALLByLimitOffset(pageableProduct);

            Pageable pageableOrder = new OffsetBasedPageRequest(0,6, Sort.Direction.DESC, "createdAt");
            List<OrdersDTO> ordersDTOS = orderService.findLatestOrders(pageableOrder);
            return ResponseEntity.status(HttpStatus.OK).body(Overview_Admin
                    .builder()
                    .categoriesDTOS(categoriesDTOS)
                    .productsDTOS(productsDTOS)
                    .ordersDTOS(ordersDTOS)
                    .build()
            );
        } catch (Exception ex){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

    @GetMapping("/order-today")
    public ResponseEntity<?> AllOrderToday(){
        try {
            Set<OrdersDTO> all_order_today = orderService.findALLByOrderToday();
            return ResponseEntity.status(HttpStatus.OK).body(all_order_today);
        } catch (Exception ex){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

    @GetMapping("/order-7-days")
    public ResponseEntity<?> AllOrder7Days(){
        try {
            Set<OrdersDTO> all_order_7days = orderService.findAllOrderBy7Day();
            return ResponseEntity.status(HttpStatus.OK).body(all_order_7days);
        } catch (Exception ex){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

    @GetMapping("/user-today")
    public ResponseEntity<?> AllUserToday(){
        try {
            Set<UsersDTO> all_user_day = userService.findAllUserRegisterByToday();
            return ResponseEntity.status(HttpStatus.OK).body(all_user_day);
        } catch (Exception ex){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

    @GetMapping("/user-7days")
    public ResponseEntity<?> AllUser7Days(){
        try {
            Set<UsersDTO> all_user_7days = userService.findAllUserRegisterBy7Days();
            return ResponseEntity.status(HttpStatus.OK).body(all_user_7days);
        } catch (Exception ex){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }
}

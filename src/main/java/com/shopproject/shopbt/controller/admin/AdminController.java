package com.shopproject.shopbt.controller.admin;

import com.shopproject.shopbt.dto.CategoriesDTO;
import com.shopproject.shopbt.dto.OrdersDTO;
import com.shopproject.shopbt.dto.ProductsDTO;
import com.shopproject.shopbt.dto.UsersDTO;
import com.shopproject.shopbt.request.OffsetBasedPageRequest;
import com.shopproject.shopbt.response.OrderResponse;
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
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.HashSet;
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
        @GetMapping("/permission-page")
        public ResponseEntity<?> checkPermissionAccessPage(){
            try{
                Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
                if(!(authentication instanceof AnonymousAuthenticationToken) && authentication.getAuthorities().stream().anyMatch(role->role.getAuthority().equals("ROLE_ADMIN"))){
                    return ResponseEntity.status(HttpStatus.OK).body("Có quyền truy cập vào trang!");
                }
                return ResponseEntity.status(401).body("Tài khoản này không có quyền truy cập vào trang!");
            }catch(Exception e){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
            }
        }
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

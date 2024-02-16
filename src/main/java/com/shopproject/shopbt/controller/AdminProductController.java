package com.shopproject.shopbt.controller;

import com.shopproject.shopbt.dto.ProductsDTO;
import com.shopproject.shopbt.service.product.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/system")
@AllArgsConstructor
public class AdminProductController {
    private final ProductService productService;

    @GetMapping("/products")
    public ResponseEntity<?> AllOrder7Days(){
        try {
            List<ProductsDTO> all_products = productService.findAllProduct();
            return ResponseEntity.status(HttpStatus.OK).body(Objects.requireNonNullElse(all_products, "Không có sản phẩm nào"));
        } catch (Exception ex){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }


}

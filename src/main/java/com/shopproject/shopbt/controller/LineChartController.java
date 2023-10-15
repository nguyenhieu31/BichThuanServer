package com.shopproject.shopbt.controller;

import com.shopproject.shopbt.dto.ProductsDTO;
import com.shopproject.shopbt.entity.Product;
import com.shopproject.shopbt.service.product.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/products")
public class LineChartController {
    @Autowired
    private ProductService productService;

//    @GetMapping("/category/{id}")
//    public ResponseEntity<List<Product>> getProductsByCategory(@PathVariable("id") Long categoryId) {
//        List<Product> products = productService.findByCategoryId(categoryId);
//        return new ResponseEntity<>(products, HttpStatus.OK);
//    }
}


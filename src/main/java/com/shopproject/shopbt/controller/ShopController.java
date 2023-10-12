package com.shopproject.shopbt.controller;

import com.shopproject.shopbt.dto.CategoriesDTO;
import com.shopproject.shopbt.dto.ProductsDTO;
import com.shopproject.shopbt.service.catrgory.CategoryService;
import com.shopproject.shopbt.service.catrgory.CategoryServiceImpl;
import com.shopproject.shopbt.service.product.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Set;

@RestController
@AllArgsConstructor
@RequestMapping("/web")
public class ShopController {
    private CategoryService categoryService;
    private ProductService productService;
    @GetMapping("/category")
    public ResponseEntity<?> findAllCategory(){
        try{
            Set<CategoriesDTO> categoriesDTOS= categoryService.findAllCategory();
            return ResponseEntity.status(200).body(categoriesDTOS);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("category/{id}")
    public ResponseEntity<?> findProductsByCategoryId(@PathVariable("id") Long id){
        try{
            Set<ProductsDTO> productsDTOS= productService.findProductsByCategoryId(id);
            return ResponseEntity.status(200).body(productsDTOS);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
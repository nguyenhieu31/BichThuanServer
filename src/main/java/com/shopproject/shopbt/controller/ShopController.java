package com.shopproject.shopbt.controller;

import com.shopproject.shopbt.dto.CategoriesDTO;
import com.shopproject.shopbt.dto.ProductsDTO;
import com.shopproject.shopbt.request.OffsetBasedPageRequest;
import com.shopproject.shopbt.service.catrgory.CategoryServiceImpl;
import com.shopproject.shopbt.service.product.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@AllArgsConstructor
@CrossOrigin
@RequestMapping("/web")
public class ShopController {
    private CategoryServiceImpl categoryService;
    private ProductService productService;
    @GetMapping("/category")
    public ResponseEntity<?> findAllCategory(){
        try{
            Set<CategoriesDTO> categoriesDTOS= categoryService.findAllCategory();
            return ResponseEntity.status(200).body(categoriesDTOS);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    @GetMapping("/shop")
    public ResponseEntity<?> findProductShop(@RequestParam int offset, @RequestParam int limit){
        try{
            Pageable pageable= new OffsetBasedPageRequest(offset,limit, Sort.Direction.ASC,"productId");
            Set<ProductsDTO> products= productService.findALLByLimitOffset(pageable);
            return ResponseEntity.status(200).body(products);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    @GetMapping("/shop/products")
    public ResponseEntity<?> findProductByCategoryId(@RequestParam("categoryId") Long categoryId){
        try{
            return ResponseEntity.status(HttpStatus.OK).body(productService.findProductsByCategoryId(categoryId));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    @GetMapping("/products/search")
    public ResponseEntity<?> findProductBySearch(@RequestParam("searchName") String searchName){
        try{
            return ResponseEntity.status(HttpStatus.OK).body(productService.findByNameLikeIgnoreCase(searchName));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

}

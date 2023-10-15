package com.shopproject.shopbt.controller;

import com.shopproject.shopbt.request.AddToCartRequest;
import com.shopproject.shopbt.service.product_cart.Product_CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@CrossOrigin
@RequestMapping("/web/addToCart")
public class CartController {
    private final Product_CartService productCartService;
    @PostMapping("/products/{id}")
    public ResponseEntity<?> AddProductToCart(@PathVariable("id") Long id, @RequestBody AddToCartRequest request){
        try{
            String message= productCartService.create_Product_Cart(id, request);
            return ResponseEntity.status(HttpStatus.OK).body(message);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    public ResponseEntity<?> getAllProductInCart(){
        return null;
    }
}

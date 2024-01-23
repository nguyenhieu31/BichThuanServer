package com.shopproject.shopbt.controller;

import com.shopproject.shopbt.ExceptionCustom.LoginException;
import com.shopproject.shopbt.entity.Product_Cart;
import com.shopproject.shopbt.request.AddToCartRequest;
import com.shopproject.shopbt.request.CartRequest;
import com.shopproject.shopbt.request.ProductCartRequest;
import com.shopproject.shopbt.response.CartResponse;
import com.shopproject.shopbt.service.product_cart.Product_CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@CrossOrigin
@RequestMapping("/web/cart")
public class CartController {
    private final Product_CartService productCartService;
    @PostMapping("/products/addToCart/{id}")
    public ResponseEntity<?> AddProductToCart(@PathVariable("id") Long id, @RequestBody AddToCartRequest request){
        try{
            String message= productCartService.create_Product_Cart(id, request);
            return ResponseEntity.status(HttpStatus.OK).body(message);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    @GetMapping("/products")
    public ResponseEntity<?> getAllProductInCart(){
        try{
            return ResponseEntity.status(HttpStatus.OK).body(productCartService.getAllProductCartByUser());
        }catch (LoginException | ClassCastException e){
            return ResponseEntity.status(403).body(e.getMessage());
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    @PutMapping("/products/increase/{productCartId}")
    public ResponseEntity<?> incrementalProductCart(@PathVariable("productCartId") Long productCartId, @RequestBody ProductCartRequest productCartRequest){
        try{
            return ResponseEntity.status(HttpStatus.OK).body(productCartService.incrementProductCart(productCartId,productCartRequest));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    @PutMapping("/products/decrease/{productCartId}")
    public ResponseEntity<?> decrementalProductCart(@PathVariable("productCartId") Long productCartId, @RequestBody ProductCartRequest productCartRequest){
        try{
            return ResponseEntity.status(HttpStatus.OK).body(productCartService.decrementProductCart(productCartId,productCartRequest));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    @DeleteMapping("/products/delete/{productCartId}")
    public ResponseEntity<String> removeProductCartById(@PathVariable("productCartId") Long productCartId){
        try{
            productCartService.delete_Product_CartById(productCartId);
            return ResponseEntity.status(HttpStatus.OK).body("xóa sản phẩm trong giỏ thành công");
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}

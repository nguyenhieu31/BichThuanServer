package com.shopproject.shopbt.product_cart;

import com.shopproject.shopbt.dto.ProductCartsDTO;
import com.shopproject.shopbt.service.product_cart.Product_CartService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TestProduct_Cart {
    @Autowired
    private Product_CartService productCartService;

    @Test
    void create(){
        ProductCartsDTO productCartsDTO = new ProductCartsDTO();
        Long cartId = 1L;
        productCartsDTO.setCartId(cartId);
        Long productId = 4L;
        productCartsDTO.setProductId(productId);
        productCartsDTO.setColor("Green");
        productCartsDTO.setQuantity(100);
        productCartsDTO.setSize("XL");
        productCartsDTO.setStatus(1);

        productCartService.create_Product_Cart(productCartsDTO);
    }

    @Test
    void findById(){
        Long id = 1L;
        ProductCartsDTO productCartsDTO = productCartService.findProduct_CartById(id);
        System.out.println(productCartsDTO.getProductId());
        System.out.println(productCartsDTO.getCartId());
    }

    @Test
    void update(){
        Long id = 1L;
        ProductCartsDTO productCartsDTO = productCartService.findProduct_CartById(id);
        productCartsDTO.setStatus(3);
        productCartsDTO.setSize("M");
        productCartsDTO.setQuantity(50);
        Long productId = 5L;
        productCartsDTO.setProductId(productId);

        productCartService.update_Product_Cart(productCartsDTO);
    }
}

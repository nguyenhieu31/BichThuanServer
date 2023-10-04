package com.shopproject.shopbt.product_cart;

import com.shopproject.shopbt.dto.ProductCartsDTO;
import com.shopproject.shopbt.service.product_cart.Product_CartService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Set;

@SpringBootTest
public class TestProduct_Cart {
    @Autowired
    private Product_CartService productCartService;

    @Test
    void create(){
        ProductCartsDTO productCartsDTO = new ProductCartsDTO();
        Long cartId = 2L;
        productCartsDTO.setCartId(cartId);
        Long productId = 6L;
        productCartsDTO.setProductId(productId);
        productCartsDTO.setColor("Red");
        productCartsDTO.setQuantity(2);
        productCartsDTO.setSize("L");
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

    @Test
    void findProduct_CartByCartId(){
        Long id = 2L;
        Set<ProductCartsDTO> productCartsDTOS = productCartService.findProduct_CartByCartId(id);
    }
}

package com.shopproject.shopbt.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
public class CartResponse {
    private Long productCartId;
    private Long productId;
    private String name;
    private String color;
    private String size;
    private BigDecimal price;
    private String image;
    private int quantity;

    public CartResponse(Long productCartId, Long productId, String name, String color, String size, BigDecimal price, String image, int quantity) {
        this.productCartId=productCartId;
        this.productId = productId;
        this.name = name;
        this.color = color;
        this.size = size;
        this.price = price;
        this.image = image;
        this.quantity = quantity;
    }
}

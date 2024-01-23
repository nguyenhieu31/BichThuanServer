package com.shopproject.shopbt.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductPayment {
    private Long productCartId;
    private Long productId;
    private String name;
    private String color;
    private String size;
    private BigDecimal price;
    private String image;
    private int quantity;
}

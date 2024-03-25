package com.shopproject.shopbt.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductCartRequest {
    private Long productId;
    private String name;
    private String color;
    private String size;
    private BigDecimal price;
    private String image;
    private int quantity;
}

package com.shopproject.shopbt.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Set;

@Data
public class ProductsDTO {
    private Long productId;
    private LocalDateTime createdAt;
    private String createdBy;
    private String description;
    private String image;
    private String material;
    private String name;
    private BigDecimal price;
    private int quantity;
    private int clickCount;
    private LocalDateTime updatedAt;
    private String updatedBy;
    private Long categoryId;
    private Set<Integer> colorId;
    private Set<Integer> sizeId;
}

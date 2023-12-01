package com.shopproject.shopbt.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderItemsDTO {
    private Long orderItemId;
    private BigDecimal pricePerUnit;
    private int quantity;
    private Long orderId;
    private Long productId;
}
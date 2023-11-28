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
    private String size;
    private String color;

    public OrderItemsDTO(BigDecimal pricePerUnit, int quantity, Long orderId, Long productId, String size, String color) {
        this.pricePerUnit = pricePerUnit;
        this.quantity = quantity;
        this.orderId = orderId;
        this.productId = productId;
        this.size = size;
        this.color = color;
    }

    public OrderItemsDTO() {
    }
}

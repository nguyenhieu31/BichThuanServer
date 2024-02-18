package com.shopproject.shopbt.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponse {
    private Long orderId;
    private String orderCode;
    private int status;
    private Long userId;
    private String fullName;
    private String address;
    private String productImage;
    private String productName;
    private BigDecimal priceUnit;
    private int quantity;
    private String size;
    private String color;
    private LocalDateTime createdAt;
}

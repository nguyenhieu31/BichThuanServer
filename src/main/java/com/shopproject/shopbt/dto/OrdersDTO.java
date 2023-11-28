package com.shopproject.shopbt.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class OrdersDTO {
    private Long orderId;
    private LocalDateTime createAt;
    private int status;
    private LocalDateTime updateAt;
    private Long userId;
    private String fullName;
    private String address;
    private String productImage;
    private String productName;
    private BigDecimal priceUnit;
    private int quantity;
    private String size;
    private String color;

    public OrdersDTO() {

    }

    // List đơn hàng mới nhất
    public OrdersDTO(Long orderId, LocalDateTime createAt, int status, String fullName) {
        this.orderId  = orderId;
        this.createAt = createAt;
        this.status   = status;
        this.fullName  = fullName;
    }

    // List đơn hàng today o.order_id, u.full_name, p.image, p.name, o.address, o.status

    public OrdersDTO(Long orderId, String fullName, String address, String productImage, String productName, int status) {
        this.orderId = orderId;
        this.status = status;
        this.fullName = fullName;
        this.address = address;
        this.productImage = productImage;
        this.productName = productName;
    }

    public OrdersDTO(Long orderId, int status, String fullName, String address, String productImage,
                     String productName, BigDecimal priceUnit, int quantity, String size, String color) {
        this.orderId = orderId;
        this.status = status;
        this.fullName = fullName;
        this.address = address;
        this.productImage = productImage;
        this.productName = productName;
        this.priceUnit = priceUnit;
        this.quantity = quantity;
        this.size     = size;
        this.color    = color;
    }
}

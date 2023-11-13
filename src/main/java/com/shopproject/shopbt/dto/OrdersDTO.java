package com.shopproject.shopbt.dto;

import lombok.Data;

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
}

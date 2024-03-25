package com.shopproject.shopbt.dto;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
@Data
public class OrdersDTO {
    private Long orderId;
    private UUID orderCode;
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
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String phone;
    private String personNote;
    public OrdersDTO() {

    }

    // List đơn hàng mới nhất
    public OrdersDTO(Long orderId, LocalDateTime createAt, int status, String fullName) {
        this.orderId  = orderId;
        this.createdAt = createAt;
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
                     String productName, BigDecimal priceUnit, int quantity, String size, String color, UUID orderCode,LocalDateTime createdAt) {
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
        this.orderCode=orderCode;
        this.createdAt=createdAt;
    }
    public OrdersDTO(int status) {
        this.status = status;
    }
}

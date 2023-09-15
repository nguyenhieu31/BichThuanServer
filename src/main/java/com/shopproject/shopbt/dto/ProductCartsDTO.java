package com.shopproject.shopbt.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ProductCartsDTO {
    private Long productCartId;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;
    private Long cartId;
    private Long productId;
    private String color;
    private int quantity;
    private String size;
    private int status;

}

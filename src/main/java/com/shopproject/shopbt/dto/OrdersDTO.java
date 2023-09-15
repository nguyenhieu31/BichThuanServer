package com.shopproject.shopbt.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OrdersDTO {
    private Long oderId;
    private LocalDateTime createAt;
    private int status;
    private LocalDateTime updateAt;
    private Long userId;
}

package com.shopproject.shopbt.dto;

import lombok.Data;

import java.time.LocalDateTime;
@Data
public class AddressDTO {
    private Long id;
    private String address;
    private Long userId;
}

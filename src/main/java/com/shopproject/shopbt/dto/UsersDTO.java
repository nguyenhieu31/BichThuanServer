package com.shopproject.shopbt.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UsersDTO {
    private Long userid;
    private String address;
    private LocalDateTime createAt;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private LocalDateTime updateAt;
}

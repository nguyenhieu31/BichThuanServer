package com.shopproject.shopbt.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UsersDTO {
    private Long userid;
    private LocalDateTime createAt;
    private String fullName;
    private String userName;
    private String password;
    private String phoneNumber;
    private String role;
    private LocalDateTime updateAt;
}

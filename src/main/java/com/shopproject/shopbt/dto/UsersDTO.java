package com.shopproject.shopbt.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

@Data
public class UsersDTO {
    private Long userid;
    private LocalDateTime createAt;
    private String fullName;
    private String userName;
    private String password;
    private String phoneNumber;
    private String role;
    private Set<Long> cartIds;
    private Set<Long> addressIds;
    private Set<Long> orderIds;
    private Set<Long> commentIds;
    private LocalDateTime updateAt;
}

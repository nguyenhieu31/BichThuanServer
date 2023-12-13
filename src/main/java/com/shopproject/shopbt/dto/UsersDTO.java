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
    private Long cartId;
    private Set<Long> addressIds;
    private Set<Long> orderIds;
    private Set<Long> commentIds;
    private LocalDateTime updateAt;
    private Long orderid;
    private String email;
    public UsersDTO(){}
    public UsersDTO(Long userid, Long orderid) {
        this.userid = userid;
        this.orderid = orderid;
    }

    public UsersDTO(Long userid, String fullName, String userName, String phoneNumber, String email, String role) {
        this.userid = userid;
        this.fullName = fullName;
        this.userName = userName;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.role  = role;
    }

    public UsersDTO(Long userid) {
        this.userid = userid;
    }
}

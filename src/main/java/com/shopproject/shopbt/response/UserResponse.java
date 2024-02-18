package com.shopproject.shopbt.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
public class UserResponse {
    private Long userId;
    private String userName;
    private String role;
    private String phoneNumber;
    private String fullName;
    private String email;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean status;
    private String updatedBy;

    public UserResponse(Long userId, String userName, String role, String phoneNumber, String fullName, String email, LocalDateTime createdAt,LocalDateTime updatedAt, boolean status, String updatedBy) {
        this.userId = userId;
        this.userName = userName;
        this.role = role;
        this.phoneNumber = phoneNumber;
        this.fullName = fullName;
        this.email = email;
        this.createdAt=createdAt;
        this.updatedAt=updatedAt;
        this.status = status;
        this.updatedBy=updatedBy;
    }
}

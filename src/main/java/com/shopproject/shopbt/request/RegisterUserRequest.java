package com.shopproject.shopbt.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterUserRequest {
    private String userName;
    private String fullName;
    private String password;
    private String phoneNumber;
    private String address;
    private String otp;
}

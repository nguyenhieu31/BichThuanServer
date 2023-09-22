package com.shopproject.shopbt.request;


import com.shopproject.shopbt.entity.Address;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

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
}

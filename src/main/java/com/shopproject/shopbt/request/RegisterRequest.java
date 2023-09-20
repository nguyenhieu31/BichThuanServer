package com.shopproject.shopbt.request;

import com.shopproject.shopbt.entity.Roles;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    private Long id;
    private String email;
    private String password;
    private List<Roles> roles;
}

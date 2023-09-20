package com.shopproject.shopbt.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ManagerResponse {
    private String token;
    private String refreshToken;
}

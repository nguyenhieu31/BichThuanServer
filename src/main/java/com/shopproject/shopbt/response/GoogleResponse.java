package com.shopproject.shopbt.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GoogleResponse {
    private String accessToken;
    private String userId;
    private String fullName;
    private Long expiresIn;
}

package com.shopproject.shopbt.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FacebookRequest {
    private String accessToken;
    private String signedRequest;
    private Long expiresIn;
    private String name;
    private String userId;
}

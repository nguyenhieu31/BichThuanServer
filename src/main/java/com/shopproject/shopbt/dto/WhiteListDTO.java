package com.shopproject.shopbt.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WhiteListDTO {
    private Long id;
    private String token;
    private LocalDateTime expirationToken;
}

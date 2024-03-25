package com.shopproject.shopbt.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
public class FreeShippingResponse {
    private Long freeShippingCodeId;
    private String code;
    private BigDecimal discountPercent;
    private String discountUnit;
    private LocalDateTime expires;
    private boolean isUsed;

    public FreeShippingResponse(Long freeShippingCodeId, String code, BigDecimal discountPercent, String discountUnit, LocalDateTime expires, boolean isUsed) {
        this.freeShippingCodeId = freeShippingCodeId;
        this.code = code;
        this.discountPercent = discountPercent;
        this.discountUnit = discountUnit;
        this.expires = expires;
        this.isUsed = isUsed;
    }
}

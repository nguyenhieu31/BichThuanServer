package com.shopproject.shopbt.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.shopproject.shopbt.dto.ProductPayment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateOrderRequest {
    private String address;
    private int statusOrder;
    @JsonProperty("isOrdered")
    private boolean isOrdered;
    private String phonePersonOrder;
    private String pricePersonPay;
    private String personNote;
    private Set<ProductPayment> productsPayment;
}

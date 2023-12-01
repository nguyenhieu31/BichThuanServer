package com.shopproject.shopbt.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddressRequest {
    private String address;
    private String wardName;
    private String districtName;
    private String provinceName;
    private String namePayment;
    private String phonePayment;
    private Boolean status;
}

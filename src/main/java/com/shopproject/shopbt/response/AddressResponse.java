package com.shopproject.shopbt.response;

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
public class AddressResponse {
    private String fullName;
    private String phoneNumber;
    private Set<Address> addresses;
}

package com.shopproject.shopbt.request;

import com.shopproject.shopbt.dto.AddressDTO;
import com.shopproject.shopbt.dto.ProductsDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateOrderRequest {
    private AddressDTO addressDTO;
    private Set<ProductsDTO> productsDTOs;
}

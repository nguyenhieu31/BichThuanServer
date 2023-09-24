package com.shopproject.shopbt.response;

import com.shopproject.shopbt.dto.ProductCartsDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Product_Carts {
    private Set<ProductCartsDTO> products_cart;
}

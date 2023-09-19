package com.shopproject.shopbt.respon;

import com.shopproject.shopbt.dto.ProductsDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Product_findbyid {
    private ProductsDTO productsDTO;
    private Set<ProductsDTO> products_same;
}

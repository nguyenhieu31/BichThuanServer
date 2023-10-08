package com.shopproject.shopbt.response;

import com.shopproject.shopbt.dto.ProductsDTO;
import com.shopproject.shopbt.entity.Gallery_Image;
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
    private ProductsDTO product;
    private Set<ProductsDTO> productSame;
}

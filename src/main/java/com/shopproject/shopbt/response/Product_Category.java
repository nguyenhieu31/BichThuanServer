package com.shopproject.shopbt.response;

import com.shopproject.shopbt.dto.ProductsDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Product_Category {
    private Page<ProductsDTO> products_category;
}

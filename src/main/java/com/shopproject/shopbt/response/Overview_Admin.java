package com.shopproject.shopbt.response;

import com.shopproject.shopbt.dto.CategoriesDTO;
import com.shopproject.shopbt.dto.OrdersDTO;
import com.shopproject.shopbt.dto.ProductsDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Overview_Admin {
    private Set<CategoriesDTO> categoriesDTOS;
    private Set<ProductsDTO> productsDTOS;
    private List<OrdersDTO> ordersDTOS;
}

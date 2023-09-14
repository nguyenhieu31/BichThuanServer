package com.shopproject.shopbt.service.product;

import com.shopproject.shopbt.dto.ProductsDTO;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

public interface ProductService {
    void create_Product(ProductsDTO productsDTO);

    ProductsDTO findProductById(Long id);

    void update_Product(ProductsDTO productsDTO);

    void delete_Product(Long id);

    Set<ProductsDTO> findProductsByCategoryId(Long id);

    Set<ProductsDTO> findProductsByPriceBetween(BigDecimal startPrice,BigDecimal endPrice);
}

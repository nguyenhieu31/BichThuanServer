package com.shopproject.shopbt.repository.product;

import com.shopproject.shopbt.entity.Categories;
import com.shopproject.shopbt.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Set<Product> findProductsByCategory_CategoryId(Long id);

    Set<Product> findProductsByPriceBetween(BigDecimal startPrice,BigDecimal endPrice);

}

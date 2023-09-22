package com.shopproject.shopbt.repository.product;

import com.shopproject.shopbt.entity.Categories;
import com.shopproject.shopbt.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Set<Product> findTop10ByPriceBetween(BigDecimal start, BigDecimal end);

    Set<Product> findTop10ByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);

    Set<Product> findByNameLikeIgnoreCase(String name);

    Page<Product> findProductsByCategory_CategoryId(Long id, Pageable pageable);

}

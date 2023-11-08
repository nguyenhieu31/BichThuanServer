package com.shopproject.shopbt.repository.product;

import com.shopproject.shopbt.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Set<Product> findProductsByCategory_CategoryId(Long id);
    Set<Product> findTop10ByPriceBetween(BigDecimal start, BigDecimal end);

    Set<Product> findTop10ByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);

    @Query("SELECT p.productId, p.image, p.name, p.price FROM Product p WHERE p.category.categoryId = :cateId")
    Set<Object[]> findProductByCateId(@Param("cateId") Long cateId);

    @Query("SELECT p.productId, p.image, p.name, p.price FROM Product p")
    Page<Object[]> findDataByLimitOffset(Pageable pageable);

    @Query("SELECT  p.productId, p.image, p.name, p.price FROM Product p WHERE p.price BETWEEN :start AND :end ORDER BY p.price ASC")
    Set<Object[]> findByPriceBetweenPrice(@Param("start") BigDecimal start, @Param("end") BigDecimal end);

    @Query("SELECT  p.productId, p.image, p.name, p.price FROM Product p")
    Set<Object[]> findProductFeature();

    @Query("SELECT p FROM Product p WHERE p.productId = :proId")
    Product findByProductId(@Param("proId") Long proId);

    @Query("SELECT p.productId, p.image, p.name, p.price FROM Product p WHERE lower(p.name) LIKE lower(concat('%', :name, '%'))")
    Set<Object[]> findProductByNameLike(@Param("name") String name);
}

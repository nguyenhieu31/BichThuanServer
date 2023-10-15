package com.shopproject.shopbt.repository.product_cart;

import com.shopproject.shopbt.entity.Product_Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.Set;

public interface Product_CartRepository extends JpaRepository<Product_Cart, Long> {
    Set<Product_Cart> findByCart_CartId(Long id);
    Optional<Product_Cart> findByProduct_ProductIdAndSize(Long id, String size);
}

package com.shopproject.shopbt.repository.carts;

import com.shopproject.shopbt.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Cart findByUser_Userid(Long id);
}

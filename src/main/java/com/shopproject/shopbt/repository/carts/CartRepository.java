package com.shopproject.shopbt.repository.carts;

import com.shopproject.shopbt.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CartRepository extends JpaRepository<Cart, Long> {
    @Query("select c from Cart c where c.user.userid=:userId")
    Cart findByUser_Userid(@Param("userId") Long userId);
}

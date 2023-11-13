package com.shopproject.shopbt.repository.orderitem;

import com.shopproject.shopbt.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Set;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    @Query("SELECT o.pricePerUnit, o.quantity, o.product.productId, o.order.oderId FROM OrderItem o WHERE o.order.oderId = :orderId")
    Set<Object[]> findALlByOrderId(@Param("orderId") Long orderId);
}

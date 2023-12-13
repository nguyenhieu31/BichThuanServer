package com.shopproject.shopbt.repository.orderitem;

import com.shopproject.shopbt.entity.Order;
import com.shopproject.shopbt.entity.OrderItem;
import com.shopproject.shopbt.response.Product_Detail_Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Set;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    @Query("SELECT new com.shopproject.shopbt.response.Product_Detail_Order(o.order.orderId, o.product.productId,o.product.name,o.pricePerUnit,or.pricePersonPay,o.color,o.size,o.product.image,o.quantity)" +
            " FROM OrderItem o " +
            "join Order or on o.order.orderId=or.orderId" +
            " WHERE o.order.orderId = :orderId")
    Set<Product_Detail_Order> findALlByOrderId(@Param("orderId") Long orderId);
    @Query("select oi from OrderItem oi where oi.order.orderId=:orderId")
    Set<OrderItem> findByOrderId(@Param("orderId") Long orderId);
}

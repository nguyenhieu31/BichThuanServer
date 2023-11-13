package com.shopproject.shopbt.repository.order;

import com.shopproject.shopbt.dto.OrdersDTO;
import com.shopproject.shopbt.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

public interface OrderRepository extends JpaRepository<Order, Long>{
    @Query("SELECT new com.shopproject.shopbt.dto.OrdersDTO(o.oderId, o.createdAt, o.status, o.user.userName) FROM Order o")
    Page<OrdersDTO> findByOrderDate(Pageable pageable);

    @Query("select o.oderId, o.status, o.createdAt from Order o where o.user.userid = :orderId")
    Set<Object[]> findOrdersByUser_Userid(@Param("orderId") Long orderId);

    @Query("SELECT new com.shopproject.shopbt.dto.OrdersDTO(o.oderId, o.createdAt, o.status, o.user.fullName) FROM Order o")
    Page<OrdersDTO> findLatestOrders(Pageable pageable);

    @Query("""
    SELECT new com.shopproject.shopbt.dto.OrdersDTO(o.oderId, u.fullName, o.address, p.image, p.name, o.status)
    FROM Order o
    JOIN OrderItem ot ON o.oderId = ot.order.oderId
    JOIN Product p ON ot.product.productId = p.productId
    JOIN User u ON o.user.userid = u.userid
    WHERE date(o.createdAt) = current_date""")
    Set<OrdersDTO> findALLByOrderToday();

    Optional<Order> findOrderByOrderId(@Param("id") Long id);

    @Query("""
            SELECT new com.shopproject.shopbt.dto.OrdersDTO(o.orderId, u.fullName, o.address, p.image, p.name, o.status)
            FROM Order o
            JOIN OrderItem ot ON o.orderId = ot.order.orderId
            JOIN Product p ON ot.product.productId = p.productId
            JOIN User u ON o.user.userid = u.userid
            WHERE date(o.createdAt) BETWEEN CURRENT_DATE - 7 AND CURRENT_DATE""")
    Set<OrdersDTO> findAllOrderBy7Day();
}

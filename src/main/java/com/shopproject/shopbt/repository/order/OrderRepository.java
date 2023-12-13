package com.shopproject.shopbt.repository.order;
import com.shopproject.shopbt.dto.OrdersDTO;
import com.shopproject.shopbt.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface OrderRepository extends JpaRepository<Order, Long>{
    @Query("SELECT new com.shopproject.shopbt.dto.OrdersDTO(o.orderId, o.createdAt, o.status, o.user.userName) FROM Order o")
    Page<OrdersDTO> findByOrderDate(Pageable pageable);

    @Query("select o from Order o where o.user.userid = :orderId")
    List<Order> findOrdersByUser_Userid(@Param("orderId") Long orderId);

    @Query("SELECT new com.shopproject.shopbt.dto.OrdersDTO(o.orderId, o.createdAt, o.status, o.user.fullName) FROM Order o")
    Page<OrdersDTO> findLatestOrders(Pageable pageable);

    @Query("""
    SELECT new com.shopproject.shopbt.dto.OrdersDTO(o.orderId, o.status, u.fullName, o.address, p.image, p.name, ot.pricePerUnit, ot.quantity, ot.size, ot.color)
    FROM Order o
    JOIN OrderItem ot ON o.orderId = ot.order.orderId
    JOIN Product p ON ot.product.productId = p.productId
    JOIN User u ON o.user.userid = u.userid
    WHERE date(o.createdAt) = current_date""")
    Set<OrdersDTO> findALLByOrderToday();

    Optional<Order> findOrderByOrderId(@Param("id") Long id);

    @Query("""
            SELECT new com.shopproject.shopbt.dto.OrdersDTO(o.orderId, o.status, u.fullName, o.address, p.image, p.name, ot.pricePerUnit, ot.quantity, ot.size, ot.color)
            FROM Order o
            JOIN OrderItem ot ON o.orderId = ot.order.orderId
            JOIN Product p ON ot.product.productId = p.productId
            JOIN User u ON o.user.userid = u.userid
            WHERE date(o.createdAt) BETWEEN CURRENT_DATE - 7 AND CURRENT_DATE""")
    Set<OrdersDTO> findAllOrderBy7Day();

    @Transactional
    @Modifying
    @Query("DELETE FROM Order o WHERE o.orderId = :id")
    void deleteOrderAndOrderItemsByOrderId(@Param("id") Long id);

    @Query("select new com.shopproject.shopbt.dto.OrdersDTO(o.status) from Order o where o.orderId = :id")
    OrdersDTO findStatusByOrderId(@Param("id") Long id);
    @Query("select o from Order o where o.status=:status and o.user.userid=:userId")
    Set<Order> findByStatusAndUserId(@Param("status") int status, @Param("userId") Long userId);
}

package com.shopproject.shopbt.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;
    private UUID orderCode;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "order")
    private Set<OrderItem> order_items = new HashSet<>();
    @Column(nullable = false)
    private int status;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    @Column(name = "address",nullable = false, length = 100)
    private String address;
    @Column(name = "is_ordered", nullable = false)
    private boolean isOrdered;
    @Column(name = "phone_person_order", nullable = false, length = 11)
    private String phonePersonOrder;
    @Column(name = "price_person_pay", nullable = true)
    private BigDecimal pricePersonPay;
    @Column(name = "reason_cancel_order", nullable = true,length = 100)
    private String reasonCancelOrder;
    @Column(name = "person_note", nullable = true, length = 255)
    private String personNote;
    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime createdAt;
    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;
    @Column(nullable = true)
    private LocalDateTime deletedAt;
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}

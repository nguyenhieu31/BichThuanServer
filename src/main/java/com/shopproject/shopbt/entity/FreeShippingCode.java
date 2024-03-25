package com.shopproject.shopbt.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "free_shipping_code")
public class FreeShippingCode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long freeShippingCodeId;
    @Column(name = "code", nullable = false,length = 10, unique = true)
    private String code;
    @Column(name = "expires", nullable = false)
    private LocalDateTime expires;
    @Column(name = "discount_unit", nullable = false, length = 25)
    private String discountUnit;
    @Column(name= "discount_percent",nullable = false)
    private BigDecimal discountPercent;
    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    @OneToMany(mappedBy = "freeShippingCode", fetch = FetchType.LAZY)
    private Set<FreeShippingMember> freeShippingMembers;
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

}

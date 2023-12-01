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
@Table(name = "voucher")
public class Voucher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long voucherId;
    @Column(name = "code", nullable = false, unique = true,length = 25)
    private String code;
    @Column(name = "discount_percent", nullable = false)
    private BigDecimal discountPercent;
    @Column(name = "expires", nullable = false)
    private LocalDateTime expires;
    @Column(name = "is_used", columnDefinition = "boolean DEFAULT false")
    private boolean isUsed;
    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    @OneToMany(mappedBy = "voucher", fetch = FetchType.LAZY)
    private Set<User_Voucher> userVouchers;
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}

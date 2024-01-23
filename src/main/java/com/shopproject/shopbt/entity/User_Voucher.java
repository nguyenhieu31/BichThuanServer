package com.shopproject.shopbt.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user_voucher")
public class User_Voucher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userVoucherId;
    @Column(name="assigned_at", nullable = false)
    private LocalDateTime assigned_at;
    @ManyToOne(fetch = FetchType.EAGER )
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "voucher_id", nullable = false)
    private Voucher voucher;
}

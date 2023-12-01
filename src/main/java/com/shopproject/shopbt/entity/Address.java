package com.shopproject.shopbt.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "address")
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "address", nullable = true, length = 100)
    private String address;
    @Column(name = "status", nullable = true)
    private int status;
    @Column(name = "province", nullable = true)
    private String province;
    @Column(name = "district", nullable = true)
    private String district;
    @Column(name="wards", nullable = true)
    private String wards;
    @Column(name = "name_payment", nullable = true)
    private String namePayment;
    @Column(name = "phone_payment", nullable = true, unique = true, length = 12)
    private String phonePayment;
    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER, cascade = { CascadeType.MERGE, CascadeType.REMOVE,CascadeType.REFRESH, CascadeType.DETACH })
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}


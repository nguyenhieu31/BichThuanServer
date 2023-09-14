package com.shopproject.shopbt.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "product_carts")
public class Product_Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long product_cart_id;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "cart_id", nullable = false)
    private Cart cart;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;
}

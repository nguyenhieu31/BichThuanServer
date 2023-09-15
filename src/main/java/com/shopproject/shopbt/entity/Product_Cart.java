package com.shopproject.shopbt.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

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
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;
    @Column(nullable = false, length = 10)
    private String size;
    @Column(nullable = false, length = 10)
    private String color;
    @Column(nullable = false)
    private int quantity;
    @Column(nullable = false)
    private int status;
    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime createAt;
    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updateAt;
}

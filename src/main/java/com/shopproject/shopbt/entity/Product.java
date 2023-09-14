package com.shopproject.shopbt.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long productId;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id", nullable = false)
    private Categories category;
    @Column(name = "description", nullable = false)
    private String description;
    @Column(name = "name", nullable = false, length = 50)
    private String name;
    @Column(name = "price", nullable = false, precision = 12, scale = 0)
    private BigDecimal price;
    @Column(name = "quantity", nullable = false)
    private int quantity;
    @Column(name = "image")
    private byte[] image;
    @Column(name = "material", nullable = false)
    private String material;
    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime createdAt;
    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;
    @Column(nullable = false, length = 25)
    private String createdBy;
    @Column(nullable = false, length = 25)
    private String updatedBy;
}

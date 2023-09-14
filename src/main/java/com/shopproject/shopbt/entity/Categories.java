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
@Table(name = "Categories")
public class Categories {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long categoryId;
    @Column(name = "name", nullable = false, length = 50)
    private String name;
    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime createAt;
    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updateAt;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "category")
    private Set<Product> products = new HashSet<Product>(0);
}

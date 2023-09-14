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
@Table(name = "Users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userid;
    @Column(name = "first_name",nullable = false,length = 50)
    private String firstName;
    @Column(name = "last_name", nullable = false, length = 25)
    private String lastName;
    @Column(name = "phone_number", nullable = false, length = 11)
    private String phoneNumber;
    @Column(name = "address", nullable = false, length = 100)
    private String address;
    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime createAt;
    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updateAt;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    private Set<Order> orders = new HashSet<Order>(0);
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    private Set<Cart> carts = new HashSet<Cart>(0);
}

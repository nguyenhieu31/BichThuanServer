package com.shopproject.shopbt.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userid;
    @Column(name = "user_name",nullable = false,unique = true,length = 50)
    private String userName;
    @Column(name = "full_name", nullable = false, length = 50)
    private String fullName;
    @Column(name="password", nullable = false, length = 150)
    private String password;
    @Column(name = "phone_number", nullable = false,unique = true, length = 11)
    private String phoneNumber;
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
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    private Set<Comment> comments = new HashSet<Comment>(0);
    @OneToMany(fetch = FetchType.LAZY,mappedBy = "user", cascade = {CascadeType.ALL})
    private Set<Address> addresses= new HashSet<Address>();
}

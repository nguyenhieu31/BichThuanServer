package com.shopproject.shopbt.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Users")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userid;
    @Column(name = "user_name",nullable = false,unique = true,length = 50)
    private String userName;
    @Column(name = "full_name", nullable = false, length = 50)
    private String fullName;
    @Column(name="password", nullable = false, length = 150)
    private String password;
    @Column(name = "email",unique = true)
    private String email; // for OAuth2
    @Column(name = "phone_number", nullable = false,unique = true, length = 11)
    private String phoneNumber;
    @Column(name = "role", nullable = false, length = 10)
    private String role;
    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime createdAt;
    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
    @OneToMany(fetch = FetchType.LAZY,mappedBy = "user", cascade = {CascadeType.ALL})
    private Set<Address> addresses= new HashSet<Address>();
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    private Set<Order> orders = new HashSet<Order>(0);
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "user")
    private Cart cart;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    private Set<Comment> comments = new HashSet<Comment>(0);

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<? extends GrantedAuthority> authorities= List.of(new SimpleGrantedAuthority("ROLE_"+role));
        return authorities;
    }

    @Override
    public String getUsername() {
        return userName;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
    @Override
    public String getPassword(){
        return password;
    }
}

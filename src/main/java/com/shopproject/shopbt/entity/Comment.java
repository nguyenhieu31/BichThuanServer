package com.shopproject.shopbt.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long commentId;
    @Column(nullable = false, length = 100)
    private String description;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    @Column(nullable = false)
    private Integer rating;
    @Column(nullable = false, length = 10)
    private String size;
    @Column(nullable = false, length = 10)
    private String color;
    @Column(nullable = false, length = 25)
    private String productName;
    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime createAt;
    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updateAt;
}

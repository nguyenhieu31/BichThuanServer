package com.shopproject.shopbt.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Transactional
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
    @Column(name = "price_discount",precision = 12,scale = 0)
    private BigDecimal priceDiscount;
    @Column(name = "quantity", nullable = false)
    private int quantity;
    @Column(name = "image", columnDefinition = "text")
    private String image;
    @Column(name = "material", nullable = false)
    private String material;
    @Column(name = "clickCount")
    private Integer clickCount;
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
    @Column(nullable = false, length = 25)
    private String createdBy;
    @Column(nullable = false, length = 25)
    private String updatedBy;
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "product")
    private Set<Product_Cart> product_carts = new HashSet<Product_Cart>(0);
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "product_size",
            joinColumns = {@JoinColumn(name = "product_id", referencedColumnName = "productId")},
            inverseJoinColumns = {@JoinColumn(name = "size_id", referencedColumnName = "sizeId")}
    )
    private Set<Size> sizes = new HashSet<Size>(0);
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "product_color",
            joinColumns = {@JoinColumn(name = "product_id", referencedColumnName = "productId")},
            inverseJoinColumns = {@JoinColumn(name = "color_id", referencedColumnName = "colorId")}
    )
    private Set<Color> colors = new HashSet<Color>(0);
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "product")
    private Set<Gallery_Image> gallery_images = new HashSet<Gallery_Image>(0);
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "product")
    private List<Comment> comments = new ArrayList<>();
}

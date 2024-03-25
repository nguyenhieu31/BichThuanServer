package com.shopproject.shopbt.dto;

import lombok.Data;
import com.shopproject.shopbt.entity.Color;
import com.shopproject.shopbt.entity.Gallery_Image;
import com.shopproject.shopbt.entity.Size;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;
import java.util.Set;

@Data
public class ProductsDTO {
    private Long productId;
    private LocalDateTime createdAt;
    private String createdBy;
    private String description;
    private String image;
    private String material;
    private String name;
    private BigDecimal price;
    private BigDecimal priceDiscount;
    private int quantity;
    private int clickCount;
    private LocalDateTime updatedAt;
    private String updatedBy;
    private Long categoryId;
    private Set<Color> colorNames;
    private List<String> sizeNames;
    private Set<Gallery_Image> galleryImages;
    private Set<Long> colorIds;
    private Set<Long> sizeIds;
    private String color;
    private String size;
    public ProductsDTO() {
    }

    public ProductsDTO(Long productId, BigDecimal price, int quantity, String color, String size) {
        this.productId = productId;
        this.price = price;
        this.quantity = quantity;
        this.color = color;
        this.size = size;
    }
    public ProductsDTO(Long productId, String description, String image, String name, BigDecimal price, BigDecimal priceDiscount, int quantity, Long categoryId) {
        this.productId = productId;
        this.description = description;
        this.image = image;
        this.name = name;
        this.price = price;
        this.priceDiscount = priceDiscount;
        this.quantity = quantity;
        this.categoryId = categoryId;
    }
}

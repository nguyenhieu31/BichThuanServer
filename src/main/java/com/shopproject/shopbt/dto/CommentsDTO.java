package com.shopproject.shopbt.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommentsDTO {
    private Long commentId;
    private Long userId;
    private Long productId;
    private String color;
    private String descriptionProductQuality;
    private String descriptionFeature;
    private int rating;
    private String size;
    private String userName;
    private boolean isActive;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;
    private String description;
    private String productName;
}

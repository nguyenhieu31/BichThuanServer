package com.shopproject.shopbt.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommentsDTO {
    private Long commentId;
    private String color;
    private LocalDateTime createAt;
    private String description;
    private String productName;
    private int rating;
    private String size;
    private LocalDateTime updateAt;
    private Long userId;
}

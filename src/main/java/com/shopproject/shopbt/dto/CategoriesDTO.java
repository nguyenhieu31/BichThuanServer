package com.shopproject.shopbt.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CategoriesDTO {
    private Long categoryId;
    private LocalDateTime createAt;
    private String name;
    private LocalDateTime updateAt;
}

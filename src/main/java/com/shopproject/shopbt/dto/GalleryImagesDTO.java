package com.shopproject.shopbt.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

@Data
public class GalleryImagesDTO {
    private Long id;
    private LocalDateTime createdAt;
    private Set<String> images;
    private LocalDateTime updatedAt;
    private Long productId;
}

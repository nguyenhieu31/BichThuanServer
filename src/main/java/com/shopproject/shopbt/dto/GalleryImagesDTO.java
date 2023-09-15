package com.shopproject.shopbt.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class GalleryImagesDTO {
    private Long id;
    private LocalDateTime createdAt;
    private byte[] image;
    private LocalDateTime updatedAt;
    private Long productId;
}

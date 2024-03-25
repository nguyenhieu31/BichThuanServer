package com.shopproject.shopbt.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentResponse {
    private Long commentId;
    private Long productId;
    private String nameProduct;
    private String color;
    private String size;
    private Float rating;
    private String descriptionProductQuality;
    private String descriptionFeature;
    private String userName;
    private boolean isActive;
    private LocalDateTime createdAt;
}

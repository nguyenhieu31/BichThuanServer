package com.shopproject.shopbt.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentRequest {
    private Long productId;
    private float rating;
    private String descriptionProductQuality;
    private String descriptionFeature;
    private String color;
    private String size;
    @JsonProperty("isShowUserName")
    private boolean isShowUserName;
}

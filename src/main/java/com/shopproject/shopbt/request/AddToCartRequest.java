package com.shopproject.shopbt.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddToCartRequest {
    private Long id;
    private String color;
    private String size;
    private Integer quantity;
}

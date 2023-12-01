package com.shopproject.shopbt.response;

import com.shopproject.shopbt.dto.OrderItemsDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Product_MyOrder {
    private Set<OrderItemsDTO> product_w_confirm;
    private Set<OrderItemsDTO> product_w_delivery;
    private Set<OrderItemsDTO> product_delivering;
    private Set<OrderItemsDTO> product_delivered;
    private Set<OrderItemsDTO> product_return;
}

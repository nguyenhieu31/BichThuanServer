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
    private Set<Product_Detail_Order> product_w_confirm;
    private Set<Product_Detail_Order> product_w_delivery;
    private Set<Product_Detail_Order> product_delivering;
    private Set<Product_Detail_Order> product_delivered;
    private Set<Product_Detail_Order> product_cancel;
    private Set<Product_Detail_Order> product_return;
}

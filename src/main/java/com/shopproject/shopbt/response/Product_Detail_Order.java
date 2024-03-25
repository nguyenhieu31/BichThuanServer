package com.shopproject.shopbt.response;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
public class Product_Detail_Order {
    private Long orderId;
    private Long productId;
    private String nameProduct;
    private BigDecimal priceProduct;
    private BigDecimal moneyPersonPay;
    private String color;
    private String size;
    private String image;
    private int quantity;
    public Product_Detail_Order(Long orderId, Long productId, String name, BigDecimal price,BigDecimal moneyPersonPay, String color, String size, String image, int quantity){
        this.orderId=orderId;
        this.productId=productId;
        this.nameProduct=name;
        this.priceProduct=price;
        this.moneyPersonPay=moneyPersonPay;
        this.color=color;
        this.size=size;
        this.image=image;
        this.quantity=quantity;
    }

}

package com.shopproject.shopbt.service.product_cart;

import com.shopproject.shopbt.dto.ProductCartsDTO;

public interface Product_CartService {
    void create_Product_Cart(ProductCartsDTO productCartsDTO);

    ProductCartsDTO findProduct_CartById(Long id);

    void update_Product_Cart(ProductCartsDTO productCartsDTO);

    void delete_Product_CartById(Long id);
}

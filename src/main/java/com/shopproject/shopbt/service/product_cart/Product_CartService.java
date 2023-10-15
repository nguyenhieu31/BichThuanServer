package com.shopproject.shopbt.service.product_cart;

import com.shopproject.shopbt.dto.ProductCartsDTO;
import com.shopproject.shopbt.request.AddToCartRequest;

import java.util.Set;

public interface Product_CartService {
    String create_Product_Cart(Long id, AddToCartRequest request) throws Exception;

    ProductCartsDTO findProduct_CartById(Long id);

    void update_Product_Cart(ProductCartsDTO productCartsDTO);

    void delete_Product_CartById(Long id);

    Set<ProductCartsDTO> findProduct_CartByCartId(Long id);
}

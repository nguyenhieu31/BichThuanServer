package com.shopproject.shopbt.service.product_cart;

import com.shopproject.shopbt.ExceptionCustom.ProductException;
import com.shopproject.shopbt.dto.ProductCartsDTO;
import com.shopproject.shopbt.entity.Product_Cart;
import com.shopproject.shopbt.request.AddToCartRequest;
import com.shopproject.shopbt.request.ProductCartRequest;
import com.shopproject.shopbt.response.CartResponse;
import java.util.List;

public interface Product_CartService {
    String create_Product_Cart(Long id, AddToCartRequest request) throws Exception;
    List<CartResponse> getAllProductCartByUser() throws Exception;
    ProductCartsDTO findProduct_CartById(Long id) throws ProductException;
    CartResponse incrementProductCart(Long productCartId, ProductCartRequest request) throws Exception;
    CartResponse decrementProductCart(Long productCartId, ProductCartRequest request) throws Exception;
    Product_Cart updateStatusCart(Long productCartId,int status) throws Exception;
    void delete_Product_CartById(Long productCartId) throws Exception;
    List<CartResponse> updateCart(String userName) throws Exception;
}

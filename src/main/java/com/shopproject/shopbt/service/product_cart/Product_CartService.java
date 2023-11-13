package com.shopproject.shopbt.service.product_cart;

import com.shopproject.shopbt.ExceptionCustom.ProductException;
import com.shopproject.shopbt.dto.ProductCartsDTO;
import com.shopproject.shopbt.request.AddToCartRequest;
import com.shopproject.shopbt.request.CartRequest;
import com.shopproject.shopbt.request.ProductCartRequest;
import com.shopproject.shopbt.response.CartResponse;

import javax.security.auth.login.LoginException;
import java.util.List;
import java.util.Set;

public interface Product_CartService {
    String create_Product_Cart(Long id, AddToCartRequest request) throws Exception;
    List<CartResponse> getAllProductCartByUser() throws Exception;
    ProductCartsDTO findProduct_CartById(Long id) throws ProductException;
    CartResponse incrementProductCart(Long productCartId, ProductCartRequest request) throws Exception;
    CartResponse decrementProductCart(Long productCartId, ProductCartRequest request) throws Exception;
    void update_Product_Cart(ProductCartsDTO productCartsDTO);
    void delete_Product_CartById(Long productCartId);
    List<CartResponse> updateCart(String userName) throws Exception;

}

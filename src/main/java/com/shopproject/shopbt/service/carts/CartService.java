package com.shopproject.shopbt.service.carts;

import com.shopproject.shopbt.dto.CartsDTO;

public interface CartService {
    void create_Cart(CartsDTO cartsDTO);

    CartsDTO findCartById(Long id);

    void update_Cart(CartsDTO cartsDTO);

    void delete_CartById(Long id);

    CartsDTO findByUserId(Long id);
}

package com.shopproject.shopbt.carts;

import com.shopproject.shopbt.dto.CartsDTO;
import com.shopproject.shopbt.dto.UsersDTO;
import com.shopproject.shopbt.service.carts.CartService;
import com.shopproject.shopbt.service.user.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TestCart {
    @Autowired
    private CartService cartService;
    @Autowired
    private UserService userService;

    @Test
    void create(){
        CartsDTO cartsDTO = new CartsDTO();
        Long id = 3L;
        cartsDTO.setUserId(id);

        cartService.create_Cart(cartsDTO);
    }

    @Test
    void findById(){
        Long id = 1L;
        CartsDTO cartsDTO = cartService.findCartById(id);
        UsersDTO usersDTO = userService.findByUserId(cartsDTO.getUserId());

        System.out.println(usersDTO.getFirstName()+" "+usersDTO.getLastName());
        System.out.println(usersDTO.getPhoneNumber());
        System.out.println(usersDTO.getAddress());
    }
}

package com.shopproject.shopbt.address;

import com.shopproject.shopbt.dto.AddressDTO;
import com.shopproject.shopbt.service.address.AddressService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TestAddress {

    @Autowired
    private AddressService addressService;

    @Test
    void create(){
//        AddressDTO addressDTO = new AddressDTO();
//        addressDTO.setAddress("113 Ong Ich Khiem");
//        addressDTO.setUserId(16L);
//
//        addressService.create_Address(addressDTO);
    }
}

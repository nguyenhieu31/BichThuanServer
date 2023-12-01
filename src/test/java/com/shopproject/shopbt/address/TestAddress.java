package com.shopproject.shopbt.address;

import com.shopproject.shopbt.dto.AddressDTO;
import com.shopproject.shopbt.request.AddressRequest;
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
        AddressRequest addressRequest= new AddressRequest();
        addressRequest.setAddress("175 trần nhân tông");
        addressRequest.setProvinceName("Quảng Nam");
        addressRequest.setDistrictName("Điện Bàn");
        addressRequest.setWardName("Vĩnh Điện");
        String response= addressService.create_Address(addressRequest,null);
    }
}

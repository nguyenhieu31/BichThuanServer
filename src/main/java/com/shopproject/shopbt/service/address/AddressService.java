package com.shopproject.shopbt.service.address;

import com.shopproject.shopbt.dto.AddressDTO;

public interface AddressService {
    void create_Address(AddressDTO addressDTO);

    AddressDTO findAddressById(Long id);

    void update_Address(AddressDTO addressDTO);

    void delete_AddressById(Long id);
}

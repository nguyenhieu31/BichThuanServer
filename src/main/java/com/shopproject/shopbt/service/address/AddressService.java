package com.shopproject.shopbt.service.address;

import com.shopproject.shopbt.ExceptionCustom.AddressException;
import com.shopproject.shopbt.dto.AddressDTO;
import com.shopproject.shopbt.entity.Address;
import com.shopproject.shopbt.entity.User;
import com.shopproject.shopbt.request.AddressRequest;

public interface AddressService {
    Address create_Address(AddressRequest request, User user) throws AddressException;

    AddressDTO findAddressById(Long id);

    Address update_Address_default(AddressRequest request, User user);
    Address updateAddress(Long addressId, AddressRequest request, User user) throws AddressException;
    void update_Address(AddressDTO addressDTO);

    void delete_AddressById(Long id);
}

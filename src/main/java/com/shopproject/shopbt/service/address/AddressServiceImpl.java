package com.shopproject.shopbt.service.address;

import com.shopproject.shopbt.dto.AddressDTO;
import com.shopproject.shopbt.entity.Address;
import com.shopproject.shopbt.repository.Address.AddressRepo;
import com.shopproject.shopbt.repository.user.UserRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AddressServiceImpl implements AddressService{
    private AddressRepo addressRepo;
    private UserRepository userRepository;
    private ModelMapper modelMapper;


    @Override
    public void create_Address(AddressDTO addressDTO) {
        Address address = new Address();
        address.setAddress(addressDTO.getAddress());
        address.setUser(userRepository.findById(addressDTO.getUserId()).get());
        addressRepo.save(address);
    }

    @Override
    public AddressDTO findAddressById(Long id) {
        return modelMapper.map(addressRepo.findById(id).get(), AddressDTO.class);
    }

    @Override
    public void update_Address(AddressDTO addressDTO) {
        Address address = addressRepo.findById(addressDTO.getId()).get();
        address = modelMapper.map(addressDTO, Address.class);
        addressRepo.save(address);
    }

    @Override
    public void delete_AddressById(Long id) {
        addressRepo.deleteById(id);
    }
}

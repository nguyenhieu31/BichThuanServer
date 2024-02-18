package com.shopproject.shopbt.service.address;

import com.shopproject.shopbt.ExceptionCustom.AddressException;
import com.shopproject.shopbt.dto.AddressDTO;
import com.shopproject.shopbt.entity.Address;
import com.shopproject.shopbt.entity.User;
import com.shopproject.shopbt.repository.Address.AddressRepo;
import com.shopproject.shopbt.repository.user.UserRepository;
import com.shopproject.shopbt.request.AddressRequest;
import com.shopproject.shopbt.dto.AddressDTO;
import com.shopproject.shopbt.entity.Address;
import com.shopproject.shopbt.repository.Address.AddressRepo;
import com.shopproject.shopbt.repository.user.UserRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
@Service
@AllArgsConstructor
public class AddressServiceImpl implements AddressService{
    private AddressRepo addressRepo;
    private UserRepository userRepository;
    private ModelMapper modelMapper;
    private Address saveAddress(AddressRequest request, User user, boolean isDefault) {
        Address address = Address.builder()
                .user(user)
                .address(request.getAddress())
                .wards(request.getWardName())
                .district(request.getDistrictName())
                .province(request.getProvinceName())
                .namePayment(request.getNamePayment())
                .phonePayment(request.getPhonePayment())
                .status(isDefault?1:0)
                .build();
        return addressRepo.save(address);
    }
    @Override
    public Address create_Address(AddressRequest request, User user) throws AddressException {
        Optional<Address> isAddress= addressRepo.findAddressByAddressName(request.getAddress(),user.getUserid());
        if(isAddress.isPresent()
                && isAddress.get().getProvince().equals(request.getProvinceName())
                && isAddress.get().getDistrict().equals(request.getDistrictName())
                && isAddress.get().getWards().equals(request.getWardName())
        ){
            throw new AddressException("đã tồn tại địa chỉ này");
        }else{
            List<Address> userAddress= addressRepo.findByUser(user);
            Address newAddress= null;
            if(userAddress.size()==0 && !request.getStatus()){
                request.setStatus(true);
                newAddress= saveAddress(request,user,request.getStatus());
            }else{
                newAddress= saveAddress(request,user,request.getStatus());
                if(request.getStatus()){
                    for(Address address:userAddress){
                        if(!address.equals(newAddress)){
                            address.setStatus(0);
                            addressRepo.save(address);
                        }
                    }
                }
            }
            return newAddress;
        }
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
    public Address update_Address_default(AddressRequest request, User user) {
        Optional<Address> isAddress=addressRepo.findAddressByAddressName(request.getAddress(),user.getUserid());
        isAddress.ifPresent(address -> address.setStatus(1));
        if(isAddress.isPresent()){
            Address updateAddress= addressRepo.save(isAddress.get());
            List<Address> userAddress= addressRepo.findByUser(user);
            for(Address address: userAddress){
                if(!Objects.equals(address.getId(), updateAddress.getId())){
                    address.setStatus(0);
                    addressRepo.save(address);
                }
            }
            return updateAddress;
        }
        return null;
    }
    private Address mapData(Address address, AddressRequest request){
        address.setAddress(request.getAddress());
        address.setWards(request.getWardName());
        address.setProvince(request.getProvinceName());
        address.setDistrict(request.getDistrictName());
        address.setNamePayment(request.getNamePayment());
        address.setPhonePayment(request.getPhonePayment());
        address.setStatus(request.getStatus()?1:0);
        return address;
    }
    @Override
    public Address updateAddress(Long addressId, AddressRequest request, User user) throws AddressException {
        Optional<Address> isAddress= addressRepo.findAddressByAddressId(addressId);
        if(isAddress.isPresent()){
            Address addressMap= mapData(isAddress.get(),request);
            Address updateAddress= addressRepo.save(addressMap);
            if(request.getStatus()){
                List<Address> userAddress= addressRepo.findByUser(user);
                for(Address address:userAddress){
                    if(!address.getId().equals(updateAddress.getId())){
                        address.setStatus(0);
                        addressRepo.save(address);
                    }
                }
            }
            return updateAddress;
        }
        throw new AddressException("không tìm thấy địa chỉ");
    }

    @Override
    public void delete_AddressById(Long id) {
        addressRepo.deleteById(id);
    }
}

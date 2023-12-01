package com.shopproject.shopbt.controller;

import com.google.api.Http;
import com.shopproject.shopbt.ExceptionCustom.AddressException;
import com.shopproject.shopbt.ExceptionCustom.LoginException;
import com.shopproject.shopbt.ExceptionCustom.LogoutException;
import com.shopproject.shopbt.entity.Address;
import com.shopproject.shopbt.entity.User;
import com.shopproject.shopbt.request.AddressRequest;
import com.shopproject.shopbt.response.AddressResponse;
import com.shopproject.shopbt.service.address.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@CrossOrigin
@RequestMapping("/web/address")
public class AddressController {
    private final AddressService addressService;
    @GetMapping("/user")
    public ResponseEntity<?> findAddressByUser(){
        try{
            Authentication userAuth= SecurityContextHolder.getContext().getAuthentication();
            if(userAuth.isAuthenticated()){
                User user= (User) userAuth.getPrincipal();
                Set<Address> addresses= user.getAddresses();
                if(addresses.size()!=0){
                    var addressResponse= AddressResponse.builder()
                            .fullName(user.getFullName())
                            .phoneNumber(user.getPhoneNumber()!=null?user.getPhoneNumber():"")
                            .addresses(addresses)
                            .build();
                    return ResponseEntity.status(HttpStatus.OK).body(addressResponse);
                }else if(user.getPhoneNumber()!=null){
                    return ResponseEntity.status(HttpStatus.OK).body(AddressResponse.builder()
                                    .phoneNumber(user.getPhoneNumber()!=null?user.getPhoneNumber():"")
                                    .fullName(user.getFullName())
                                    .addresses(new HashSet<>())
                                    .build());
                }else{
                    return ResponseEntity.status(HttpStatus.OK).body(AddressResponse.builder().build());
                }
            }
            return ResponseEntity.status(403).body("phiên đăng nhập hết hạn");
        }catch (Exception e){
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }
    @PutMapping("/edit")
    public ResponseEntity<?> editAddressDefault(@RequestBody AddressRequest addressRequest){
        try{
            Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
            if(authentication.isAuthenticated()){
                User user= (User) authentication.getPrincipal();
                Address address= addressService.update_Address_default(addressRequest,user);
                return ResponseEntity.status(HttpStatus.OK).body(address);
            }else{
                return ResponseEntity.status(403).body("session is expires");
            }
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    @PutMapping("/update-address/{id}")
    public ResponseEntity<?> updateAddress(@PathVariable("id") Long id, @RequestBody AddressRequest request){
        try{
            Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
            if(authentication.isAuthenticated()){
                User user= (User) authentication.getPrincipal();
                Address address= addressService .updateAddress(id,request,user);
                return ResponseEntity.status(HttpStatus.OK).body(address);
            }
            throw new LoginException("phiên đăng nhập hết hạn");
        }catch (AddressException e){
            return ResponseEntity.status(400).body(e.getMessage());
        }catch (LoginException e){
            return ResponseEntity.status(403).body(e.getMessage());
        }
    }
}

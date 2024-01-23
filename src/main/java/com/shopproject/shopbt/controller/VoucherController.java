package com.shopproject.shopbt.controller;

import com.shopproject.shopbt.ExceptionCustom.LoginException;
import com.shopproject.shopbt.entity.FreeShippingMember;
import com.shopproject.shopbt.entity.User;
import com.shopproject.shopbt.response.FreeShippingResponse;
import com.shopproject.shopbt.service.FreeShippingMember.FreeShippingMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/web/voucher")
public class VoucherController {
    private final FreeShippingMemberService freeShippingMemberService;
    @GetMapping("/free-shipping")
    public ResponseEntity<?> getALlFreeShippingOfUser(){
        try{
            Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
            if(!(authentication instanceof AnonymousAuthenticationToken)){
                User user= (User)authentication.getPrincipal();
                List<FreeShippingResponse> freeShippingMember= freeShippingMemberService.getAllFreeShippingOfUser(user);
                return ResponseEntity.status(HttpStatus.OK).body(freeShippingMember);
            }
            throw new LoginException("phiên đăng nhập hết hạn");
        }catch(LoginException e){
            return ResponseEntity.status(403).body(e.getMessage());
        }
    }
}

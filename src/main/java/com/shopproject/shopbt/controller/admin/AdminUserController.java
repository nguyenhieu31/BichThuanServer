package com.shopproject.shopbt.controller.admin;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.api.Http;
import com.shopproject.shopbt.entity.User;
import com.shopproject.shopbt.response.UserResponse;
import com.shopproject.shopbt.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@CrossOrigin
@RequestMapping("/system/user")
public class AdminUserController {
    private final UserService userService;
    @GetMapping("")
    public ResponseEntity<?> getAllUser(){
        try{
            Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
            if(!(authentication instanceof AnonymousAuthenticationToken)){
                return ResponseEntity.status(HttpStatus.OK).body(userService.getAllUser());
            }
            return ResponseEntity.status(403).body("Hết phiên đăng nhập hoặc không có quyền truy cập!!!");
        }catch(Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    @PatchMapping("/update")
    public ResponseEntity<?> handleUpdateStatusOfUser(@RequestParam("status") boolean status, @RequestParam("userId") Long userId){

        try{
            Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
            if(!(authentication instanceof AnonymousAuthenticationToken)){
                String extractUserName= authentication.getName();
                userService.updateStatusOfUser(status,userId,extractUserName);
                return ResponseEntity.status(HttpStatus.OK).body("Cập nhật thành công");
            }
            return ResponseEntity.status(403).body("Hết phiên đăng nhập hoặc không có quyền truy cập!!!");
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}

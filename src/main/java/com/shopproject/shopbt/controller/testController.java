package com.shopproject.shopbt.controller;

import com.shopproject.shopbt.request.LoginRequest;
import com.shopproject.shopbt.request.RefreshTokenRequest;
import com.shopproject.shopbt.request.RegisterRequest;
import com.shopproject.shopbt.response.ManagerResponse;
import com.shopproject.shopbt.service.managers.ManagerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class testController {
    private final ManagerService managerService;
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest request){
        return ResponseEntity.status(HttpStatus.OK).body(managerService.register(request));
    }
    @PostMapping("/login")
    public ResponseEntity<ManagerResponse> login(@RequestBody LoginRequest request){
        return ResponseEntity.status(HttpStatus.OK).body(managerService.authenticate(request));
    }
    @GetMapping("/refreshToken")
    public ResponseEntity<ManagerResponse> refreshToken(@RequestBody RefreshTokenRequest request){
        ManagerResponse managerResponse= managerService.refreshToken(request);
        if(managerResponse==null){
            return ResponseEntity.status(403).body(null);
        }
        return ResponseEntity.status(HttpStatus.OK).body(managerResponse);
    }
}

package com.shopproject.shopbt.controller;


import com.shopproject.shopbt.request.LoginRequest;
import com.shopproject.shopbt.request.RefreshTokenRequest;
import com.shopproject.shopbt.request.RegisterRequest;
import com.shopproject.shopbt.request.RegisterUserRequest;
import com.shopproject.shopbt.response.AuthenticationResponse;
import com.shopproject.shopbt.service.authentication.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/web/api/v1")
@RequiredArgsConstructor
public class testController {
    private final AuthenticationService authenticationService;
    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody RegisterUserRequest request){
        return ResponseEntity.status(HttpStatus.OK).body(authenticationService.registerUser(request));
    }
//    @PostMapping("/register")
//    public ResponseEntity<String> register(@RequestBody RegisterRequest request){
//        return ResponseEntity.status(HttpStatus.OK).body(authenticationService.register(request));
//    }
    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody LoginRequest request){
        AuthenticationResponse authenticationResponse= authenticationService.authenticate(request);
        if(authenticationResponse==null){
            return ResponseEntity.status(401).body(null);
        }
        return ResponseEntity.status(HttpStatus.OK).body(authenticationResponse);
    }
    @GetMapping("/refreshToken")
    public ResponseEntity<AuthenticationResponse> refreshToken(@RequestBody RefreshTokenRequest request){
        AuthenticationResponse managerResponse= authenticationService.refreshToken(request);
        if(managerResponse==null){
            return ResponseEntity.status(403).body(null);
        }
        return ResponseEntity.status(HttpStatus.OK).body(managerResponse);
    }
}

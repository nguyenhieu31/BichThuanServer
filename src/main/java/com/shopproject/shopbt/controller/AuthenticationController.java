package com.shopproject.shopbt.controller;


import com.shopproject.shopbt.ExceptionCustom.LoginException;
import com.shopproject.shopbt.ExceptionCustom.LogoutException;
import com.shopproject.shopbt.ExceptionCustom.RefreshTokenException;
import com.shopproject.shopbt.ExceptionCustom.RegisterException;
import com.shopproject.shopbt.request.LoginRequest;
import com.shopproject.shopbt.request.LogoutRequest;
import com.shopproject.shopbt.request.RefreshTokenRequest;
import com.shopproject.shopbt.request.RegisterUserRequest;
import com.shopproject.shopbt.response.AuthenticationResponse;
import com.shopproject.shopbt.service.authentication.AuthenticationService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody RegisterUserRequest request) {
        try{
            String message= authenticationService.registerUser(request);
            return ResponseEntity.status(HttpStatus.OK).body(message);
        }catch (RegisterException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
//    @PostMapping("/register")
//    public ResponseEntity<String> register(@RequestBody RegisterRequest request){
//        return ResponseEntity.status(HttpStatus.OK).body(authenticationService.register(request));
//    }
    public Cookie addAttributeForCookie(Cookie cookie, int expires){
        cookie.setMaxAge(expires);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        return cookie;
    }
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request, HttpServletResponse response){
        try{
            AuthenticationResponse authenticationResponse= authenticationService.authenticate(request);
            Cookie accessTokenCookie= new Cookie("accessToken",authenticationResponse.getToken());
            Cookie refreshTokenCookie= new Cookie("refreshToken",authenticationResponse.getRefreshToken());
            accessTokenCookie=addAttributeForCookie(accessTokenCookie, 60*1000);
            refreshTokenCookie= addAttributeForCookie(refreshTokenCookie,24*60*60*1000);
            response.addCookie(accessTokenCookie);
            response.addCookie(refreshTokenCookie);
            return ResponseEntity.status(200).body(authenticationResponse);
        }catch (LoginException e){
            Map<String,String> error= new HashMap<>();
            error.put("error","Authenticated failed");
            error.put("message",e.getMessage());
            return ResponseEntity.status(401).body(error);
        }
    }
    @PostMapping("/refreshToken")
    public ResponseEntity<?> refreshToken(@RequestBody RefreshTokenRequest request){
        try{
            AuthenticationResponse managerResponse= authenticationService.refreshToken(request);
            return ResponseEntity.status(HttpStatus.OK).body(managerResponse);
        }catch (RefreshTokenException e){
            return ResponseEntity.status(403).body(e.getMessage());
        }
    }
    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestBody LogoutRequest request, HttpServletResponse response){
        try{
            Cookie accessTokenCookie= new Cookie("accessToken", "");
            Cookie refreshTokenCookie= new Cookie("refreshToken", "");
            accessTokenCookie.setMaxAge(0);
            refreshTokenCookie.setMaxAge(0);
            response.addCookie(accessTokenCookie);
            response.addCookie(refreshTokenCookie);
            return ResponseEntity.status(200).body(authenticationService.logout(request));
        }catch (LogoutException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}

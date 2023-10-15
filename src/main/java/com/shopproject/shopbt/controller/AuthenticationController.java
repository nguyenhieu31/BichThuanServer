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
import com.shopproject.shopbt.service.JwtServices.JwtServices;
import com.shopproject.shopbt.service.authentication.AuthenticationService;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/web")
@CrossOrigin
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    private final JwtServices jwtServices;
    @PostMapping("/auth/register")
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
    private Cookie addAttributeForCookie(Cookie cookie, int expires){
        cookie.setMaxAge(expires);
        cookie.setPath("/");
        return cookie;
    }
    private void generatorTokenCookie(HttpServletResponse response, AuthenticationResponse authenticationResponse) {
        Cookie accessTokenCookie= new Cookie("accessToken",authenticationResponse.getToken());
        Cookie refreshTokenCookie= new Cookie("refreshToken",authenticationResponse.getRefreshToken());
        accessTokenCookie=addAttributeForCookie(accessTokenCookie, 5*60*1000);
        refreshTokenCookie= addAttributeForCookie(refreshTokenCookie,24*60*60*1000);
        refreshTokenCookie.setHttpOnly(true);
        response.addCookie(accessTokenCookie);
        response.addCookie(refreshTokenCookie);
    }
    @PostMapping("/auth/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request, HttpServletResponse response){
        try{
            AuthenticationResponse authenticationResponse= authenticationService.authenticate(request);
            generatorTokenCookie(response, authenticationResponse);
            return ResponseEntity.status(200).body(authenticationResponse);
        }catch (LoginException e){
            Map<String,String> error= new HashMap<>();
            error.put("error","Authenticated failed");
            error.put("message",e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }
    @GetMapping("/auth/checkStateLogin")
    public ResponseEntity<?> checkStateLogin(HttpServletRequest request, HttpServletResponse response){
        try{
            Cookie[] cookies= request.getCookies();
            String token = null;
            for(Cookie cookie:cookies){
                if(cookie.getName().equals("accessToken")) token=cookie.getValue();
            }
            if(token!=null && token.length()>0 && !jwtServices.isTokenExpiration(token)){
                return ResponseEntity.status(HttpStatus.OK).body(jwtServices.ExtractUserName(token));
            }else{
                return ResponseEntity.status(401).body("session is expires");
            }
        }catch (ExpiredJwtException e){
            return ResponseEntity.status(403).body("token is expires");
        }
    }
    @GetMapping("/auth/refreshToken")
    public ResponseEntity<?> refreshToken(HttpServletRequest servletRequest, HttpServletResponse response){
        try{
            Cookie[] cookies= servletRequest.getCookies();
            String token = null;
            String refreshToken=null;
            for(Cookie cookie:cookies){
                if(cookie.getName().equals("accessToken")) token=cookie.getValue();
                if(cookie.getName().equals("refreshToken")) refreshToken=cookie.getValue();
            }
            if((token!=null&& refreshToken!=null) && (token.length()>0 && refreshToken.length()>0)){
                var refreshTokenRequest= RefreshTokenRequest.builder()
                        .token(token)
                        .refreshToken(refreshToken)
                        .build();
                AuthenticationResponse authenticationResponse= authenticationService.refreshToken(refreshTokenRequest);
                generatorTokenCookie(response, authenticationResponse);
                return ResponseEntity.status(HttpStatus.OK).body(authenticationResponse);
            }else{
                return ResponseEntity.status(401).body("session expires");
            }
        }catch (RefreshTokenException e){
            return ResponseEntity.status(401).body(e.getMessage());
        }
    }
    @PostMapping("/auth/logout")
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

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
import com.shopproject.shopbt.service.Redis.RedisService;
import com.shopproject.shopbt.service.authentication.AuthenticationService;
import com.shopproject.shopbt.util.CookieUtil;
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
    private final RedisService redisService;
    private final CookieUtil cookieUtil;
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
    @PostMapping("/auth/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request, HttpServletResponse response){
        try{
            AuthenticationResponse authenticationResponse= authenticationService.authenticate(request);
            cookieUtil.generatorTokenCookie(response, authenticationResponse);
            return ResponseEntity.status(200).body(authenticationResponse);
        }catch (LoginException e){
            Map<String,String> error= new HashMap<>();
            error.put("error","Authenticated failed");
            error.put("message",e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/auth/checkStateLogin")
    public ResponseEntity<?> checkStateLogin(HttpServletRequest request){
        try{
            Cookie[] cookies= request.getCookies();
            String token = null;
            String keyToken=null;
            for(Cookie cookie:cookies){
                if(cookie.getName().equals("accessToken")) keyToken=cookie.getValue();
            }
            if(keyToken!=null){
                token= redisService.getDataFromRedis(keyToken);
                if(token!=null && !jwtServices.isTokenExpiration(token)){
                    return ResponseEntity.status(HttpStatus.OK).body(jwtServices.ExtractUserName(token));
                }else{
                    return ResponseEntity.status(403).body("session is expires");
                }
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
                cookieUtil.generatorTokenCookie(response, authenticationResponse);
                return ResponseEntity.status(HttpStatus.OK).body(authenticationResponse);
            }else{
                return ResponseEntity.status(401).body("session expires");
            }
        }catch (RefreshTokenException e){
            var authenticationRes= AuthenticationResponse.builder()
                    .token(null)
                    .refreshToken(null)
                    .build();
            cookieUtil.generatorTokenCookie(response,authenticationRes);
            return ResponseEntity.status(401).body(e.getMessage());
        }
    }
    @PostMapping("/auth/logout")
    public ResponseEntity<String> logout(@RequestBody LogoutRequest request, HttpServletResponse response){
        try{
            var authenticationRes= AuthenticationResponse.builder()
                    .token(null)
                    .refreshToken(null)
                    .build();
            cookieUtil.generatorTokenCookie(response,authenticationRes);
            return ResponseEntity.status(200).body(authenticationService.logout(request));
        }catch (LogoutException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}

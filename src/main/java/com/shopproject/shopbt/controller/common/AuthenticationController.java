package com.shopproject.shopbt.controller.common;

import com.shopproject.shopbt.ExceptionCustom.*;
import com.shopproject.shopbt.entity.Address;
import com.shopproject.shopbt.entity.User;
import com.shopproject.shopbt.request.*;
import com.shopproject.shopbt.response.AuthenticationResponse;
import com.shopproject.shopbt.service.JwtServices.JwtServices;
import com.shopproject.shopbt.service.address.AddressService;
import com.shopproject.shopbt.service.authentication.AuthenticationService;
import com.shopproject.shopbt.util.CookieUtil;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
@RestController
@RequestMapping("/auth")
@CrossOrigin
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    private final JwtServices jwtServices;
    private final CookieUtil cookieUtil;
    private final AddressService addressService;
    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody RegisterUserRequest request) {
        try{
            String message= authenticationService.registerUser(request);
            return ResponseEntity.status(HttpStatus.OK).body(message);
        }catch (RegisterException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    @PostMapping("/register-admin")
    public ResponseEntity<String> registerAdmin(@RequestBody RegisterRequest request){
        try{
            return ResponseEntity.status(HttpStatus.OK).body(authenticationService.register(request));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    @PostMapping("/login")
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
    @GetMapping("/checkStateLogin")
    public ResponseEntity<?> checkStateLogin(HttpServletRequest request){
        try{
            Cookie[] cookies= request.getCookies();
            String token = null;
            String userId= null;
            for(Cookie cookie:cookies){
                if(cookie.getName().equals("accessToken")) token=cookie.getValue();
                if(cookie.getName().equals("userId")) userId=cookie.getValue();
            }
            if(token!=null){
                if(userId!=null){
                    Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
                    if(!(authentication instanceof AnonymousAuthenticationToken)){
                        User user= (User) authentication.getPrincipal();
                        return ResponseEntity.status(HttpStatus.OK).body(user.getFullName());
                    }else{
                        return ResponseEntity.status(403).body("session is expires");
                    }
                }else{
                    return ResponseEntity.status(HttpStatus.OK).body(jwtServices.ExtractUserName(token));
                }
            }else{
                return ResponseEntity.status(403).body("session is expires");
            }
        }catch (ExpiredJwtException e){
            return ResponseEntity.status(403).body("token is expires");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    @GetMapping("/refreshToken")
    public ResponseEntity<?> refreshToken(HttpServletRequest servletRequest, HttpServletResponse response){
        try{
            Cookie[] cookies= servletRequest.getCookies();
            String refreshToken=null;
            for(Cookie cookie:cookies){
                if(cookie.getName().equals("refreshToken")) refreshToken=cookie.getValue();
            }
            if((refreshToken!=null) && (refreshToken.length()>0)){
                var refreshTokenRequest= RefreshTokenRequest.builder()
                        .refreshToken(refreshToken)
                        .build();
                AuthenticationResponse authenticationResponse= authenticationService.refreshToken(refreshTokenRequest);
                cookieUtil.saveAccessTokenCookie(response, authenticationResponse);
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
    @GetMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request, HttpServletResponse response){
        try{
            var authenticationRes= AuthenticationResponse.builder()
                    .token(null)
                    .refreshToken(null)
                    .build();
            String userId=null;
            String accessToken=null;
            String refreshToken=null;
            Cookie[] cookies= request.getCookies();
            if(cookies!=null){
                for(Cookie cookie:cookies){
                    if(cookie.getName().equals("userId")){
                        userId=cookie.getValue();
                    }else if(cookie.getName().equals("accessToken")){
                        accessToken=cookie.getValue();
                    }else if(cookie.getName().equals("refreshToken")){
                        refreshToken=cookie.getValue();
                    }
                }
            }
            SecurityContextHolder.getContext().setAuthentication(null);
            if(userId!=null){
                if(accessToken!=null){
                    Long result= authenticationService.logoutOAuth2(userId,accessToken);
                    if(result>0){
                        cookieUtil.removeCookieOAuth2(response,userId,accessToken);
                        return ResponseEntity.status(200).body("Đăng xuất thành công");
                    }else{
                        throw new LogoutException("Không thể đăng xuất");
                    }
                }else{
                    return ResponseEntity.status(200).body("Đăng xuất thành công");
                }
            }
            cookieUtil.generatorTokenCookie(response,authenticationRes);
            return ResponseEntity.status(200).body(authenticationService.logout(refreshToken));
        }catch (LogoutException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }catch (Exception e){
            return ResponseEntity.status(401).body(e.getMessage());
        }
    }
    @PostMapping("/update-address")
    public ResponseEntity<?> SaveAddress(@RequestBody AddressRequest request){
        try{
            Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
            if(authentication.isAuthenticated()){
                User user= (User) authentication.getPrincipal();
                Address address=addressService.create_Address(request,user);
                return ResponseEntity.status(HttpStatus.OK).body(address);
            }else{
                throw new LoginException("phiên đăng nhập hết hạn");
            }
        } catch (LoginException e){
            return ResponseEntity.status(403).body(e.getMessage());
        }catch (AddressException e){
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }
}

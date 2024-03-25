package com.shopproject.shopbt.controller.web;

import com.shopproject.shopbt.request.FacebookRequest;
import com.shopproject.shopbt.response.AuthenticationResponse;
import com.shopproject.shopbt.response.FaceBookResponse;
import com.shopproject.shopbt.response.GoogleResponse;
import com.shopproject.shopbt.service.OAuth2.FacebookOAuth2Service;
import com.shopproject.shopbt.service.OAuth2.GoogleOAuth2Service;
import com.shopproject.shopbt.util.CookieUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth/oauth2")
@CrossOrigin
public class OAuth2Controller {
    private final GoogleOAuth2Service googleOAuth2Service;
    private final FacebookOAuth2Service facebookOAuth2Service;
    private void addAttributeForCookie(Cookie cookie, int expires){
        cookie.setMaxAge(expires);
        cookie.setPath("/");
    }
    @NotNull
    private ResponseEntity<?> getResponseEntity(String accessToken2, String userId2, Long expiresIn, String fullName,HttpServletResponse response) {
        Cookie accessToken= new Cookie("accessToken", accessToken2);
        Cookie userId= new Cookie("userId", userId2);
        addAttributeForCookie(accessToken, expiresIn.intValue());
        addAttributeForCookie(userId, expiresIn.intValue());
        userId.setHttpOnly(true);
        response.addCookie(accessToken);
        response.addCookie(userId);
        return ResponseEntity.status(HttpStatus.OK).body(fullName);
    }
    @PostMapping("/google")
    public ResponseEntity<?> loginWithGoogle(@RequestBody String code, HttpServletResponse response){
        try{
            String resetCode= code.replace("\"","");
            GoogleResponse responseResult= googleOAuth2Service.authenticateCodeOAuth2(resetCode);
            return getResponseEntity(responseResult.getAccessToken(), responseResult.getUserId(), responseResult.getExpiresIn(), responseResult.getFullName(),response);
        }catch (Exception e){
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }
    @PostMapping("/facebook")
    public ResponseEntity<?> loginWithFacebook(@RequestBody FacebookRequest facebookRequest, HttpServletResponse response){
        try{
            FaceBookResponse oAuth2Response= facebookOAuth2Service.authenticate(facebookRequest);
            return getResponseEntity(oAuth2Response.getAccessToken(), oAuth2Response.getUserId(), oAuth2Response.getExpiresIn(), oAuth2Response.getFullName(),response);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}

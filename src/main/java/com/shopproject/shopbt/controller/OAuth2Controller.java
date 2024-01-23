package com.shopproject.shopbt.controller;

import com.shopproject.shopbt.response.AuthenticationResponse;
import com.shopproject.shopbt.response.OAuth2Response;
import com.shopproject.shopbt.service.OAuth2.GoogleOAuth2Service;
import com.shopproject.shopbt.util.CookieUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/web")
@CrossOrigin
public class OAuth2Controller {
    private final GoogleOAuth2Service googleOAuth2Service;
    private final CookieUtil cookieUtil;
    @PostMapping("/auth/oauth2/google")
    public ResponseEntity<?> loginWithGoogle(@RequestBody String code, HttpServletResponse response){
        try{
            String resetCode= code.replace("\"","");
            OAuth2Response responseResult= googleOAuth2Service.authenticateCodeOAuth2(resetCode);
            cookieUtil.generatorTokenCookie(response,AuthenticationResponse.builder()
                            .token(responseResult.getAccessToken())
                            .refreshToken(responseResult.getRefreshToken())
                    .build());
            return ResponseEntity.status(HttpStatus.OK).body(responseResult.getFullName());
        }catch (Exception e){
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }
}

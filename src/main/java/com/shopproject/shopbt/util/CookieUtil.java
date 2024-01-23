package com.shopproject.shopbt.util;

import com.google.api.Http;
import com.shopproject.shopbt.response.AuthenticationResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
public class CookieUtil {
    @Value("${JWT.EXPIRATION_ACCESS_TOKEN}")
    private String expiresAccessToken;
    @Value("${JWT.EXPIRATION_REFRESH_TOKEN}")
    private String expiresRefreshToken;
    public Cookie addAttributeForCookie(Cookie cookie, int expires){
        cookie.setMaxAge(expires);
        cookie.setPath("/");
//        cookie.setAttribute("SameSite","None");
//        cookie.setSecure(true);
        return cookie;
    }
    public void generatorTokenCookie(HttpServletResponse response, AuthenticationResponse authenticationResponse) {
        Cookie accessTokenCookie= new Cookie("accessToken",authenticationResponse.getToken());
        Cookie refreshTokenCookie= new Cookie("refreshToken",authenticationResponse.getRefreshToken());
        if(authenticationResponse.getToken()==null || authenticationResponse.getRefreshToken()==null){
            accessTokenCookie=addAttributeForCookie(accessTokenCookie, 0);
            refreshTokenCookie= addAttributeForCookie(refreshTokenCookie,0);
        }else{
            accessTokenCookie=addAttributeForCookie(accessTokenCookie, Integer.parseInt(expiresAccessToken)/1000);
            refreshTokenCookie= addAttributeForCookie(refreshTokenCookie,Integer.parseInt(expiresRefreshToken)/1000);
        }
        refreshTokenCookie.setHttpOnly(true);
        response.addCookie(accessTokenCookie);
        response.addCookie(refreshTokenCookie);
    }
    public void saveNewTokenCookie(HttpServletResponse response, AuthenticationResponse authenticationResponse){
        Cookie accessTokenCookie= new Cookie("accessToken", authenticationResponse.getToken());
        accessTokenCookie=addAttributeForCookie(accessTokenCookie,Integer.parseInt(expiresAccessToken)/1000);
        response.addCookie(accessTokenCookie);
    }
}

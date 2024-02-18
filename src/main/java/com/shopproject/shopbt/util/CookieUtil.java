package com.shopproject.shopbt.util;

import com.shopproject.shopbt.response.AuthenticationResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
public class CookieUtil {
    @Value("${JWT.EXPIRATION_ACCESS_TOKEN}")
    private String expiresAccessToken;
    @Value("${JWT.EXPIRATION_REFRESH_TOKEN}")
    private String expiresRefreshToken;
    public Cookie addAttributeForCookie(Cookie cookie, int expiresInSeconds){
        cookie.setMaxAge(Math.max(expiresInSeconds, 0));
        cookie.setPath("/");
//        cookie.setAttribute("SameSite","Lax");
//        cookie.setSecure(true);
        return cookie;
    }
    private void generatorToken(HttpServletResponse response, AuthenticationResponse authenticationResponse, String expiresAccessToken, String expiresRefreshToken) {
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
    public void generatorTokenCookie(HttpServletResponse response, AuthenticationResponse authenticationResponse) {
        generatorToken(response, authenticationResponse, expiresAccessToken, expiresRefreshToken);
    }
//    public void generatorTokenCookieOAuth2(HttpServletResponse response, AuthenticationResponse authenticationResponse){
//        generatorToken(response, authenticationResponse, expiresAccessTokenGoogle, expiresRefreshTokenGoogle);
//    }
    public void saveAccessTokenCookie(HttpServletResponse response, AuthenticationResponse authenticationResponse){
        Cookie accessTokenCookie= new Cookie("accessToken", authenticationResponse.getToken());
        accessTokenCookie=addAttributeForCookie(accessTokenCookie, Integer.parseInt(expiresAccessToken)/1000);
        response.addCookie(accessTokenCookie);
    }
    public void removeCookieOAuth2(HttpServletResponse response, String userId, String accessToken){
        Cookie accessTokenCookie= new Cookie("accessToken", accessToken);
        Cookie userIdCookie= new Cookie("userId", userId);
        accessTokenCookie=addAttributeForCookie(accessTokenCookie, 0);
        userIdCookie=addAttributeForCookie(userIdCookie, 0);
        userIdCookie.setHttpOnly(true);
        response.addCookie(accessTokenCookie);
        response.addCookie(userIdCookie);
    }
}

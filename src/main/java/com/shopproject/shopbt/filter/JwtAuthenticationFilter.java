package com.shopproject.shopbt.filter;

import com.shopproject.shopbt.ExceptionCustom.LoginException;
import com.shopproject.shopbt.service.JwtServices.JwtServices;
import com.shopproject.shopbt.service.OAuth2.GoogleOAuth2Service;
import com.shopproject.shopbt.service.Redis.RedisService;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.modelmapper.internal.bytebuddy.implementation.bytecode.Throw;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtServices jwtServices;
    @Autowired
    private final UserDetailsService userDetailsService;
    private final RedisService redisService;
    private final GoogleOAuth2Service googleOAuth2Service;
    @Value("${GOOGLE.STATE_KEY}")
    private String googleState;
    @Value("${GOOGLE.ID-TOKEN}")
    private String googleIdTokenKey;
    @Override
    protected void doFilterInternal(HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull FilterChain filterChain) throws ServletException, IOException {
        String authorization= request.getHeader("Authorization");
        String token=null;
        String keyToken=null;
        String stateOAuth=null;
        final String userName;
        if(request.getServletPath().startsWith("/socket.io")){
            filterChain.doFilter(request,response);
            return;
        }
        if(request.getServletPath().startsWith("/web")
            && !request.getServletPath().startsWith("/web/cart")
            && !request.getServletPath().startsWith("/web/auth/checkStateLogin")
            && !request.getServletPath().startsWith("/web/address")
            && !request.getServletPath().startsWith("/web/auth/update-address")
            && !request.getServletPath().startsWith("/web/voucher")
            && !request.getServletPath().startsWith("/web/order")
            && !request.getServletPath().startsWith("/web/comment")
        ){
            filterChain.doFilter(request,response);
            return;
        }
        if(authorization==null || !authorization.startsWith("Bearer")){
            filterChain.doFilter(request,response);
            return;
        }
        keyToken= authorization.substring(7);
        try{
            token = redisService.getDataFromRedis(keyToken);
            stateOAuth= redisService.getDataFromRedis(googleState);
            if(stateOAuth!=null && !googleOAuth2Service.checkExpiresAccessToken()){
                String email= redisService.getDataFromRedis("email");
                if(email!=null){
                    UserDetails userDetails= this.userDetailsService.loadUserByUsername(email);
                    if(userDetails.getUsername().equals(email)){
                        UsernamePasswordAuthenticationToken authenticationToken= new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
                        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    }
                }
            }else if(token!=null && !jwtServices.isTokenInBlackList(token)){
                boolean checkExpirationToken= jwtServices.isTokenExpiration(token);
                if(!checkExpirationToken){
                    userName= jwtServices.ExtractUserName(token);
                    if(userName !=null){
                        UserDetails userDetails= this.userDetailsService.loadUserByUsername(userName);
                        if(userName.equals(userDetails.getUsername()) && !jwtServices.isTokenExpiration(token)) {
                            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                        }
                    }
                }
            }else{
                throw new ExpiredJwtException(null,null,"token is not found");
            }
        }catch (ExpiredJwtException | LoginException e){
            response.setStatus(403);
            response.getWriter().write("Login session expired");
            return;
        }catch (Exception e){
                System.out.println(e.getMessage());
                response.setStatus(401);
                response.getWriter().write("token isn't valid");
                return;
        }
        filterChain.doFilter(request,response);
    }
}

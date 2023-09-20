package com.shopproject.shopbt.filter;

import com.shopproject.shopbt.service.JwtServices.JwtServices;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorization= request.getHeader("Authorization");
        String token=null;
        final String managerEmail;
        if(request.getServletPath().contains("/auth")){
            filterChain.doFilter(request,response);
            return;
        }
        if(authorization==null && !authorization.startsWith("Bearer")){
            filterChain.doFilter(request,response);
            return;
        }
        token= authorization.substring(7);
        if(token!=null && !jwtServices.isTokenInBlackList(token)){
            try {
                boolean checkExpirationToken= jwtServices.isTokenExpiration(token);
                if(!checkExpirationToken){
                    managerEmail= jwtServices.ExtractUserName(token);
                    if(managerEmail !=null){
                        UserDetails userDetails= this.userDetailsService.loadUserByUsername(managerEmail);
                        if(managerEmail.equals(userDetails.getUsername()) && !jwtServices.isTokenExpiration(token)){
                            UsernamePasswordAuthenticationToken authenticationToken= new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
                            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                        }
                    }
                }
            }catch (ExpiredJwtException e){
                response.setStatus(403);
                response.getWriter().write("Login session expired");
                return;
            }
        }else{
            response.setStatus(402);
            response.getWriter().write("token isn't valid");
            return;
        }
        filterChain.doFilter(request,response);
    }
}

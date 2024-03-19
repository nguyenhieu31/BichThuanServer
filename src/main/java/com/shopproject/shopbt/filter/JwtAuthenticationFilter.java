package com.shopproject.shopbt.filter;

import com.shopproject.shopbt.ExceptionCustom.LoginException;
import com.shopproject.shopbt.entity.User;
import com.shopproject.shopbt.entity.WhiteList;
import com.shopproject.shopbt.repository.WhiteList.WhiteListRepo;
import com.shopproject.shopbt.service.JwtServices.JwtServices;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtServices jwtServices;
    private final UserDetailsService userDetailsService;
    private final WhiteListRepo whiteListRepo;
    @Override
    protected void doFilterInternal(HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull FilterChain filterChain) throws ServletException, IOException {
        String authorization= request.getHeader("Authorization");
        String userId=null;
        Cookie[] cookies= request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("userId")) {
                    userId = cookie.getValue();
                }
            }
        }
        String token=null;
        final String userName;
        if(request.getServletPath().startsWith("/socket.io")){
            filterChain.doFilter(request,response);
            return;
        }
        if(request.getServletPath().startsWith("/web")
            && !request.getServletPath().startsWith("/web/cart")
            && !request.getServletPath().startsWith("/web/address")
            && !request.getServletPath().startsWith("/web/voucher")
            && !request.getServletPath().startsWith("/web/order")
            && !request.getServletPath().startsWith("/web/comment")
            && request.getServletPath().startsWith("/web/comment/product")){
            filterChain.doFilter(request,response);
            return;
        }
        if(authorization==null || !authorization.startsWith("Bearer")){
            filterChain.doFilter(request,response);
            return;
        }
        if(request.getServletPath().startsWith("/auth")
           &&!request.getServletPath().startsWith("/auth/checkStateLogin")
           && !request.getServletPath().startsWith("/auth/update-address")
           && !request.getServletPath().startsWith("/auth/logout")
        ){
            filterChain.doFilter(request,response);
            return;
        }
        try{
            token= authorization.substring(7);
            if(userId != null){
                User userDetails= (User) this.userDetailsService.loadUserByUsername(userId);
                WhiteList isToken= whiteListRepo.findByToken(token).orElse(null);
                if(isToken!=null){
                    LocalDateTime now= LocalDateTime.now();
                    LocalDateTime expiresToken= isToken.getExpirationToken();
                    if(!now.isBefore(expiresToken)){
                        whiteListRepo.deleteByToken(token);
                        throw new Exception("Hết phiên đăng nhập vui lòng đăng nhập lại!");
                    }
                    if(userDetails.getUsername().equals(userId)){
                        UsernamePasswordAuthenticationToken authenticationToken= new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
                        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    }else{
                        throw new Exception("userId không hợp lệ hoặc token đã hết hạn");
                    }
                }else{
                    throw new Exception("Token không hợp lệ");
                }
            }else if(!jwtServices.isTokenInBlackList(token)) {
                boolean checkExpirationToken = jwtServices.isTokenExpiration(token);
                if (!checkExpirationToken) {
                    userName = jwtServices.ExtractUserName(token);
                    if (userName != null) {
                        UserDetails userDetails = this.userDetailsService.loadUserByUsername(userName);
                        if (userName.equals(userDetails.getUsername()) && !jwtServices.isTokenExpiration(token)) {
                            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                        }
                    }
                }
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

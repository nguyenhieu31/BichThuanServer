package com.shopproject.shopbt.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.security.sasl.AuthenticationException;
import java.io.IOException;
@Component
@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull FilterChain filterChain) throws ServletException, IOException {
        try{
            Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
            if((authentication!=null) && !(authentication instanceof AnonymousAuthenticationToken)){
                if(request.getServletPath().startsWith(("/system"))){
                    boolean checkedRole= authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
                    if(checkedRole){
                        filterChain.doFilter(request,response);
                    }else{
                        SecurityContextHolder.getContext().setAuthentication(null);
                        response.setStatus(401);
                        throw new AuthenticationException("Bạn không có quyền truy cập vào trang này!");
                    }
                }
            }
            filterChain.doFilter(request,response);
        }catch (Exception e){
            response.setStatus(401);
            response.getWriter().write(e.getMessage());
        }

    }
}

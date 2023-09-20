package com.shopproject.shopbt.service.managers;

import com.shopproject.shopbt.dto.ManagerDTO;
import com.shopproject.shopbt.entity.Manager;
import com.shopproject.shopbt.entity.Roles;
import com.shopproject.shopbt.entity.WhiteList;
import com.shopproject.shopbt.repository.Manager.ManagerRepo;
import com.shopproject.shopbt.repository.Role.RoleRepo;
import com.shopproject.shopbt.repository.WhiteList.WhiteListRepo;
import com.shopproject.shopbt.request.LoginRequest;
import com.shopproject.shopbt.request.RefreshTokenRequest;
import com.shopproject.shopbt.request.RegisterRequest;
import com.shopproject.shopbt.response.ManagerResponse;
import com.shopproject.shopbt.service.JwtServices.JwtServices;
import io.jsonwebtoken.Claims;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ManagerService {
    private final JwtServices jwtServices;
    private final ManagerRepo managerRepo;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepo roleRepo;
    private final AuthenticationManager authenticationManager;
    private final WhiteListRepo whiteListRepo;
    private final UserDetailsService userDetailsService;
    public String register(RegisterRequest request){
        List<String> nameRole= new ArrayList<>();
        request.getRoles().forEach(role->nameRole.add(role.getName()));
        List<Roles> roles= new ArrayList<>();
        nameRole.forEach(name->{
            Optional<Roles> role= roleRepo.findByName(name);
            role.ifPresent(roles::add);
        });
        var manager= Manager.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .roles(roles)
                .build();
        try {
            managerRepo.save(manager);
        }catch (Exception e){
            return "register is failed";
        }
        return "register is successful";
    }
    public ManagerResponse authenticate(LoginRequest request){
        Authentication authentication= authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(),request.getPassword())
        );
        System.out.println(authentication);
        var manager= managerRepo.findByEmail(request.getEmail())
                .orElseThrow(()-> new UsernameNotFoundException("manager not found"));
        var token= jwtServices.GeneratorAccessToken(manager);
        var refreshToken= jwtServices.GeneratorRefreshToken(manager);
        Date expiration= jwtServices.decodedToken(refreshToken).getExpiration();
        System.out.println(expiration);
        Instant instant= expiration.toInstant();
        ZoneId zoneId= ZoneId.of("UTC");
        var saveTokenOnWhiteList= WhiteList.builder()
                .token(refreshToken)
                .expirationToken(instant.atZone(zoneId).toLocalDateTime())
                .build();
        whiteListRepo.save(saveTokenOnWhiteList);
        return ManagerResponse.builder()
                .token(token)
                .refreshToken(refreshToken)
                .build();
    }
    public ManagerResponse refreshToken(RefreshTokenRequest request){
        String newToken=null;
        if(!jwtServices.isTokenExpiration(request.getRefreshToken()) && request.getRefreshToken() !=null){
            Claims claims= jwtServices.decodedToken(request.getRefreshToken());
            UserDetails manager= this.userDetailsService.loadUserByUsername(claims.getSubject());
            newToken= jwtServices.GeneratorTokenByRefreshToken(request.getToken(),manager);
        }else{
            return null;
        }
        return ManagerResponse.builder()
                .token(newToken)
                .refreshToken(request.getRefreshToken())
                .build();
    }
}

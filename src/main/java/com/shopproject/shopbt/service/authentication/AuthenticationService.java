package com.shopproject.shopbt.service.authentication;

import com.shopproject.shopbt.entity.*;
import com.shopproject.shopbt.repository.Manager.ManagerRepo;
import com.shopproject.shopbt.repository.Role.RoleRepo;
import com.shopproject.shopbt.repository.WhiteList.WhiteListRepo;
import com.shopproject.shopbt.repository.user.UserRepository;
import com.shopproject.shopbt.request.LoginRequest;
import com.shopproject.shopbt.request.RefreshTokenRequest;
import com.shopproject.shopbt.request.RegisterRequest;
import com.shopproject.shopbt.request.RegisterUserRequest;
import com.shopproject.shopbt.response.AuthenticationResponse;
import com.shopproject.shopbt.service.JwtServices.JwtServices;
import io.jsonwebtoken.Claims;
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
import java.time.ZoneId;
import java.util.*;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final JwtServices jwtServices;
    private final ManagerRepo managerRepo;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepo roleRepo;
    private final AuthenticationManager authenticationManager;
    private final WhiteListRepo whiteListRepo;
    private final UserDetailsService userDetailsService;
    private final UserRepository userRepository;
    //admin
    public String register(RegisterRequest request){
        List<String> nameRole= new ArrayList<>();
        request.getRoles().forEach(role->nameRole.add(role.getName()));
        List<Roles> roles= new ArrayList<>();
        nameRole.forEach(name->{
            Optional<Roles> role= roleRepo.findByName(name);
            role.ifPresent(roles::add);
        });
        var manager= Manager.builder()
                .managerName(request.getUserName())
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
    //user
    public String registerUser(RegisterUserRequest request){
        Date time= new Date(System.currentTimeMillis());
        Instant instant= time.toInstant();
        ZoneId zoneId= ZoneId.of("UTC");
        var user= User.builder()
                .userName(request.getUserName())
                .fullName(request.getFullName())
                .password(passwordEncoder.encode(request.getPassword()))
                .phoneNumber(request.getPhoneNumber())
                .role("USER")
                .createAt(instant.atZone(zoneId).toLocalDateTime())
                .updateAt(instant.atZone(zoneId).toLocalDateTime())
                .build();
        var address= Address.builder()
                .address(request.getAddress())
                .user(user)
                .build();
        if (user.getAddresses() == null) {
            user.setAddresses(new HashSet<>());
        }
        user.getAddresses().add(address);
        try {
            userRepository.save(user);
            return "register is successful";
        }catch (Exception e){
            return "register is failed"+ e.getMessage();
        }
    }
    public AuthenticationResponse authenticate(LoginRequest request){
        Authentication authentication= authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUserName(),request.getPassword())
        );
        var manager= managerRepo.findByManagerName(request.getUserName());
        String token=null;
        String refreshToken= null;
        if(manager.isPresent()){
            token= jwtServices.GeneratorAccessToken(manager.get());
            refreshToken= jwtServices.GeneratorRefreshToken(manager.get());
        }else{
            var user= userRepository.findByUserName(request.getUserName())
                    .orElseThrow(()-> new UsernameNotFoundException("user is not found"));
            token= jwtServices.GeneratorAccessToken(user);
            refreshToken= jwtServices.GeneratorRefreshToken(user);
        }
        Date expiration= jwtServices.decodedToken(refreshToken).getExpiration();
        Instant instant= expiration.toInstant();
        ZoneId zoneId= ZoneId.of("UTC");
        var saveTokenOnWhiteList= WhiteList.builder()
                .token(refreshToken)
                .expirationToken(instant.atZone(zoneId).toLocalDateTime())
                .build();
        whiteListRepo.save(saveTokenOnWhiteList);
        if(token==null && refreshToken==null){
            return null;
        }
        return AuthenticationResponse.builder()
                .token(token)
                .refreshToken(refreshToken)
                .build();
    }
    public AuthenticationResponse refreshToken(RefreshTokenRequest request){
        String newToken=null;
        if(!jwtServices.isTokenExpiration(request.getRefreshToken()) && request.getRefreshToken() !=null){
            Claims claims= jwtServices.decodedToken(request.getRefreshToken());
            UserDetails manager= this.userDetailsService.loadUserByUsername(claims.getSubject());
            newToken= jwtServices.GeneratorTokenByRefreshToken(request.getToken(),manager);
        }else{
            return null;
        }
        return AuthenticationResponse.builder()
                .token(newToken)
                .refreshToken(request.getRefreshToken())
                .build();
    }
}

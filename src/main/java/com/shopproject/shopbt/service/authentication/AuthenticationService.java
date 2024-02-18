package com.shopproject.shopbt.service.authentication;

import com.shopproject.shopbt.ExceptionCustom.LoginException;
import com.shopproject.shopbt.ExceptionCustom.LogoutException;
import com.shopproject.shopbt.ExceptionCustom.RefreshTokenException;
import com.shopproject.shopbt.ExceptionCustom.RegisterException;
import com.shopproject.shopbt.entity.*;
import com.shopproject.shopbt.repository.Manager.ManagerRepo;
import com.shopproject.shopbt.repository.Role.RoleRepo;
import com.shopproject.shopbt.repository.WhiteList.WhiteListRepo;
import com.shopproject.shopbt.repository.carts.CartRepository;
import com.shopproject.shopbt.repository.user.UserRepository;
import com.shopproject.shopbt.request.*;
import com.shopproject.shopbt.response.AuthenticationResponse;
import com.shopproject.shopbt.service.JwtServices.JwtServices;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
    private final CartRepository cartRepository;
    //admin
    public String register(RegisterRequest request) throws Exception {
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
            e.printStackTrace();
            throw new Exception("Đăng kí thất bại");
        }
        return "Đăng kí thành công";
    }
    //user
    public String registerUser(RegisterUserRequest request) throws RegisterException {
        if(request.getUserName()!=null){
            var user= userRepository.findByUserNameAndActiveTrue(request.getUserName());
            if(user.isPresent()){
                throw new RegisterException("Tài khoản đã tồn tại");
            }
        }
        if(request.getPhoneNumber()!=null){
            var user= userRepository.findByPhoneNumber(request.getPhoneNumber());
            if(user.isPresent()){
                throw new RegisterException("Số điện thoại đã tồn tại");
            }
        }
        var user= User.builder()
                .userName(request.getUserName())
                .fullName(request.getFullName())
                .password(passwordEncoder.encode(request.getPassword()))
                .phoneNumber(request.getPhoneNumber())
                .role("USER")
                .active(true)
                .build();
        var cart= Cart.builder()
                .user(user)
                .build();
        if (user.getAddresses() == null) {
            user.setAddresses(new HashSet<>());
        }
        try {
            userRepository.save(user);
            cartRepository.save(cart);
            return "đăng kí thành công";
        }catch (Exception e){
            throw new RegisterException(e.getMessage());
        }
    }
    public AuthenticationResponse authenticate(LoginRequest request) throws Exception {
        String token = null;
        String refreshToken = null;
        Authentication authentication= authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUserName(),request.getPassword())
        );
        if(!(authentication instanceof AnonymousAuthenticationToken)){
            UserDetails user= (UserDetails) authentication.getPrincipal();
            token= jwtServices.GeneratorAccessToken(user);
            refreshToken= jwtServices.GeneratorRefreshToken(user);
            Date expiration= jwtServices.decodedToken(refreshToken).getExpiration();
            Instant instant= expiration.toInstant();
            ZoneId zoneId= ZoneId.of("UTC");
            var saveTokenOnWhiteList= WhiteList.builder()
                    .token(refreshToken)
                    .expirationToken(instant.atZone(zoneId).toLocalDateTime())
                    .build();
            whiteListRepo.save(saveTokenOnWhiteList);
        }
        if(token==null && refreshToken==null){
            throw new LoginException("Tài khoản hoặc mật khẩu không đúng!");
        }
        return AuthenticationResponse.builder()
                .token(token)
                .refreshToken(refreshToken)
                .build();
    }
    public AuthenticationResponse refreshToken(RefreshTokenRequest request) throws RefreshTokenException {
        String newToken=null;
        String refreshToken= request.getRefreshToken();
        if(request.getRefreshToken() !=null){
            if(!jwtServices.isTokenExpiration(refreshToken)){
                Claims claims= jwtServices.decodedToken(refreshToken);
                UserDetails user= this.userDetailsService.loadUserByUsername(claims.getSubject());
                newToken= jwtServices.GeneratorTokenByRefreshToken(user);
            }else{
                throw new RefreshTokenException("refreshToken expired");
            }
        }else{
            throw new RefreshTokenException("RefreshToken invalid");
        }
        if(newToken!=null){
            return AuthenticationResponse.builder()
                    .token(newToken)
                    .refreshToken(refreshToken)
                    .build();
        }else{
            throw new RefreshTokenException("refreshToken expired");
        }
    }
    @Transactional
    public Long logoutOAuth2(String userId, String accessToken) throws Exception {
        User isUser= userRepository.findByUserNameAndActiveTrue(userId).orElse(null);
        WhiteList isToken=whiteListRepo.findByToken(accessToken).orElse(null);
        if(isUser!=null && isToken!=null){
            return whiteListRepo.deleteByToken(accessToken);
        }else{
            throw new Exception("Không thể đăng xuất hoặc chưa đăng nhập");
        }
    }
    @Transactional
    public Long clearToken(String refreshToken) throws Exception {
        if(refreshToken!=null){
            return whiteListRepo.deleteByToken(refreshToken);
        }else{
            throw new Exception("không thể đăng xuất hoặc chưa đăng nhập!");
        }

    }
    @Transactional
    public String logout(String refreshToken) throws Exception {
        Long deleteRecord=clearToken(refreshToken);
        if(deleteRecord>0){
            return "đăng xuất thành công";
        }
        throw new LogoutException("đăng xuất thất bại");
    }
}

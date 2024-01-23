package com.shopproject.shopbt.service.authentication;

import com.shopproject.shopbt.ExceptionCustom.LoginException;
import com.shopproject.shopbt.ExceptionCustom.LogoutException;
import com.shopproject.shopbt.ExceptionCustom.RefreshTokenException;
import com.shopproject.shopbt.ExceptionCustom.RegisterException;
import com.shopproject.shopbt.entity.*;
import com.shopproject.shopbt.repository.BlackList.BlackListRepo;
import com.shopproject.shopbt.repository.Manager.ManagerRepo;
import com.shopproject.shopbt.repository.Role.RoleRepo;
import com.shopproject.shopbt.repository.WhiteList.WhiteListRepo;
import com.shopproject.shopbt.repository.carts.CartRepository;
import com.shopproject.shopbt.repository.user.UserRepository;
import com.shopproject.shopbt.request.*;
import com.shopproject.shopbt.response.AuthenticationResponse;
import com.shopproject.shopbt.service.JwtServices.JwtServices;
import com.shopproject.shopbt.service.Redis.RedisService;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
    private final BlackListRepo blackListRepo;
    private final CartRepository cartRepository;
    private final RedisService redisService;
    @Value("${ACCESS_TOKEN_KEY}")
    private String accessTokenKey;
    @Value("${REFRESH_TOKEN_KEY}")
    private String refreshTokenKey;
    @Value("${JWT.EXPIRATION_ACCESS_TOKEN}")
    private String expirationToken;
    @Value("${JWT.EXPIRATION_REFRESH_TOKEN}")
    private String expirationRefreshToken;
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
    public String registerUser(RegisterUserRequest request) throws RegisterException {
        if(request.getUserName()!=null){
            var user= userRepository.findByUserName(request.getUserName());
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
            throw new LoginException("Tài khoản hoặc mật khẩu không đúng!");
        }
        redisService.saveDataInRedis(accessTokenKey, token,Long.parseLong(expirationToken));
        redisService.saveDataInRedis(refreshTokenKey,refreshToken,Long.parseLong(expirationRefreshToken));
        return AuthenticationResponse.builder()
                .token(accessTokenKey)
                .refreshToken(refreshTokenKey)
                .build();
    }
    public AuthenticationResponse refreshToken(RefreshTokenRequest request) throws RefreshTokenException {
        String newToken=null;
        if(request.getRefreshToken() !=null){
            String refreshToken= redisService.getDataFromRedis(request.getRefreshToken());
            if(refreshToken!=null && !jwtServices.isTokenExpiration(refreshToken)){
                Claims claims= jwtServices.decodedToken(refreshToken);
                UserDetails user= this.userDetailsService.loadUserByUsername(claims.getSubject());
                newToken= jwtServices.GeneratorTokenByRefreshToken(user);
                redisService.saveDataInRedis(accessTokenKey,newToken, Long.parseLong(expirationToken));
            }else{
                throw new RefreshTokenException("refreshToken expired");
            }
        }else{
            throw new RefreshTokenException("RefreshToken invalid");
        }
        if(newToken!=null){
            return AuthenticationResponse.builder()
                    .token(accessTokenKey)
                    .refreshToken(refreshTokenKey)
                    .build();
        }else{
            throw new RefreshTokenException("refreshToken expired");
        }
    }
    @Transactional
    public Long clearToken() throws Exception {
        String refreshToken= redisService.getDataFromRedis(refreshTokenKey);
        if(refreshToken!=null){
            Long deleteRecord= whiteListRepo.deleteByToken(refreshToken);
            redisService.deleteDataInRedis(accessTokenKey);
            redisService.deleteDataInRedis(refreshTokenKey);
            return deleteRecord;
        }else{
            throw new Exception("không thể đăng xuất hoặc chưa đăng nhập!");
        }

    }
    @Transactional
    public String logout() throws Exception {
        Long deleteRecord=clearToken();
        if(deleteRecord>0){
            return "đăng xuất thành công";
        }
        throw new LogoutException("đăng xuất thất bại");
    }
}

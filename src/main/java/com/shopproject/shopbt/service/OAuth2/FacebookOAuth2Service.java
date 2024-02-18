package com.shopproject.shopbt.service.OAuth2;

import com.shopproject.shopbt.entity.Address;
import com.shopproject.shopbt.entity.Cart;
import com.shopproject.shopbt.entity.User;
import com.shopproject.shopbt.entity.WhiteList;
import com.shopproject.shopbt.repository.WhiteList.WhiteListRepo;
import com.shopproject.shopbt.repository.carts.CartRepository;
import com.shopproject.shopbt.repository.user.UserRepository;
import com.shopproject.shopbt.request.FacebookRequest;
import com.shopproject.shopbt.response.FaceBookResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashSet;

@Service
@RequiredArgsConstructor
public class FacebookOAuth2Service {
    @Value("${FACEBOOK.APP-ID}")
    private String appId;
    @Value("${FACEBOOK.APP-SECRET}")
    private String appSecret;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CartRepository cartRepository;
    private final WhiteListRepo whiteListRepo;
    public FaceBookResponse authenticate(FacebookRequest facebookRequest) throws Exception {
        try{
            User isUser= userRepository.findByUserNameAndActiveTrue(facebookRequest.getUserId()).orElse(null);

            if(isUser==null){
                var user= User.builder()
                        .userName(facebookRequest.getUserId())
                        .role("USER")
                        .password(passwordEncoder.encode("User"+facebookRequest.getUserId()))
                        .fullName(facebookRequest.getName())
                        .phoneNumber(null)
                        .build();
                Address address= Address.builder()
                        .address("")
                        .user(user)
                        .build();
                Cart cart= Cart.builder()
                        .user(user)
                        .build();
                if (user.getAddresses() == null) {
                    user.setAddresses(new HashSet<>());
                }
                user.getAddresses().add(address);
                userRepository.save(user);
                cartRepository.save(cart);
            }
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime expires= now.plusDays(2);
            Duration duration= Duration.between(now,expires);
            Long seconds= duration.getSeconds();
            WhiteList isToken= whiteListRepo.findByUserId(facebookRequest.getUserId()).orElse(null);
            if (isToken!=null){
                isToken.setToken(facebookRequest.getAccessToken());
                whiteListRepo.save(isToken);
            }else{
                var saveToken= WhiteList.builder()
                        .token(facebookRequest.getAccessToken())
                        .expirationToken(expires)
                        .userId(facebookRequest.getUserId())
                        .build();
                whiteListRepo.save(saveToken);
            }
            return FaceBookResponse.builder()
                    .accessToken(facebookRequest.getAccessToken())
                    .userId(isUser != null ? isUser.getUsername() : null)
                    .fullName(isUser != null ? isUser.getFullName() : null)
                    .expiresIn(seconds)
                    .build();
        }catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }
}

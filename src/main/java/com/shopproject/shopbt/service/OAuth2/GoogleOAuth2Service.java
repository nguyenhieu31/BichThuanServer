package com.shopproject.shopbt.service.OAuth2;


import com.shopproject.shopbt.Enum.GrantTypeOAuthGoogle;
import com.shopproject.shopbt.entity.Address;
import com.shopproject.shopbt.entity.Cart;
import com.shopproject.shopbt.entity.User;
import com.shopproject.shopbt.entity.WhiteList;
import com.shopproject.shopbt.repository.WhiteList.WhiteListRepo;
import com.shopproject.shopbt.repository.carts.CartRepository;
import com.shopproject.shopbt.repository.user.UserRepository;
import com.shopproject.shopbt.response.GoogleResponse;
import com.shopproject.shopbt.service.Redis.RedisService;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GoogleOAuth2Service {
    @Value("${GOOGLE.TOKEN.URL}")
    private String googleTokenEndpoint;
    @Value("${GOOGLE.REVOKE.URL}")
    private String googleRevokeTokenEndpoint;
    @Value("${GOOGLE.USERINFO.URL}")
    private String googleUserInfoEndpoint;
    @Value("${GOOGLE.CLIENT_ID}")
    private String googleClientId;
    @Value("${GOOGLE.CLIENT_SECRET}")
    private String googleClientSecret;
    @Value("${GOOGLE.GRANT_TYPE}")
    private String googleGrantType;
    @Value("${GOOGLE.REDIRECT_URI}")
    private String googleRedirectUri;
    @Value("${GOOGLE.STATE_KEY}")
    private String googleStateKey;
    @Value("${ACCESS_TOKEN_KEY}")
    private String  accessTokenKey;
    @Value("${REFRESH_TOKEN_KEY}")
    private String refreshTokenKey;
    @Value("${GOOGLE.EXPIRES_ACCESS-TOKEN}")
    private String googleExpiresAccessToken;
    @Value("${GOOGLE.EXPIRES_REFRESH-TOKEN}")
    private String googleExpiresRefreshToken;
    @Value("${GOOGLE.ID-TOKEN}")
    private String googleIdTokenKey;
    private final RedisService redisService;
    private final RestTemplate restTemplate;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CartRepository cartRepository;
    private final WhiteListRepo whiteListRepo;
    private void clearDataInRedis(){
        redisService.deleteDataInRedis(googleStateKey);
        redisService.deleteDataInRedis(googleIdTokenKey);
        redisService.deleteDataInRedis(accessTokenKey);
        redisService.deleteDataInRedis(refreshTokenKey);
        redisService.deleteDataInRedis("name");
        redisService.deleteDataInRedis("email");
    }
    private void addRequestOAuthInfo(MultiValueMap<String,String> request, String code, String grantType){
        if(code!=null){
            request.add("code",code);
        }
        request.add("client_id",googleClientId);
        request.add("client_secret",googleClientSecret);
        request.add("grant_type",grantType);
    }
    private ResponseEntity<String> revokeToken(String accessToken) throws Exception {
        try{
            String uri = googleRevokeTokenEndpoint +
                    "?token=" + accessToken;
            MultiValueMap<String,String> request= new LinkedMultiValueMap<>();
            request.set("Content-Type","application/json");
            ResponseEntity<String> response= restTemplate.postForEntity(uri,request,String.class);
            clearDataInRedis();
            return response;
        }catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }
    public GoogleResponse authenticateCodeOAuth2(String code) throws Exception {
        try{
            MultiValueMap<String,String> request= new LinkedMultiValueMap<>();
            addRequestOAuthInfo(request, code, GrantTypeOAuthGoogle.authorization_code.toString());
            request.add("redirect_uri",googleRedirectUri);
            HttpHeaders headers= new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            headers.set("User-Agent","google-oauth-playground");
            headers.set("Host","oauth2.googleapis.com");
            HttpEntity<MultiValueMap<String,String>> entity= new HttpEntity<>(request,headers);
            ResponseEntity<String> response= restTemplate.postForEntity(googleTokenEndpoint,entity,String.class);
            JSONObject jsonResponse= new JSONObject(response.getBody());
            String accessToken= jsonResponse.getString("access_token");
            if(accessToken!=null){
                return extractUserInfo(accessToken);
            }else{
                throw new Exception("login failed");
            }
        }catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }
    private GoogleResponse extractUserInfo(String accessToken) throws Exception {
        try{
            HttpHeaders headers= new HttpHeaders();
            headers.set("Authorization","Bearer "+accessToken);
            HttpEntity<String> entity= new HttpEntity<>(headers);
            ResponseEntity<String> response= restTemplate.exchange(googleUserInfoEndpoint, HttpMethod.GET,entity,String.class);
            JSONObject jsonRes= new JSONObject(response.getBody());
            String fullName= jsonRes.getString("name");
            String sub= jsonRes.getString("sub");
            String email= jsonRes.getString("email");
            if(fullName!=null&& sub!=null && email!=null){
                Optional<User> isUser= userRepository.findByUserNameAndActiveTrue(sub);
                if(isUser.isEmpty()){
                    var user= User.builder()
                            .userName(sub)
                            .password(passwordEncoder.encode("User"+sub))
                            .phoneNumber(null)
                            .role("USER")
                            .fullName(fullName)
                            .email(email)
                            .build();
                    var address= Address.builder()
                            .address("")
                            .user(user)
                            .build();
                    var cart= Cart.builder()
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
                WhiteList isToken= whiteListRepo.findByUserId(sub).orElse(null);
                if (isToken!=null){
                    isToken.setToken(accessToken);
                    whiteListRepo.save(isToken);
                }else{
                    var saveToken= WhiteList.builder()
                            .token(accessToken)
                            .expirationToken(expires)
                            .userId(sub)
                            .build();
                    whiteListRepo.save(saveToken);
                }
                return GoogleResponse.builder()
                        .accessToken(accessToken)
                        .fullName(fullName)
                        .userId(sub)
                        .expiresIn(seconds)
                        .build();
            }else{
                throw new Exception("Can't get user info to accessToken");
            }
        }catch(Exception e){
            throw new Exception(e.getMessage());
        }
    }
//    private String refreshToken(String refreshTokenKey) {
//        try{
//            String refreshToken= redisService.getDataFromRedis(refreshTokenKey);
//            if(refreshToken!=null){
//                HttpHeaders headers= new HttpHeaders();
//                headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
//                MultiValueMap<String,String> request= new LinkedMultiValueMap<>();
//                addRequestOAuthInfo(request, null, GrantTypeOAuthGoogle.refresh_token.toString());
//                request.set("refresh_token",refreshToken);
//                HttpEntity<MultiValueMap<String,String>> entity= new HttpEntity<>(request,headers);
//                ResponseEntity<String> response= restTemplate.exchange(googleTokenEndpoint,HttpMethod.POST,entity, String.class);
//                if(response.getStatusCode()==HttpStatus.OK){
//                    return response.getBody();
//                }else{
//                    throw new RuntimeException("Token refresh failed");
//                }
//            }else{
//                throw new Exception("refreshToken expires");
//            }
//        }catch (Exception e){
//            return null;
//        }
//    }
//    public String generatorTokenFromRefreshToken(String refreshTokenKey){
//        String response = refreshToken(refreshTokenKey);
//        JSONObject jsonResponse = new JSONObject(response);
//        String newToken = jsonResponse.getString("access_token");
//        String newIdToken = jsonResponse.getString("id_token");
//        redisService.saveDataInRedis(accessTokenKey, newToken, Long.parseLong(googleExpiresAccessToken));
//        redisService.saveDataInRedis(googleIdTokenKey, newIdToken, Long.parseLong(googleExpiresAccessToken));
//        return newToken!=null?accessTokenKey:null;
//    }
//    public boolean isAccessTokenNotExpired() throws Exception {
//        boolean result = true;
//        String refreshToken = redisService.getDataFromRedis(refreshTokenKey);
//        String idToken = redisService.getDataFromRedis(googleIdTokenKey);
//        if (refreshToken != null && idToken != null) {
//            DecodedJWT jwt = JWT.decode(idToken);
//            long expiration = jwt.getClaim("exp").asLong() * 1000;
//            long currenTime = System.currentTimeMillis();
//            if (Math.abs(currenTime - expiration) <= 50000) {
//                String newToken= generatorTokenFromRefreshToken(refreshTokenKey);
//                result= newToken != null;
//            } else {
//                result = expiration <= currenTime;
//            }
//        } else if (refreshToken != null) {
//            String newToken= generatorTokenFromRefreshToken(refreshTokenKey);
//            result= newToken != null;
//        } else {
//            throw new Exception("token is not found");
//        }
//        return !result;
//    }
//    public String logout() throws Exception {
//        try{
//            String accessToken= redisService.getDataFromRedis(accessTokenKey);
//            if(accessToken!=null){
//                ResponseEntity<String> response= revokeToken(accessToken);
//                return response.getBody();
//            }
//            String refreshToken= redisService.getDataFromRedis(refreshTokenKey);
//            if(refreshToken!=null){
//                ResponseEntity<String> response= revokeToken(refreshToken);
//                return response.getBody();
//            }
//            throw new LogoutException("Đăng xuất thất bại!");
//        }catch (Exception e){
//            throw new LogoutException(e.getMessage());
//        }
//    }
}

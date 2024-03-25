package com.shopproject.shopbt.service.JwtServices;

import com.shopproject.shopbt.ExceptionCustom.RefreshTokenException;
import com.shopproject.shopbt.entity.BlackList;
import com.shopproject.shopbt.entity.WhiteList;
import com.shopproject.shopbt.repository.BlackList.BlackListRepo;
import com.shopproject.shopbt.repository.WhiteList.WhiteListRepo;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Key;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JwtServices {
    @Value("${JWT.SECRET_KEY}")
    private String SECRET_KEY;
    @Value("${JWT.EXPIRATION_ACCESS_TOKEN}")
    private String expirationAccessToken;
    @Value("${JWT.EXPIRATION_REFRESH_TOKEN}")
    private String expirationRefreshToken;

    private final BlackListRepo blackListRepo;

    private final WhiteListRepo whiteListRepo;

    private final UserDetailsService userDetailsService;
    private Key getSignKey(){
        byte[] keys= Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keys);
    }
    private Claims ExtractClaimsAll(String token){
        return Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
    private <T> T ExtractClaim(String token, Function<Claims,T> claimsResolver){
        final Claims claims= ExtractClaimsAll(token);
        return claimsResolver.apply(claims);
    }
    public String ExtractUserName(String token){
        return ExtractClaim(token,Claims::getSubject);
    }
    private String GeneratorToken(Map<String, List<String>> extractClaims, UserDetails userDetails, Long expirationToken){
        Collection<? extends GrantedAuthority> managerRoles= userDetails.getAuthorities();
        List<String> roles= managerRoles.stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
        extractClaims.put("roles", roles);
        return Jwts.builder()
                .setClaims(extractClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+ expirationToken))
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }
    public String GeneratorAccessToken(UserDetails userDetails){
        long expirationToken= Long.parseLong(expirationAccessToken);
        return GeneratorToken(new HashMap<>(), userDetails, expirationToken );
    }
    public String GeneratorRefreshToken(UserDetails userDetails){
        long expirationToken=  Long.parseLong(expirationRefreshToken);
        return GeneratorToken(new HashMap<>(), userDetails,expirationToken);
    }
    public Claims decodedToken(String token){
        return ExtractClaimsAll(token);
    }
    private Date ExtractExpiration(String token){
        return ExtractClaim(token,Claims::getExpiration);
    }
    public boolean isTokenExpiration(String token){
        return ExtractExpiration(token).before(new Date());
    }
    public boolean isTokenInBlackList(String token){
        Optional<BlackList> isToken= blackListRepo.findByToken(token);
        return isToken.isPresent();
    }
    @Transactional
    public String GeneratorTokenByRefreshToken( UserDetails userDetails) throws RefreshTokenException {
        List<WhiteList> tokens= whiteListRepo.findAll();
        List<WhiteList> tokensToRemove = new ArrayList<>();
        final String[] newToken= new String[1];
        for (WhiteList item : tokens) {
            try {
                if (item.getUserId()==null && !isTokenExpiration(item.getToken())) {
                    final String userName = ExtractUserName(item.getToken());
                    if (userName.equals(userDetails.getUsername())) {
                        UserDetails user = this.userDetailsService.loadUserByUsername(userName);
                        newToken[0] = this.GeneratorAccessToken(user);
                    }
                }
            } catch (ExpiredJwtException e) {
                var tokenBlackList = BlackList.builder()
                        .token(item.getToken())
                        .build();
                tokensToRemove.add(item);
                blackListRepo.save(tokenBlackList);
            }
        }
        for(WhiteList item:tokensToRemove){
            Long deleteRecord= whiteListRepo.deleteByToken(item.getToken());
        }
        if(newToken[0]!= null){
            return newToken[0];
        }else{
            throw new RefreshTokenException("token not valid");
        }
    }
}

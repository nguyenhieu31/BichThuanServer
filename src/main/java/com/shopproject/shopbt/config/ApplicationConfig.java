package com.shopproject.shopbt.config;

import com.shopproject.shopbt.entity.Manager;
import com.shopproject.shopbt.entity.User;
import com.shopproject.shopbt.repository.Manager.ManagerRepo;
import com.shopproject.shopbt.repository.user.UserRepository;
import com.shopproject.shopbt.util.CookieUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Configuration
@EnableTransactionManagement
@RequiredArgsConstructor
public class ApplicationConfig {
    private final ManagerRepo managerRepo;
    private final UserRepository userRepository;
    @Bean
    public CookieUtil cookieUtil(){
        return new CookieUtil();
    }
    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }
    @Bean
    public UserDetailsService userDetailsService(){
        return username -> {
            Optional<User> user= userRepository.findByUserNameAndActiveTrue(username);
            if(user.isPresent()){
                return user.get();
            }else{
                Optional<Manager> manager= managerRepo.findByManagerName(username);
                if(manager.isPresent()){
                    return manager.get();
                }else{
                    throw new UsernameNotFoundException("userName is not found");
                }
            }
        };
    }
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider provider= new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService());
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception{
        return configuration.getAuthenticationManager();
    }
//    @Bean
//    public CorsFilter corsFilter(){
//        UrlBasedCorsConfigurationSource source= new UrlBasedCorsConfigurationSource();
//        CorsConfiguration configuration= new CorsConfiguration();
//        configuration.setAllowCredentials(true);//cho phép sử dụng cookie khi gửi request
//        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE"));
//        configuration.addAllowedOrigin("http://localhost:3000");//url của react js
//        configuration.addAllowedHeader("*");// cho phép tất cả các header
//        configuration.addAllowedMethod("*"); //cho phép các phương thức ( get, post, put, delete)
//        source.registerCorsConfiguration("/**",configuration);
//        return new CorsFilter(source);
//    }
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowCredentials(true);//cho phép sử dụng cookie khi gửi request
        configuration.addAllowedOrigin("http://localhost:3000");//url của react js
        configuration.addAllowedOrigin("http://localhost:3030");
//        configuration.addAllowedOrigin("http://ip172-18-0-41-clrvv3ufml8g00cob46g-3000.direct.labs.play-with-docker.com");
        configuration.setAllowedMethods(Arrays.asList("GET","POST","PUT","PATCH","DELETE","OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        source.registerCorsConfiguration("/socket.io/?EIO=4&transport=polling&t=OiybJbh",configuration);
        return source;
    }
//    @Bean
//    public CorsConfigurationSource corsConfigurationSource() {
//        CorsConfiguration configuration = new CorsConfiguration();
//        configuration.setAllowCredentials(true);//cho phép sử dụng cookie khi gửi request
//        configuration.addAllowedOrigin("http://localhost:3030");//url của react js
////        configuration.addAllowedOrigin("http://ip172-18-0-41-clrvv3ufml8g00cob46g-3000.direct.labs.play-with-docker.com");
//        configuration.setAllowedMethods(Arrays.asList("GET","POST","PUT","PATCH","DELETE","OPTIONS"));
//        configuration.setAllowedHeaders(List.of("*"));
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**", configuration);
//        source.registerCorsConfiguration("/socket.io/?EIO=4&transport=polling&t=OiybJbh",configuration);
//        return source;
//    }
}

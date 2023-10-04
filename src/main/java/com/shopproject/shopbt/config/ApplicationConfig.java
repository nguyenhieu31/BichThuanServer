package com.shopproject.shopbt.config;

import com.shopproject.shopbt.entity.Manager;
import com.shopproject.shopbt.entity.User;
import com.shopproject.shopbt.repository.Manager.ManagerRepo;
import com.shopproject.shopbt.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Optional;

@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {
    @Autowired
    private final ManagerRepo managerRepo;
    @Autowired
    private final UserRepository userRepository;
    @Bean
    public UserDetailsService userDetailsService(){
        return username -> {
             Optional<Manager> manager= managerRepo.findByManagerName(username);
             if(manager.isPresent()){
                 return manager.get();
             }else{
                 Optional<User> user= userRepository.findByUserName(username);
                 if(user.isPresent()){
                     return user.get();
                 }
             }
            throw new UsernameNotFoundException("userName is not found");
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
    @Bean
    public CorsFilter corsFilter(){
        UrlBasedCorsConfigurationSource source= new UrlBasedCorsConfigurationSource();
        CorsConfiguration configuration= new CorsConfiguration();
        configuration.setAllowCredentials(true);//cho phép sử dụng cookie khi gửi request
        configuration.addAllowedOrigin("http://localhost:3000");//url của react js
        configuration.addAllowedHeader("*");// cho phép tất cả các header
        configuration.addAllowedMethod("*"); //cho phép các phương thức ( get, post, put, delete)
        source.registerCorsConfiguration("/**",configuration);
        return new CorsFilter(source);
    }
}

package com.shopproject.shopbt.config;

import com.shopproject.shopbt.filter.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final AuthenticationProvider authenticationProvider;
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowCredentials(true);//cho phép sử dụng cookie khi gửi request
        configuration.addAllowedOrigin("http://localhost:3030");//url của react js
//        configuration.addAllowedOrigin("http://ip172-18-0-41-clrvv3ufml8g00cob46g-3000.direct.labs.play-with-docker.com");
        configuration.setAllowedMethods(Arrays.asList("GET","POST","PUT","PATCH","DELETE","OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        source.registerCorsConfiguration("/socket.io/?EIO=4&transport=polling&t=OiybJbh",configuration);
        return source;
    }

    @Bean
    public SecurityFilterChain filterChain(@NotNull HttpSecurity http) throws Exception{
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth ->
                        auth.requestMatchers("/**","/socket.io/**").permitAll()
                                .requestMatchers("/web/cart/**",
                                        "/web/auth/update-address",
                                        "/web/voucher/**",
                                        "/web/address/**",
                                        "/web/order/**",
                                        "/web/comment/**"
                                ).hasRole("USER")
                                .requestMatchers("/system/**").hasRole("ADMIN")
                                .anyRequest().authenticated()
                )
                .sessionManagement((sessionManagement)->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authenticationProvider(authenticationProvider)
                .logout((logout)->logout
                        .logoutUrl("/auth/logout")
                        .clearAuthentication(true)
                        .invalidateHttpSession(true)
                        .permitAll()
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}

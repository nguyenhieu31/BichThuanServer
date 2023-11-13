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
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    @Autowired
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    @Autowired
    private final AuthenticationProvider authenticationProvider;
    @Bean
    public SecurityFilterChain filterChain(@NotNull HttpSecurity http) throws Exception{
        http
                .cors(Customizer.withDefaults())
                .csrf((csrf)-> csrf.disable())
                .authorizeHttpRequests(auth ->
                        auth.requestMatchers("/**","/socket.io/**").permitAll()
                                .requestMatchers("/web/cart/**").hasRole("USER")
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

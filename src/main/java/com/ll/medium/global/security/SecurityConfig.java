package com.ll.medium.global.security;

import lombok.SneakyThrows;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {
    @Bean
    @SneakyThrows
    public SecurityFilterChain filterChain(HttpSecurity http) {
        http
                .authorizeRequests(authorizeRequests -> authorizeRequests
//                        .requestMatchers("/article/delete/**")
//                            .hasRole("MEMBER", "ROLE_ADMIN")
                        .requestMatchers("/**")
                        .permitAll()
                )
                .headers(
                        headers ->
                                headers.frameOptions(
                                        frameOptions ->
                                                frameOptions.sameOrigin()
                                )
                )
                .csrf(
                        csrf ->
                                csrf.ignoringRequestMatchers(
                                        "/h2-console/**"
                                )
                )
                .formLogin(
                        formLogin -> formLogin
                                .loginPage("/member/login")
                                .defaultSuccessUrl("/")
                )
                .logout(
                        logout -> logout
                                .logoutRequestMatcher(
                                        new AntPathRequestMatcher("/member/logout")
                                )
                                .logoutSuccessUrl("/")
                )
        ;

        return http.build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

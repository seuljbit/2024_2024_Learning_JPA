package com.ezen.boot_JPA.config;

import com.ezen.boot_JPA.security.CustomUserService;
import com.ezen.boot_JPA.security.LoginFailureHandler;
import com.ezen.boot_JPA.security.LoginSuccessHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http, AuthenticationSuccessHandler successHandler) throws Exception {
        return http
                .csrf(csrf -> csrf.disable()) // CSRF 비활성화
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/", "/js/**", "/css/**", "/img/**", "/dist/**", "/upload/**",
                                "/index/**", "/user/join", "/user/login", "/board/list", "/board/register",
                                "/board/password-check", "/gallery/**", "/error").permitAll()
                        .requestMatchers("/board/**").permitAll() // 나머지 /board 경로도 허용
                        .requestMatchers("/user/list").hasRole("ADMIN") // ADMIN 전용
                        .anyRequest().authenticated() // 그 외 모든 요청은 인증 필요
                )
                .formLogin(login -> login
                        .usernameParameter("email")
                        .passwordParameter("pwd")
                        .loginPage("/user/login")
                        .successHandler(successHandler())
                        .failureHandler(failureHandler())
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/user/logout") // 로그아웃 URL
                        .invalidateHttpSession(true) // 세션 무효화
                        .deleteCookies("JSESSIONID") // 쿠키 삭제
                        .logoutSuccessUrl("/")
                )
                .build();
    }

    @Bean
    UserDetailsService customUserService() {
        return new CustomUserService();
    }

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();

    }

    @Bean
    AuthenticationSuccessHandler successHandler() {
        return new LoginSuccessHandler();
    }

    @Bean
    AuthenticationFailureHandler failureHandler() {
        return new LoginFailureHandler();
    }
}

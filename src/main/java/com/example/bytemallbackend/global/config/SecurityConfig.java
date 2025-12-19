package com.example.bytemallbackend.global.config;

import com.example.bytemallbackend.domain.member.entity.enumerate.Role;
import com.example.bytemallbackend.global.security.jwt.JwtAuthenticationFilter;
import com.example.bytemallbackend.global.security.jwt.JwtTokenProvider;
import com.example.bytemallbackend.global.security.oauth.handler.OAuth2AuthenticationSuccessHandler;
import com.example.bytemallbackend.global.security.oauth.service.CustomOAuth2UserService;
import com.example.bytemallbackend.global.util.CookieUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CorsConfigurationSource corsConfigurationSource;
    private final JwtTokenProvider jwtTokenProvider;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;
    private final CookieUtil cookieUtil;

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .httpBasic(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .logout(AbstractHttpConfigurer::disable);


        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http
                .oauth2Login(oauth2 -> oauth2
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(customOAuth2UserService)) // 사용자 정보 로드 서비스
                        .successHandler(oAuth2AuthenticationSuccessHandler) // 성공 시 처리 핸들러
                );

        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**", "/login/**").permitAll()
                        .requestMatchers("/error").permitAll()
                        .requestMatchers("/api/admin/**").hasAuthority(String.valueOf(Role.ADMIN))
                        .requestMatchers("/api/seller/**").hasAuthority(String.valueOf(Role.SELLER))
                        .anyRequest().authenticated());

        http.exceptionHandling(exception -> exception
                .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)));

        http
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider, cookieUtil), UsernamePasswordAuthenticationFilter.class);

        return http.build();

    }

}

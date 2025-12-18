package com.example.bytemallbackend.global.security.oauth.handler;


import com.example.bytemallbackend.global.security.jwt.JwtTokenProvider;
import com.example.bytemallbackend.global.security.principal.AuthMember;
import com.example.bytemallbackend.global.util.CookieUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Duration;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtTokenProvider jwtTokenProvider;
    private final CookieUtil cookieUtil;
    private final StringRedisTemplate redisTemplate;

    private static final String REDIRECT_URI = "http://localhost:5500";

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        AuthMember authMember = (AuthMember) authentication.getPrincipal();
        log.info("OAuth2 Login Success. User ID: {}", authMember.getId());

        // 1. 토큰 생성
        String accessToken = jwtTokenProvider.createAccessToken(authMember);
        String refreshToken = jwtTokenProvider.createRefreshToken(authMember);

        // 2. Redis에 Refresh Token 저장
        redisTemplate.opsForValue().set(
                "RT:" + authMember.getId(),
                refreshToken,
                Duration.ofMillis(604800000) // 7일
        );

        // 3. 쿠키 설정 (헤더에 추가)
        response.addHeader(HttpHeaders.SET_COOKIE, cookieUtil.createAccessTokenCookie(accessToken).toString());
        response.addHeader(HttpHeaders.SET_COOKIE, cookieUtil.createRefreshTokenCookie(refreshToken).toString());

        // 4. 리다이렉트 수행
        getRedirectStrategy().sendRedirect(request, response, REDIRECT_URI);
    }
}
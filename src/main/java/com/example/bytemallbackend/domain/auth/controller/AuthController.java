package com.example.bytemallbackend.domain.auth.controller;

import com.example.bytemallbackend.domain.auth.dto.request.JoinRequest;
import com.example.bytemallbackend.domain.auth.dto.request.LoginRequest;
import com.example.bytemallbackend.domain.auth.dto.response.AuthResult;
import com.example.bytemallbackend.domain.auth.dto.response.LoginResponse;
import com.example.bytemallbackend.domain.auth.service.AuthService;
import com.example.bytemallbackend.global.security.principal.AuthMember;
import com.example.bytemallbackend.global.util.CookieUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final CookieUtil cookieUtil;

    @PostMapping("/join/customer")
    public ResponseEntity<Void> joinCustomer(@RequestBody JoinRequest request) {
        authService.joinCustomer(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/join/seller")
    public ResponseEntity<Void> joinSeller(@RequestBody JoinRequest request) {
        authService.joinSeller(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request, HttpServletResponse response) {
        AuthResult authResult = authService.login(request);

        response.addHeader(HttpHeaders.SET_COOKIE, cookieUtil.createAccessTokenCookie(authResult.getAccessToken()).toString());
        response.addHeader(HttpHeaders.SET_COOKIE, cookieUtil.createRefreshTokenCookie(authResult.getRefreshToken()).toString());

        return ResponseEntity.ok(authResult.getLoginResponse()); // LoginResponse 반환 이유: Role에 따라 페이지 이동 분기처리를 위해
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@AuthenticationPrincipal AuthMember authMember) {
        authService.logout(authMember.getId());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/reissue")
    public ResponseEntity<Void> reissue(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = cookieUtil.resolveToken(request, "refreshToken");

        AuthResult authResult = authService.reissue(refreshToken);

        response.addHeader(HttpHeaders.SET_COOKIE, cookieUtil.createAccessTokenCookie(authResult.getAccessToken()).toString());
        response.addHeader(HttpHeaders.SET_COOKIE, cookieUtil.createRefreshTokenCookie(authResult.getRefreshToken()).toString());

        return ResponseEntity.ok().build();
    }

}

package com.example.bytemallbackend.domain.auth.service;

import com.example.bytemallbackend.domain.auth.dto.request.JoinRequest;
import com.example.bytemallbackend.domain.auth.dto.request.LoginRequest;
import com.example.bytemallbackend.domain.auth.dto.response.AuthResult;
import com.example.bytemallbackend.domain.auth.dto.response.LoginResponse;
import com.example.bytemallbackend.domain.member.entity.Member;
import com.example.bytemallbackend.domain.member.entity.enumerate.Grade;
import com.example.bytemallbackend.domain.member.entity.enumerate.Role;
import com.example.bytemallbackend.domain.member.exception.MemberErrorCode;
import com.example.bytemallbackend.domain.member.repository.MemberRepository;
import com.example.bytemallbackend.global.error.BusinessException;
import com.example.bytemallbackend.global.error.GlobalErrorCode;
import com.example.bytemallbackend.global.security.jwt.JwtTokenProvider;
import com.example.bytemallbackend.global.security.principal.AuthMember;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final StringRedisTemplate redisTemplate;

    /**
     * 일반 구매자 회원가입
     * */
    @Transactional
    public void joinCustomer(JoinRequest request) {
        register(request, Role.CUSTOMER);
    }

    /**
     * 판매자(사업자) 회원가입
     * */
    @Transactional
    public void joinSeller(JoinRequest request) {
        register(request, Role.SELLER);
    }

    /**
     * 로그인
     */
    @Transactional
    public AuthResult login(LoginRequest request) {
        // Spring Security가 인식할 수 있는 Spring Security 전용 authentication 객체(UsernamePasswordAuthenticationToken) 생성
        // ㄴ Spring Security 입장에서는 AuthMember나 MemberEntity 인식 불가
        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword());

        // 여기서 AuthenticationManager는 CustomUserDetailsService 사용하여 인증을 수행
        // ㄴ 즉, authenticationManager.authenticate(authenticationToken); 코드는 CustomUserDetailsService의 loadUserByUsername 메서드를 내부적으로 호출
        Authentication authentication = authenticationManager.authenticate(authToken);
        AuthMember authMember = (AuthMember) authentication.getPrincipal();

        String accessToken = jwtTokenProvider.createAccessToken(authMember);
        String refreshToken = jwtTokenProvider.createRefreshToken(authMember);

        redisTemplate.opsForValue().set(
            "RT:" + authMember.getId(),
                refreshToken,
                Duration.ofMillis(604800000)
        );

        return AuthResult.from(
                accessToken,
                refreshToken,
                authMember
        );

    }

    /**
     * 로그아웃
     */
    @Transactional
    public void logout(Long memberId) {
        redisTemplate.delete("RT:" + memberId);
    }

    /**
     * 토큰 재발급
     */
    @Transactional
    public AuthResult reissue(String refreshToken) {
        // 1. Refresh Token 검증
        if (refreshToken == null || !jwtTokenProvider.validateToken(refreshToken)) {
            throw new BusinessException(GlobalErrorCode.INVALID_INPUT_VALUE); // 유효하지 않은 토큰
        }

        // 2. 토큰에서 인증 객체 추출
        Authentication authentication = jwtTokenProvider.getAuthentication(refreshToken);

        // 3. Principal을 AuthMember로 형변환하여 memberId 획득
        AuthMember authMember = (AuthMember) authentication.getPrincipal();
        Long memberId = authMember.getId();

        // 4. DB에서 유저 조회 (findById 사용)
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException(MemberErrorCode.MEMBER_NOT_FOUND));

        // 5. Redis에 저장된 Refresh Token 조회
        String savedToken = redisTemplate.opsForValue().get("RT:"+member.getId());

        // 6. 요청된 토큰과 Redis 저장 토큰 일치 여부 확인
        if (!refreshToken.equals(savedToken)) {
            throw new BusinessException(GlobalErrorCode.INVALID_INPUT_VALUE); // 토큰 불일치 (탈취 가능성 등)
        }

        // 7. 새로운 토큰 발급
        String newAccessToken = jwtTokenProvider.createAccessToken(authMember);
        String newRefreshToken = jwtTokenProvider.createRefreshToken(authMember);

        // 8. Redis 갱신
        redisTemplate.opsForValue().set(
                "RT:"+member.getId(),
                newRefreshToken,
                Duration.ofMillis(604800000) // 7일
        );

        return AuthResult.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .loginResponse(null)
                .build();
    }
    
    //======================//
    //=== 내부 헬퍼 메서드 ===//
    //======================//

    private void register(JoinRequest request, Role role) {

        // ID 중복 확인
        validateUsername(request.getUsername());
        // PW 일치 여부 확인
        validatePassword(request.getPassword(), request.getPasswordConfirm());

        Member joinMember = Member.ofLocal(
                request.getUsername(),
                passwordEncoder.encode(request.getPassword()),
                role,
                Grade.IRON
        );

        memberRepository.save(joinMember);
    }

    private void validateUsername(String username) {
        if (memberRepository.existsByUsername(username)) {
            throw new BusinessException(MemberErrorCode.DUPLICATE_USERNAME);
        }
    }

    private void validatePassword(String password, String passwordConfirm) {
        if (!password.equals(passwordConfirm)) {
            throw new BusinessException(MemberErrorCode.PASSWORD_CONFIRM_MISMATCH);
        }
    }
}

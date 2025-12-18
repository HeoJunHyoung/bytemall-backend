package com.example.bytemallbackend.global.security.oauth.service;

import com.example.bytemallbackend.domain.member.entity.Member;
import com.example.bytemallbackend.domain.member.entity.enumerate.Grade;
import com.example.bytemallbackend.domain.member.entity.enumerate.Role;
import com.example.bytemallbackend.domain.member.repository.MemberRepository;
import com.example.bytemallbackend.global.security.oauth.info.OAuth2UserInfo;
import com.example.bytemallbackend.global.security.oauth.info.OAuth2UserInfoFactory;
import com.example.bytemallbackend.global.security.principal.AuthMember;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        // 1. 소셜 로그인 API에서 사용자 정보 가져오기
        OAuth2User oAuth2User = super.loadUser(userRequest);

        // 2. 어떤 소셜 로그인인지 확인 (google, kakao)
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(registrationId, oAuth2User.getAttributes());

        // 3. 사용자 이름 생성 (중복 방지: provider_providerId)
        String username = oAuth2UserInfo.getProvider() + "_" + oAuth2UserInfo.getProviderId();

        // 4. DB 저장 또는 조회
        Member member = memberRepository.findByUsername(username)
                .orElseGet(() -> createMember(username, oAuth2UserInfo));

        // 5. AuthMember 반환 (AuthenticationPrincipal로 사용)
        return new AuthMember(
                member.getId(),
                member.getUsername(),
                member.getPassword(),
                member.getRole(),
                oAuth2User.getAttributes()
        );
    }

    private Member createMember(String username, OAuth2UserInfo userInfo) {

        // 비밀번호는 소셜 로그인이라 필요 없지만, NOT NULL 제약조건 방어
        String password = passwordEncoder.encode(UUID.randomUUID().toString());

        Member newMember = Member.ofSocial(
                username,
                password,
                Role.CUSTOMER,
                Grade.IRON,
                userInfo.getProvider(),
                userInfo.getProviderId()
        );

        return memberRepository.save(newMember);
    }

}

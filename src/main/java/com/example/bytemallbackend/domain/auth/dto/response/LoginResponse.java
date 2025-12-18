package com.example.bytemallbackend.domain.auth.dto.response;

import com.example.bytemallbackend.domain.member.entity.Member;
import com.example.bytemallbackend.global.security.principal.AuthMember;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginResponse {

    private String memberId;

    private String role;


    public static LoginResponse from(AuthMember authMember) {
        return LoginResponse.builder()
                .memberId(String.valueOf(authMember.getId()))
                .role(String.valueOf(authMember.getRole()))
                .build();
    }

}

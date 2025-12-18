package com.example.bytemallbackend.domain.member.dto.response;

import com.example.bytemallbackend.domain.member.entity.Member;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MemberResponse {

    private Long memberId;

    private String username;

    private String role;

    private String grade;

    public static MemberResponse fromEntity(Member member) {
        return MemberResponse.builder()
                .memberId(member.getId())
                .username(member.getUsername())
                .role(String.valueOf(member.getRole()))
                .grade(String.valueOf(member.getGrade()))
                .build();
    }
}

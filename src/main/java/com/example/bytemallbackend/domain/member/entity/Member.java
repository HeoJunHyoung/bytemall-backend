package com.example.bytemallbackend.domain.member.entity;

import com.example.bytemallbackend.domain.member.entity.enumerate.Grade;
import com.example.bytemallbackend.domain.member.entity.enumerate.Role;
import com.example.bytemallbackend.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
@Table(name = "members")
public class Member extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;    // DB Increment Id

    private String username; // 로그인 ID

    private String password; // 로그인 PW

    @Enumerated(EnumType.STRING)
    private Role role;

    @Enumerated(EnumType.STRING)
    private Grade grade;

    private String provider; // google, kakao
    private String providerId; // sub, id 등 식별자

    protected Member() { }

    private Member(String username, String password, Role role, Grade grade, String provider, String providerId) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.grade = grade;
        this.provider = provider;
        this.providerId = providerId;
    }

    // 로컬(자체) 회원가입: Role과 Grade에 기본값(CUSTOMER, IRON) 주입
    public static Member ofLocal(String username, String password, Role role, Grade grade) {
        return new Member(
                username,
                password,
                role,
                grade,
                null,
                null
        );
    }

    // 소셜 로그인: 외부에서 받은 정보 그대로 주입
    public static Member ofSocial(String username, String password, Role role, Grade grade, String provider, String providerId) {
        return new Member(username,
                password,
                role,
                grade,
                provider,
                providerId);
    }

}

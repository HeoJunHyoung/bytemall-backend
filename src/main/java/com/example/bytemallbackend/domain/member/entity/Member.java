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

    protected Member() { }

    private Member(String username, String password, Role role, Grade grade) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.grade = grade;
    }

    // 로컬(자체) 회원가입: Role과 Grade에 기본값(CUSTOMER, IRON) 주입
    public static Member ofLocal(String username, String password, Role role, Grade grade) {
        return new Member(
                username,
                password,
                role,
                grade
        );
    }

}

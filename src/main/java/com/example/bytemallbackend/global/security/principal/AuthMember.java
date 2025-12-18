package com.example.bytemallbackend.global.security.principal;

import com.example.bytemallbackend.domain.member.entity.enumerate.Role;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@Getter
public class AuthMember implements UserDetails, OAuth2User {

    private Long id;
    private String username;
    private String password;
    private Role role;

    // OAUth2 로그인 시 속성들을 담을 Map
    private Map<String, Object> attributes;

    // 1. 일반 로그인용 생성자
    public AuthMember(Long id, String username, String password, Role role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
    }

    // 2. 소셜 로그인용 생성자
    public AuthMember(Long id, String username, String password, Role role, Map<String, Object> attributes) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
        this.attributes = attributes;
    }

    // JWT 인증용 생성자 (비밀번호 불필요)
    public AuthMember(Long id, Role role) {
        this.id = id;
        this.role = role;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    // -- OAuth2User 구현 --
    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public String getName() {
        return username;
    }

    // -- UserDetails 구현 --
    @Override public String getPassword() { return password; }
    @Override public String getUsername() { return username; }
    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return true; }


}

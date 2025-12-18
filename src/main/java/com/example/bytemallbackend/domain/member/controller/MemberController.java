package com.example.bytemallbackend.domain.member.controller;

import com.example.bytemallbackend.domain.member.dto.response.MemberResponse;
import com.example.bytemallbackend.domain.member.service.MemberService;
import com.example.bytemallbackend.global.security.principal.AuthMember;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members")
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/me")
    public ResponseEntity<MemberResponse> getMyInfo(@AuthenticationPrincipal AuthMember authMember) {
        MemberResponse memberResponse = memberService.getMyInfo(authMember.getId());
        return ResponseEntity.ok(memberResponse);
    }

}

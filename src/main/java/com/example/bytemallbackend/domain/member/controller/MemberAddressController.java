package com.example.bytemallbackend.domain.member.controller;

import com.example.bytemallbackend.domain.member.dto.request.MemberAddressCreateRequest;
import com.example.bytemallbackend.domain.member.dto.response.MemberAddressResponse;
import com.example.bytemallbackend.domain.member.service.MemberAddressService;
import com.example.bytemallbackend.global.security.principal.AuthMember;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members/addresses")
public class MemberAddressController {

    private final MemberAddressService memberAddressService;

    // 내 배송지 목록 조회
    @GetMapping
    public ResponseEntity<List<MemberAddressResponse>> getMyAddresses(@AuthenticationPrincipal AuthMember authMember) {
        return ResponseEntity.ok(memberAddressService.getAddresses(authMember.getId()));
    }

    // 배송지 추가
    @PostMapping
    public ResponseEntity<Void> addAddress(@AuthenticationPrincipal AuthMember authMember, @RequestBody MemberAddressCreateRequest request) {
        memberAddressService.addAddress(authMember.getId(), request);
        return ResponseEntity.ok().build();
    }

    // 배송지 삭제
    @DeleteMapping("/{addressId}")
    public ResponseEntity<Void> deleteAddress(@AuthenticationPrincipal AuthMember authMember, @PathVariable("addressId") Long addressId) {
        memberAddressService.deleteAddress(authMember.getId(), addressId);
        return ResponseEntity.ok().build();
    }
}
package com.example.bytemallbackend.domain.member.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MemberAddressCreateRequest {
    private String addressName;   // 예: 우리집, 회사
    private String recipientName; // 수령인 이름
    private String phoneNumber;   // 연락처
    private String zipcode;       // 우편번호
    private String roadAddress;   // 도로명 주소
    private String detailAddress; // 상세 주소
    private boolean isDefault;    // 기본 배송지 설정 여부
}

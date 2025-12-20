package com.example.bytemallbackend.domain.member.service;

import com.example.bytemallbackend.domain.delivery.entity.Address;
import com.example.bytemallbackend.domain.member.dto.request.MemberAddressCreateRequest;
import com.example.bytemallbackend.domain.member.dto.response.MemberAddressResponse;
import com.example.bytemallbackend.domain.member.entity.Member;
import com.example.bytemallbackend.domain.member.entity.MemberAddress;
import com.example.bytemallbackend.domain.member.exception.MemberErrorCode;
import com.example.bytemallbackend.domain.member.repository.MemberAddressRepository;
import com.example.bytemallbackend.domain.member.repository.MemberRepository;
import com.example.bytemallbackend.global.error.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberAddressService {

    private final MemberAddressRepository memberAddressRepository;
    private final MemberRepository memberRepository;

    // 배송지 추가
    @Transactional
    public void addAddress(Long memberId, MemberAddressCreateRequest request) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException(MemberErrorCode.MEMBER_NOT_FOUND));

        if (request.isDefault()) {
            resetDefaultAddress(memberId);
        }

        boolean isDefault = request.isDefault();
        if (memberAddressRepository.findAllByMemberId(memberId).isEmpty()) {
            isDefault = true;
        }

        MemberAddress memberAddress = MemberAddress.of(
                member,
                request.getAddressName(),
                request.getRecipientName(),
                request.getPhoneNumber(),
                new Address(request.getZipcode(), request.getRoadAddress(), request.getDetailAddress()),
                isDefault
        );

        memberAddressRepository.save(memberAddress);
    }

    // 배송지 삭제
    @Transactional
    public void deleteAddress(Long memberId, Long addressId) {
        MemberAddress address = memberAddressRepository.findById(addressId)
                .orElseThrow(() -> new BusinessException(MemberErrorCode.ADDRESS_NOT_FOUND));

        // 삭제 요청자가 해당 주소의 소유자인지 검증함
        if (!address.getMember().getId().equals(memberId)) {
            throw new BusinessException(MemberErrorCode.MEMBER_UNAUTHORIZED);
        }

        memberAddressRepository.delete(address);
    }

    /**
     * 내 배송지 목록 조회
     */
    public List<MemberAddressResponse> getAddresses(Long memberId) {
        return memberAddressRepository.findAllByMemberId(memberId).stream()
                .map(MemberAddressResponse::from)
                .toList();
    }

    private void resetDefaultAddress(Long memberId) {
        memberAddressRepository.findByMemberIdAndIsDefaultTrue(memberId)
                .ifPresent(addr -> addr.setDefault(false));
    }
}
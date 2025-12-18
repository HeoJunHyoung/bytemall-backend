package com.example.bytemallbackend.domain.member.service;

import com.example.bytemallbackend.domain.member.dto.response.MemberResponse;
import com.example.bytemallbackend.domain.member.entity.Member;
import com.example.bytemallbackend.domain.member.exception.MemberErrorCode;
import com.example.bytemallbackend.domain.member.repository.MemberRepository;
import com.example.bytemallbackend.global.error.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberResponse getMyInfo(Long memberId) {
        Member findMember = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException(MemberErrorCode.MEMBER_NOT_FOUND));
        return MemberResponse.fromEntity(findMember);
    }

}

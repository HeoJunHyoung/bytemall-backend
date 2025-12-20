package com.example.bytemallbackend.domain.member.repository;

import com.example.bytemallbackend.domain.member.entity.MemberAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface MemberAddressRepository extends JpaRepository<MemberAddress, Long> {

    List<MemberAddress> findAllByMemberId(Long memberId);

    Optional<MemberAddress> findByMemberIdAndIsDefaultTrue(Long memberId);

}
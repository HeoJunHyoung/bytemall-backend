package com.example.bytemallbackend.domain.member.entity;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Table(name = "seller_profile")
@Getter
public class SellerProfile {

    @Id @Column(name = "member_id") // PK이자 FK
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId // Member의 PK를 자신의 PK로 가져와서 사용 (1:1 매핑관계에서 사용)
    @JoinColumn(name = "member_id")
    private Member member;

    private String businessNumber; // 사업자 번호
    private String bankAccount;    // 정산 계좌

    protected SellerProfile() { }

    private SellerProfile(Member member, String businessNumber, String bankAccount) {
        this.member = member;
        this.businessNumber = businessNumber;
        this.bankAccount = bankAccount;
    }

    public static SellerProfile of(Member member, String businessNumber, String bankAccount) {
        return new SellerProfile(member, businessNumber, bankAccount);
    }

}

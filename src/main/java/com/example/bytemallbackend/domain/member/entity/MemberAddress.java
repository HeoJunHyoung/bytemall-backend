package com.example.bytemallbackend.domain.member.entity;

import com.example.bytemallbackend.domain.delivery.entity.Address;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Table(name = "member_address")
@Getter
public class MemberAddress {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_address_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private String addressName; // 예: 우리집, 사무실
    private String recipientName; // 수령인
    private String phoneNumber;

    @Embedded
    private Address address;

    @Column(name = "is_default")
    private boolean isDefault;

    // 생성자
    protected MemberAddress() { }

    private MemberAddress(Member member, String addressName, String recipientName, String phoneNumber, Address address, boolean isDefault) {
        this.member = member;
        this.addressName = addressName;
        this.recipientName = recipientName;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.isDefault = isDefault;
    }

    public static MemberAddress of(Member member, String addressName, String recipientName, String phoneNumber, Address address, boolean isDefault) {
        return new MemberAddress(member, addressName, recipientName, phoneNumber, address, isDefault);
    }

    public void setDefault(boolean isDefault) {
        this.isDefault = isDefault;
    }
}

package com.example.bytemallbackend.domain.member.dto.response;

import com.example.bytemallbackend.domain.member.entity.MemberAddress;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MemberAddressResponse {
    private Long id;
    private String addressName;
    private String recipientName;
    private String phoneNumber;
    private String zipcode;
    private String roadAddress;
    private String detailAddress;
    @JsonProperty("isDefault")
    private boolean isDefault;

    public MemberAddressResponse(Long id, String addressName, String recipientName,
                                 String phoneNumber, String zipcode, String roadAddress,
                                 String detailAddress, boolean isDefault) {
        this.id = id;
        this.addressName = addressName;
        this.recipientName = recipientName;
        this.phoneNumber = phoneNumber;
        this.zipcode = zipcode;
        this.roadAddress = roadAddress;
        this.detailAddress = detailAddress;
        this.isDefault = isDefault;
    }

    public static MemberAddressResponse from(MemberAddress entity) {
        return new MemberAddressResponse(
                entity.getId(),
                entity.getAddressName(),
                entity.getRecipientName(),
                entity.getPhoneNumber(),
                entity.getAddress().getZipcode(),
                entity.getAddress().getRoadAddress(),
                entity.getAddress().getDetailAddress(),
                entity.isDefault()
        );
    }
}

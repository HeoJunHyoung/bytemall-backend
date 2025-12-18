package com.example.bytemallbackend.global.security.oauth.info;

import com.example.bytemallbackend.global.error.BusinessException;
import com.example.bytemallbackend.global.error.GlobalErrorCode;
import com.example.bytemallbackend.global.security.oauth.info.impl.GoogleOAuth2UserInfo;
import com.example.bytemallbackend.global.security.oauth.info.impl.KakaoOAuth2UserInfo;

import java.util.Map;

public class OAuth2UserInfoFactory {

    public static OAuth2UserInfo getOAuth2UserInfo(String registrationId, Map<String, Object> attributes) {
        if (registrationId.equalsIgnoreCase("google")) {
            return new GoogleOAuth2UserInfo(attributes);
        } else if (registrationId.equalsIgnoreCase("kakao")) {
            return new KakaoOAuth2UserInfo(attributes);
        } else {
            throw new BusinessException(GlobalErrorCode.INVALID_INPUT_VALUE);
        }
    }

}
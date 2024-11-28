package com.woohahaapps.study.diary.domain;

import java.util.HashMap;
import java.util.Map;

public class KakaoOAuth2UserInfo implements OAuth2UserInfo{

    private final Map<String, Object> attributes;

    public KakaoOAuth2UserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    @Override
    public String getProvider() {
        return "kakao";
    }

    @Override
    public String getProviderId() {
        return String.valueOf(attributes.get("id"));
    }

    @Override
    public String getEmail() {
        return "";
    }

    @Override
    public String getName() {
        return (String) ((Map)((Map)attributes.get("kakao_account")).get("profile")).get("nickname");
    }

    @Override
    public String getProfileImageUrl() {
        return (String) ((Map)((Map)attributes.get("kakao_account")).get("profile")).get("profile_image_url");
    }

    @Override
    public Map<String, Object> convertToMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("provider", getProvider());
        map.put("providerid", getProviderId());
        map.put("email", getEmail());
        map.put("name", getName());
        map.put("profileimageurl", getProfileImageUrl());
        return map;
    }
}

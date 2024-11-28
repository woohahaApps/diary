package com.woohahaapps.study.diary.domain;

import java.util.HashMap;
import java.util.Map;

public class NaverOAuth2UserInfo implements OAuth2UserInfo {

    private final Map<String, Object> attributes;

    public NaverOAuth2UserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    @Override
    public String getProvider() {
        return "naver";
    }

    @Override
    public String getProviderId() {
        return (String)attributes.get("id");
    }

    @Override
    public String getEmail() {
        return "";
    }

    @Override
    public String getName() {
        return (String)attributes.get("name");
    }

    @Override
    public String getProfileImageUrl() {
        return (String)attributes.get("profile_image");
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
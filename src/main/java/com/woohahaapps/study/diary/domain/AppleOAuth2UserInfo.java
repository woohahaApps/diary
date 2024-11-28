package com.woohahaapps.study.diary.domain;

import java.util.HashMap;
import java.util.Map;

public class AppleOAuth2UserInfo implements OAuth2UserInfo {
    private final Map<String, Object> attributes;

    public AppleOAuth2UserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    @Override
    public String getProvider() {
        return "apple";
    }

    @Override
    public String getProviderId() {
        return (String) attributes.get("sub");
    }

    @Override
    public String getEmail() {
        return (String) attributes.get("email");
    }

    @Override
    public String getName() {
        return "";
    }

    @Override
    public String getProfileImageUrl() {
        return "";
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

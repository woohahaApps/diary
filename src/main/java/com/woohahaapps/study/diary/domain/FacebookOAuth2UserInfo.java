package com.woohahaapps.study.diary.domain;

import java.util.HashMap;
import java.util.Map;

public class FacebookOAuth2UserInfo implements OAuth2UserInfo {
    private final Map<String, Object> attributes;

    public FacebookOAuth2UserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    @Override
    public String getProvider() {
        return "facebook";
    }

    @Override
    public String getProviderId() {
        return (String) attributes.get("id");
    }

    @Override
    public String getEmail() {
        return (String) attributes.get("email");
    }

    @Override
    public String getName() {
        return (String) attributes.get("name");
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

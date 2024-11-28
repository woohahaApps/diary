package com.woohahaapps.study.diary.domain;

import java.util.Map;

public interface OAuth2UserInfo {
    String getProvider();
    String getProviderId();
    String getEmail();
    String getName();
    String getProfileImageUrl();

    Map<String, Object> convertToMap();
}

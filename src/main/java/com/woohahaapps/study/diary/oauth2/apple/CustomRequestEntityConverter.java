package com.woohahaapps.study.diary.oauth2.apple;

import com.woohahaapps.study.diary.service.AppleClientSecretService;
import org.springframework.http.RequestEntity;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequestEntityConverter;
import org.springframework.util.MultiValueMap;

public class CustomRequestEntityConverter extends OAuth2AuthorizationCodeGrantRequestEntityConverter {

    // AppleClientSecretService 의존성 주입
    private final AppleClientSecretService appleClientSecretService;

    public CustomRequestEntityConverter(AppleClientSecretService appleClientSecretService) {
        this.appleClientSecretService = appleClientSecretService;
    }

    @Override
    public RequestEntity<?> convert(OAuth2AuthorizationCodeGrantRequest request) {
        // 기존 요청을 생성
        RequestEntity<?> originalRequest = super.convert(request);

        String registrationId = request.getClientRegistration().getRegistrationId();
        if (registrationId.equals("apple")) {
            MultiValueMap<String, String> body = (MultiValueMap<String, String>) originalRequest.getBody();
            // client_secret 채우기
            body.set("client_secret", appleClientSecretService.generateClientSecret());

            return RequestEntity
                    .post(request.getClientRegistration().getProviderDetails().getTokenUri())
                    .body(body);
        }

        return originalRequest;
    }

}

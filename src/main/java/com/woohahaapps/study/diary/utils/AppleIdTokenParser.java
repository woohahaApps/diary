package com.woohahaapps.study.diary.utils;

import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

import java.text.ParseException;
import java.util.Map;

public class AppleIdTokenParser {
    /**
     * Apple의 id_token을 파싱하고 클레임을 추출합니다.
     *
     * @param idToken Apple 서버에서 반환된 id_token
     * @return 사용자 정보가 포함된 클레임
     */
    public static Map<String, Object> parseIdToken(String idToken) {
        try {
            // id_token 파싱
            SignedJWT signedJWT = SignedJWT.parse(idToken);

            // JWT의 Claims 추출
            JWTClaimsSet claims = signedJWT.getJWTClaimsSet();

            // Claims를 Map 형태로 반환
            return claims.getClaims();
        } catch (ParseException e) {
            throw new IllegalArgumentException("Failed to parse id_token", e);
        }
    }
}

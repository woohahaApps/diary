package com.woohahaapps.study.diary.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;
import java.util.Date;

@Service
public class AppleClientSecretService {
    private final String teamId = "82YP86DL7C";
    private final String clientId = "com.woohahaapps.diary.signin";
    private final String keyId = "LCN8P6YW4C";

    public String generateClientSecret() {
        try {
            // Private Key 로드
            PrivateKey privateKey = privateKey();
            // JWT 생성
            return Jwts.builder()
                    .setHeaderParam("kid", "LCN8P6YW4C") // Key ID
                    .setIssuer("82YP86DL7C") // Team ID
                    .setAudience("https://appleid.apple.com") // Apple 고정 값
                    .setSubject("com.woohahaapps.diary.signin") // Client ID
                    .setExpiration(new Date(System.currentTimeMillis() + 3600 * 1000)) // 만료 시간
                    .signWith(SignatureAlgorithm.ES256, (java.security.interfaces.ECPrivateKey) privateKey)
                    .compact();
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate Apple client secret", e);
        }
    }

    private PrivateKey privateKey() {
        try {
            // Apple에서 발급한 .p8 파일 경로
//            String path = "src/file/AuthKey_DiarySignInKey_82YP86DL7C_LCN8P6YW4C.p8";
//            String path = "/var/webapp/diary/AuthKey_DiarySignInKey_82YP86DL7C_LCN8P6YW4C.p8";
            String path = "/var/data/AuthKey.p8";
            System.out.println("Loading private key from: " + path);

            // 키 파일 읽기
            String privateKeyContent = Files.readString(Paths.get(path));
            System.out.println("Raw private key content: " + privateKeyContent);

            // BEGIN/END 제거 및 Base64 디코딩
            privateKeyContent = privateKeyContent
                    .replaceAll("\\n", "")
                    .replace("-----BEGIN PRIVATE KEY-----", "")
                    .replace("-----END PRIVATE KEY-----", "");
            byte[] keyBytes = Base64.getDecoder().decode(privateKeyContent);

            // Elliptic Curve (EC) KeyFactory 사용
            KeyFactory keyFactory = KeyFactory.getInstance("EC");
            return keyFactory.generatePrivate(new PKCS8EncodedKeySpec(keyBytes));
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to load private key", e);
        }
    }
}

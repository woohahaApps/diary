package com.woohahaapps.study.diary.service;

import com.woohahaapps.study.diary.domain.*;
import com.woohahaapps.study.diary.mapper.MemberMapper;
import com.woohahaapps.study.diary.mapper.UsersMapper;
import com.woohahaapps.study.diary.utils.AppleIdTokenParser;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final MemberMapper memberMapper;
    private final JavaMailSender mailSender;
    private final UsersMapper usersMapper;

    public CustomOAuth2UserService(MemberMapper memberMapper, JavaMailSender mailSender, UsersMapper usersMapper) {
        this.memberMapper = memberMapper;
        this.mailSender = mailSender;
        this.usersMapper = usersMapper;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        String provider = userRequest.getClientRegistration().getRegistrationId();
        System.out.printf("provider = [%s]%n", provider);
        // 각 Provider에 따라 nameAttributeKey 설정
        String nameAttributeKey = userRequest.getClientRegistration()
                .getProviderDetails()
                .getUserInfoEndpoint()
                .getUserNameAttributeName();

        OAuth2User oAuth2User = null;
        OAuth2UserInfo oAuth2UserInfo = null;

        if (provider.equals("apple")) {
            Map<String, Object> claims = userRequest.getAdditionalParameters();
            if (claims.containsKey("id_token")) {
                String idToken = (String) claims.get("id_token");
                Map<String, Object> extractedUserInfo = new HashMap<>(extractUserInfoFromIdToken(idToken));
                System.out.println("Extracted User Info: " + extractedUserInfo);
                if ((Boolean) extractedUserInfo.get("is_private_email"))
                    extractedUserInfo.remove("email");
                oAuth2UserInfo = new AppleOAuth2UserInfo(extractedUserInfo);

                // `oAuth2User`를 수동으로 생성
                Collection<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_USER"));
                nameAttributeKey = "sub";
                oAuth2User = new DefaultOAuth2User(authorities, extractedUserInfo, nameAttributeKey);
            }
        } else {
            oAuth2User = super.loadUser(userRequest);
            if (provider.equals("google")) {
                oAuth2UserInfo = new GoogleOAuth2UserInfo(oAuth2User.getAttributes());
            } else if (provider.equals("naver")) {
                oAuth2UserInfo = new NaverOAuth2UserInfo(oAuth2User.getAttribute("response"));
            } else if (provider.equals("kakao")) {
                oAuth2UserInfo = new KakaoOAuth2UserInfo(oAuth2User.getAttributes());
            } else if (provider.equals("facebook")) {
                oAuth2UserInfo = new FacebookOAuth2UserInfo(oAuth2User.getAttributes());
            }
        }
        String email = oAuth2UserInfo.getEmail();
        String name = oAuth2UserInfo.getName();
        String provider_user_id = oAuth2UserInfo.getProviderId();
        String profile_image_url = oAuth2UserInfo.getProfileImageUrl();

        Optional<Users> users = usersMapper.GetUsers(provider_user_id, provider);
        if (users.isEmpty()) { // users 테이블에 OAuth2 인증정보가 존재하지 않음
            Users newUsers = new Users();
            newUsers.setProvider(provider);
            newUsers.setProvider_user_id(provider_user_id);
            newUsers.setEmail(email);
            newUsers.setName(name);
            newUsers.setProfile_image_url(profile_image_url);

            Integer affectedCount = usersMapper.CreateUsers(newUsers);
            if (affectedCount < 1) {
                throw new RuntimeException(String.format("""
                                소셜 로그인 인증정보 저장오류: 
                                provider=%s
                                , provider_user_id=%s
                                , email=%s
                                , name=%s
                                , profile_image_url=%s
                                """
                                , provider, provider_user_id, email, name, profile_image_url
                ));
            }
            // 소셜 로그인 인증정보 저장 성공
            users = usersMapper.GetUsers(provider_user_id, provider);
        }

        // 기존 attributes를 복사하여 새로운 HashMap 생성
        Map<String, Object> attributes = new HashMap<>(oAuth2User.getAttributes());

        // usersAttributes 추가
        Map<String, Object> usersAttributes = oAuth2UserInfo.convertToMap();
        usersAttributes.put("uid", users.get().getId());

        attributes.put("users", usersAttributes);

        Collection<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_USER"));

        return new DefaultOAuth2User(authorities, attributes, nameAttributeKey);
    }

    private Map<String, Object> extractUserInfoFromIdToken(String idToken) {
        // Nimbus JWT 라이브러리로 id_token을 파싱 및 사용자 정보 추출
        // (예: name, email 등)
        return AppleIdTokenParser.parseIdToken(idToken); // 사용자 정보 반환
    }
}

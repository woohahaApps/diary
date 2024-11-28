package com.woohahaapps.study.diary.oauth2.handler;

import com.woohahaapps.study.diary.domain.Member;
import com.woohahaapps.study.diary.jwt.JwtUtil;
import com.woohahaapps.study.diary.mapper.MemberMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Optional;

@Component
public class CustomOAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtUtil jwtUtil;
    private final MemberMapper memberMapper;

    public CustomOAuth2LoginSuccessHandler(JwtUtil jwtUtil, MemberMapper memberMapper) {
        this.jwtUtil = jwtUtil;
        this.memberMapper = memberMapper;
    }

    private void createJwtCookieAndRedirect(HttpServletResponse response, Authentication authentication) throws IOException {
        // JWT 생성
        String jwtToken = jwtUtil.createToken(authentication);
        System.out.println("Generated JWT Token: " + jwtToken);

        // 쿠키 설정
        Cookie cookie = new Cookie("Authorization", "Bearer=" + jwtToken);
        cookie.setMaxAge(24 * 60 * 60); // 1일 유효
        cookie.setPath("/");
        cookie.setHttpOnly(true);

        response.addCookie(cookie);

        // 홈으로 리디렉션 설정
        setDefaultTargetUrl("/");
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        System.out.println("authentication = [" + authentication + "]");
        DefaultOAuth2User user = (DefaultOAuth2User) authentication.getPrincipal();
        Map<String, Object> attributes = user.getAttributes();

        // 소셜 로그인 사용자 정보 확인
        Map<String, Object> usersAttributes = (Map<String, Object>) attributes.get("users");
        String provider = (String) usersAttributes.get("provider");
        String email = (String) usersAttributes.get("email");
        String name = (String) usersAttributes.get("name");
        String uidStr = String.valueOf(usersAttributes.get("uid"));
        Long uid = Long.valueOf(uidStr);
//        String password = email + name + uidStr;

        // 최종적으로 구할 데이터는 member 테이블의 값이다.
        // member 테이블에서 uid 컬럼값을 조회
        Optional<Member> member = memberMapper.GetMemberByUserId(uid);
        if (member.isPresent()) {// 조회 성공. 인증정보 수정
            // JWT 생성 및 쿠키 설정
            createJwtCookieAndRedirect(response, jwtUtil.createAuthentication(member.get().getEmail()));
        } else {// member 테이블에 uid 값으로 등록된 레코드가 존재하지 않는다.
            if (email == null || email.isEmpty()) {// 이메일 주소 정보가 존재하지 않는다.
                // 이메일 입력 페이지로 리디렉션
                String targetUrl = String.format("/register-email?uid=%d&provider=%s&name=%s"
                        , uid, provider, URLEncoder.encode(name, StandardCharsets.UTF_8));
                setDefaultTargetUrl(targetUrl);
            } else {// 이메일 주소 정보가 존재하므로 레코드를 추가하고 인증 처리하면 된다.
                // 동일한 이메일 주소가 이미 존재하는가를 확인한다.
                member = memberMapper.GetMember(email);
                if (member.isPresent()) {// uid 를 업데이트한다.
                    member.get().setUid(uid);
                    member.get().setProvider(provider);
                    memberMapper.UpdateMember(member.get());
                } else {
                    // member 테이블에 신규 회원 추가
                    Member newMember = new Member();
                    newMember.setEmail(email);
                    newMember.setName(name);
//                    newMember.setPassword(passwordEncoder.encode(password));
                    newMember.setUid(uid);
                    newMember.setProvider(provider);
                    newMember.setRole("ROLE_USER");
                    memberMapper.CreateMember(newMember);
                }
                // 인증처리하여 JWT 발급 및 쿠키 설정
                createJwtCookieAndRedirect(response, jwtUtil.createAuthentication(email));
            }
        }

        super.onAuthenticationSuccess(request, response, authentication);
    }
}

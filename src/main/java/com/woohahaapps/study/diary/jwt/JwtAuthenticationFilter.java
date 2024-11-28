package com.woohahaapps.study.diary.jwt;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

//    private final JwtTokenProvider jwtTokenProvider;
    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String requestURI = request.getRequestURI();

        // 이미 인증된 상태라면 필터를 건너뛰기
        if (SecurityContextHolder.getContext().getAuthentication() != null &&
                SecurityContextHolder.getContext().getAuthentication().isAuthenticated()) {
            filterChain.doFilter(request, response);
            return;
        }

        // 폼 로그인과 OAuth2 로그인 경로 예외 처리
        if (requestURI.startsWith("/process_login") ||
                requestURI.startsWith("/oauth2/") ||
                requestURI.startsWith("/login/oauth2/code/") ||
//                requestURI.startsWith("/login") ||
                requestURI.startsWith("/favicon.ico")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 1. Request Header 또는 쿠키에서 JWT 토큰 추출
        String token = jwtUtil.resolveToken(request);
        System.out.println(request.getRequestURI());

        // 2. validateToken으로 토큰 유효성 검사
        if (token != null && jwtUtil.validateToken(token)) {
            // 토큰이 유효할 경우 토큰에서 Authentication 객체를 가지고 와서 SecurityContext에 저장
            Claims info = jwtUtil.getUserInfoFromToken(token);
            setAuthentication(info.getSubject());
        }

        filterChain.doFilter(request, response);
    }

    public void setAuthentication(String username) {
        /*jwt 인증 성공 시*/
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = jwtUtil.createAuthentication(username);
        context.setAuthentication(authentication);

        SecurityContextHolder.setContext(context);
    }
}

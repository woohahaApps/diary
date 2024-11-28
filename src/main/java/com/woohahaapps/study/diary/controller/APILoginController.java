package com.woohahaapps.study.diary.controller;

import com.woohahaapps.study.diary.domain.LoginBody;
import com.woohahaapps.study.diary.domain.LoginResult;
import com.woohahaapps.study.diary.jwt.JwtUtil;
import com.woohahaapps.study.diary.service.LoginService;
import jakarta.servlet.http.Cookie;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/login")
@Slf4j
public class APILoginController {

    private final LoginService loginService;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtUtil jwtUtil;

    @Autowired
    public APILoginController(LoginService loginService, AuthenticationManagerBuilder authenticationManagerBuilder, JwtUtil jwtUtil) {
        this.loginService = loginService;
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping()
    public ResponseEntity<?> login(@RequestBody LoginBody loginBody) {
        log.debug(String.valueOf(loginBody));
        try {
            UserDetails userDetails = loginService.loadUserByUsername(loginBody.getEmail());

            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                    new UsernamePasswordAuthenticationToken(userDetails, loginBody.getPassword(), userDetails.getAuthorities());
            // 2. 인증 가능 여부 확인(패스워드 일치여부 확인)
            Authentication authentication = authenticationManagerBuilder.getObject().authenticate(usernamePasswordAuthenticationToken);
            System.out.println(authentication);

            // 사용자 인증에 성공하면 쿠키로 jwt 토큰을 발급한다.
            String jwtToken = jwtUtil.createToken(authentication);
            System.out.println(jwtToken);

            LoginResult result = LoginResult.builder()
                    .body(jwtToken)
                    .errorCode(0)
                    .build();

            return ResponseEntity.ok().body(result);
        } catch (UsernameNotFoundException e) {

            LoginResult result = LoginResult.builder()
                    .errorCode(400)
                    .errorMessage("Invalid login info")
                    .build();
            return ResponseEntity.status(400).body(result);
        }
    }

//    @GetMapping()
//    public ResponseEntity<?> loginQuery(@RequestParam("email") String email, @RequestParam("password") String password) {
//        log.debug("email=" + email + ",password=" + password);
//        try {
//            UserDetails userDetails = loginService.loadUserByUsername(email);
//
//            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
//                    new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());
//            // 2. 인증 가능 여부 확인(패스워드 일치여부 확인)
//            Authentication authentication = authenticationManagerBuilder.getObject().authenticate(usernamePasswordAuthenticationToken);
//            System.out.println(authentication);
//
//            // 사용자 인증에 성공하면 쿠키로 jwt 토큰을 발급한다.
//            String jwtToken = jwtUtil.createToken(authentication);
//            System.out.println(jwtToken);
//
//            LoginResult result = LoginResult.builder()
//                    .body(jwtToken)
//                    .errorCode(0)
//                    .build();
//
//            return ResponseEntity.ok().body(result);
//        } catch (UsernameNotFoundException e) {
//
//            LoginResult result = LoginResult.builder()
//                    .errorCode(400)
//                    .errorMessage("Invalid login info")
//                    .build();
//            return ResponseEntity.status(400).body(result);
//        }
//    }
}

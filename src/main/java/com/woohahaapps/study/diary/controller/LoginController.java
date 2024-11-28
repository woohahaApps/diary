package com.woohahaapps.study.diary.controller;

import com.woohahaapps.study.diary.domain.Member;
import com.woohahaapps.study.diary.domain.User;
import com.woohahaapps.study.diary.exception.MemberException;
import com.woohahaapps.study.diary.jwt.JwtUtil;
import com.woohahaapps.study.diary.mapper.MemberMapper;
import com.woohahaapps.study.diary.service.LoginService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Controller
public class LoginController {

    private final LoginService loginService;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtUtil jwtUtil;
    private final MemberMapper memberMapper;

    public LoginController(LoginService loginService, AuthenticationManagerBuilder authenticationManagerBuilder, JwtUtil jwtUtil, MemberMapper memberMapper) {
        this.loginService = loginService;
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.jwtUtil = jwtUtil;
        this.memberMapper = memberMapper;
    }

    @GetMapping("/login")
    public String login(@RequestParam(value="error", required = false) String error
            , @RequestParam(value="message", required = false) String message
            , @AuthenticationPrincipal User user
//            , @AuthenticationPrincipal Object user // Object 타입으로 받아 다양한 사용자 타입 처리
            , Model model) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println(authentication);

        // 로그인된 상태라면 home 으로 이동 : return "redirect:/";
        // anonymousUser가 아닌 경우 홈으로 리디렉션
        if (authentication != null && authentication.isAuthenticated() && !"anonymousUser".equals(authentication.getPrincipal())) {
            System.out.println("[REDIRECT] LoginController. /login. redirect:/");
            return "redirect:/";
        }
        model.addAttribute("error", error);
        model.addAttribute("message", message);
        return "login";
    }

    @PostMapping("/signin")
    public String signIn(String email, String password, HttpServletResponse response) {
        System.out.println("email=" + email);
        System.out.println("password=" + password);

        try {
            // 입력받은 로그인 정보로 사용자 정보를 확인하여 인증한다.
            // 1. 사용자정보 존재여부 확인
            UserDetails userDetails = loginService.loadUserByUsername(email);

            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                    new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());

            // 2. 인증 가능 여부 확인(패스워드 일치여부 확인)
            Authentication authentication = authenticationManagerBuilder.getObject().authenticate(usernamePasswordAuthenticationToken);
            System.out.println(authentication);

            // 사용자 인증에 성공하면 쿠키로 jwt 토큰을 발급한다.
            String jwtToken = jwtUtil.createToken(authentication);
            System.out.println(jwtToken);

            Cookie cookie = new Cookie("Authorization", "Bearer=" + jwtToken);
            cookie.setMaxAge(24 * 60 * 60);// 1일동안 유효
            cookie.setPath("/");
            cookie.setHttpOnly(true);

            response.addCookie(cookie);
        } catch (UsernameNotFoundException e) {
            String errorMessage = "아이디 또는 비밀번호가 맞지 않습니다. 다시 확인해 주세요.";
            errorMessage = URLEncoder.encode(errorMessage, StandardCharsets.UTF_8); /* 한글 인코딩 깨진 문제 방지 */
            System.out.println("[REDIRECT] LoginController.UsernameNotFoundException /signin. redirect:/login");
            return "redirect:/login?error=true&message="+errorMessage;
        } catch (BadCredentialsException e) {
            String errorMessage = "아이디 또는 비밀번호가 맞지 않습니다. 다시 확인해 주세요.";
            errorMessage = URLEncoder.encode(errorMessage, StandardCharsets.UTF_8); /* 한글 인코딩 깨진 문제 방지 */
            System.out.println("[REDIRECT] LoginController.BadCredentialsException /signin. redirect:/login");
            return "redirect:/login?error=true&message="+errorMessage;
        }

        System.out.println("[REDIRECT] LoginController. /signin. redirect:/login");
        return "redirect:/login";
    }

    // JWT Test
//    @PostMapping("/logout")
//    public String logout(HttpServletRequest request, HttpServletResponse response) {
//        System.out.println("/logout url handler");
//        new SecurityContextLogoutHandler().logout(request, response, SecurityContextHolder.getContext().getAuthentication());
//        Cookie[] cookies = request.getCookies();
//        for (Cookie c : cookies) {
//            Cookie cookie = new Cookie(c.getName(), null);
//            cookie.setMaxAge(0);
//            response.addCookie(cookie);
//        }
//        return "redirect:/login";
//    }

//    @GetMapping("/logout")
//    public String logout(HttpServletRequest request, HttpServletResponse response) {
//        new SecurityContextLogoutHandler().logout(request, response, SecurityContextHolder.getContext().getAuthentication());
//        Cookie[] cookies = request.getCookies();
//        for (Cookie c : cookies) {
//            Cookie cookie = new Cookie(c.getName(), null);
//            cookie.setMaxAge(0);
//            response.addCookie(cookie);
//        }
//        return "redirect:/login";
//    }
    
    @GetMapping("/logoutdone")
    public String logoutdone(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("/logoutdone url handler. 로그아웃 성공");
        new SecurityContextLogoutHandler().logout(request, response, SecurityContextHolder.getContext().getAuthentication());
        Cookie[] cookies = request.getCookies();
        for (Cookie c : cookies) {
            Cookie cookie = new Cookie(c.getName(), null);
            cookie.setMaxAge(0);
            response.addCookie(cookie);
        }
        System.out.println("[REDIRECT] LoginController. /logoutdone. redirect:/");
        return "redirect:/";
    }

//    @GetMapping("/logout")
//    public String logout(HttpServletRequest request, HttpServletResponse response) {
//        System.out.println("/logout url handler");
//        new SecurityContextLogoutHandler().logout(request, response, SecurityContextHolder.getContext().getAuthentication());
//        Cookie[] cookies = request.getCookies();
//        for (Cookie c : cookies) {
//            Cookie cookie = new Cookie(c.getName(), null);
//            cookie.setMaxAge(0);
//            response.addCookie(cookie);
//        }
//        return "redirect:/login";
//    }

    @GetMapping("/register-email")
    public String registerEmail(@RequestParam(value="error", required = false) String error
                                , @RequestParam(value="message", required = false) String message
                                , @RequestParam("uid") String uid
                                , @RequestParam("provider") String provider
                                , @RequestParam("name") String name
                                , Model model
    ) {
        // uid, provider, name 을 모델에 추가하여 뷰에 전달
        Member member = new Member();
        member.setProvider(provider);
        member.setUid(Long.parseLong(uid));
        member.setName(URLDecoder.decode(name, StandardCharsets.UTF_8));
        model.addAttribute("member", member);

        model.addAttribute("error", error);
        model.addAttribute("message", message);

        return "register-email"; // templates/register-email.html 반환
    }

    @PostMapping("/api/register-email")
    public String registerEmail(@Valid Member member, BindingResult result, HttpServletResponse response, Model model) {

        if (result.hasErrors()) {
            String targetUrl = String.format("/register-email?uid=%d&provider=%s&name=%s"
                    , member.getUid(), member.getProvider(), URLEncoder.encode(member.getName(), StandardCharsets.UTF_8));
            //return "redirect:" + targetUrl;
            return "register-email"; // templates/register-email.html 반환
        }

        // 이메일 저장 로직
        try {
            member.setRole("ROLE_USER");
            memberMapper.CreateMember(member);

            // 인증처리하여 jwt 토큰을 발급
            return createJwtCookieAndRedirect(response, jwtUtil.createAuthentication(member.getEmail()));
        } catch (MemberException ex) {
            String errorMessage = ex.getMessage();
            errorMessage = URLEncoder.encode(errorMessage, StandardCharsets.UTF_8); /* 한글 인코딩 깨진 문제 방지 */
            String targetUrl = String.format("/register-email?uid=%d&provider=%s&name=%s&error=true&message=%s"
                    , member.getUid(), member.getProvider(), URLEncoder.encode(member.getName(), StandardCharsets.UTF_8)
                    , errorMessage);
            return "redirect:" + targetUrl;
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            System.out.println(e.getClass().getName());// org.springframework.dao.DuplicateKeyException
            if (e.getClass().getName().toLowerCase().contains("duplicatekey")){
                String errorMessage = "이메일주소가 이미 등록되어 있습니다: " + member.getEmail();
                errorMessage = URLEncoder.encode(errorMessage, StandardCharsets.UTF_8); /* 한글 인코딩 깨진 문제 방지 */
                String targetUrl = String.format("/register-email?uid=%d&provider=%s&name=%s&error=true&message=%s"
                        , member.getUid(), member.getProvider(), URLEncoder.encode(member.getName(), StandardCharsets.UTF_8)
                        , errorMessage);
                return "redirect:" + targetUrl;
            } else {
                throw new MemberException(e.getMessage());
            }
        }
    }

    private String createJwtCookieAndRedirect(HttpServletResponse response, Authentication authentication) throws IOException {
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
        return "redirect:/";
    }
}

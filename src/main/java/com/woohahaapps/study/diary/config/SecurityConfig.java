package com.woohahaapps.study.diary.config;

import com.woohahaapps.study.diary.jwt.JwtAuthenticationFilter;
import com.woohahaapps.study.diary.jwt.JwtUtil;
import com.woohahaapps.study.diary.oauth2.apple.CustomRequestEntityConverter;
import com.woohahaapps.study.diary.oauth2.handler.CustomOAuth2LoginSuccessHandler;
import com.woohahaapps.study.diary.service.AppleClientSecretService;
import com.woohahaapps.study.diary.service.CustomOAuth2UserService;
import com.woohahaapps.study.diary.service.LoginService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.endpoint.DefaultAuthorizationCodeTokenResponseClient;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CustomAuthFailureHandler customAuthFailureHandler;
    private final LoginService loginService;
//    private final JwtTokenProvider jwtTokenProvider;
    private final JwtUtil jwtUtil;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final CustomOAuth2LoginSuccessHandler customOAuth2LoginSuccessHandler;

    public SecurityConfig(CustomAuthFailureHandler customAuthFailureHandler, LoginService loginService, JwtUtil jwtUtil, CustomOAuth2UserService customOAuth2UserService, CustomOAuth2LoginSuccessHandler customOAuth2LoginSuccessHandler) {
        this.customAuthFailureHandler = customAuthFailureHandler;
        this.loginService = loginService;
//        this.jwtTokenProvider = jwtTokenProvider;
        this.jwtUtil = jwtUtil;
        this.customOAuth2UserService = customOAuth2UserService;
        this.customOAuth2LoginSuccessHandler = customOAuth2LoginSuccessHandler;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity, AppleClientSecretService appleClientSecretService) throws Exception {
        return httpSecurity
//                .csrf((csrf) -> csrf
//                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())// swagger 에서는 PUT, POST, DELETE method 를 위해서 필수(필수아님)로 기록해야 한다.
//                        .csrfTokenRequestHandler(new CsrfTokenRequestAttributeHandler())
//                )
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(
                                "/swagger-ui.html" // swagger
                                , "/swagger-ui/**" // swagger
                                , "/v3/api-docs/**" // swagger
                        ).hasRole("SWAGGER")
                        .requestMatchers("/login", "/process_login", "/signin"
                                , "/signup"
                                , "/api/**"
                                , "/images/**"
                                , "/register-email"
//                                , "/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**"
//                                , "/logoutdone"
                        ).permitAll()
                        //.requestMatchers("/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**").hasRole("USER")
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/process_login")
                        .usernameParameter("email")
                        .failureHandler(customAuthFailureHandler)
                        .permitAll()
                )
                .logout(logout -> logout
                        //.logoutUrl("/logout")
                        .logoutSuccessUrl("/logoutdone")
                )
                // OAuth2
//                .oauth2Login(Customizer.withDefaults())
                .oauth2Login(oauth2Login ->
                        oauth2Login
                                .loginPage("/login")
                                .tokenEndpoint(token -> token
                                        .accessTokenResponseClient(customAccessTokenResponseClient(appleClientSecretService))
                                )
                                .userInfoEndpoint(userInfo -> userInfo
                                        .userService(customOAuth2UserService))
                                .successHandler(customOAuth2LoginSuccessHandler)
                )
                .httpBasic(Customizer.withDefaults())// swagger 에서는 필수로 기록해야 한다.
                // JWT 인증을 위하여 직접 구현한 필터를 UsernamePasswordAuthenticationFilter 전에 실행
                .addFilterBefore(new JwtAuthenticationFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class)
                .sessionManagement(sessionManagement -> sessionManagement
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)// JWT 를 사용하므로 세션을 사용하지 않음
                        //.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)// OAuth2 를 사용하면서 수정.
                )
                .build();
    }

    private DefaultAuthorizationCodeTokenResponseClient customAccessTokenResponseClient(AppleClientSecretService appleClientSecretService) {
        DefaultAuthorizationCodeTokenResponseClient client = new DefaultAuthorizationCodeTokenResponseClient();
        client.setRequestEntityConverter(new CustomRequestEntityConverter(appleClientSecretService));
        return client;
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


}

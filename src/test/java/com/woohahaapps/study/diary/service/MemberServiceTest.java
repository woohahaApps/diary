package com.woohahaapps.study.diary.service;

import com.woohahaapps.study.diary.domain.Member;
import com.woohahaapps.study.diary.exception.MemberException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberServiceTest {

    private final MemberService memberService;

    @Autowired
    MemberServiceTest(MemberService memberService) {
        this.memberService = memberService;
    }

//    @Test
//    void signUp() {
//        String email = "";
//        String password = "";
//        String name = "";
//        Assertions.assertThrows(MemberException.class, () -> memberService.signUp(new Member(email, password, name)));
//    }
}
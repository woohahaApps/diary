package com.woohahaapps.study.diary.mapper;

import com.woohahaapps.study.diary.domain.Member;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberMapperTest {

    private final MemberMapper memberMapper;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    MemberMapperTest(MemberMapper memberMapper, PasswordEncoder passwordEncoder) {
        this.memberMapper = memberMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Test
    void createMember() {
        //memberMapper.CreateMember("woohaha@gmail.com", "1111", "우하하");
        Member member = new Member();
        member.setName("우하하");
        member.setEmail("woohaha@gmail.com");
        member.setPassword(passwordEncoder.encode("1111"));
        memberMapper.CreateMember(member);
    }

    @Test
    void getMember() {
    }

    @Test
    void updateMember() {
    }

    @Test
    void deleteMember() {
    }

    @Test
    void getAllMembers() {
    }
}
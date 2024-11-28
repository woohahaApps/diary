package com.woohahaapps.study.diary.service;

import com.woohahaapps.study.diary.domain.Member;
import com.woohahaapps.study.diary.exception.MemberException;
import com.woohahaapps.study.diary.mapper.MemberMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MemberService {
    private final MemberMapper memberMapper;
    private final PasswordEncoder passwordEncoder;
    private final JavaMailSender mailSender;

    @Autowired
    public MemberService(MemberMapper memberMapper, PasswordEncoder passwordEncoder, JavaMailSender mailSender) {
        this.memberMapper = memberMapper;
        this.passwordEncoder = passwordEncoder;
        this.mailSender = mailSender;
    }

    public void signUp(Member member) throws MemberException {
        if (member.getEmail().isBlank() || member.getPassword().isBlank() || member.getName().isBlank()) {
            throw new MemberException("필수입력값이 없습니다: "
                    + (member.getEmail().isBlank() ? "email " : "")
                    + (member.getPassword().isBlank() ? "password " : "")
                    + (member.getName().isBlank() ? "name " : "")
            );
        }
        try {
            //memberMapper.CreateMember(email, passwordEncoder.encode(password), name);
            System.out.println("password = " + member.getPassword());
            System.out.println("encoded password = " + passwordEncoder.encode(member.getPassword()));
            member.setPassword(passwordEncoder.encode(member.getPassword()));
            // 기본권한 설정
            member.setRole("ROLE_USER");
            memberMapper.CreateMember(member);

            // Mail 발송
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(member.getEmail());
            message.setSubject("[signUp] diary 에 회원가입되었습니다");
            message.setText(String.format("email: %s\nname: %s", member.getEmail(), member.getName()));
            mailSender.send(message);
        } catch (Exception e) {
            System.out.println(e.getClass().getName());// org.springframework.dao.DuplicateKeyException
            if (e.getClass().getName().toLowerCase().contains("duplicatekey")){
                throw new MemberException("이메일주소가 이미 등록되어 있습니다: " + member.getEmail());
            } else {
                throw new MemberException(e.getMessage());
            }
        }
    }

    public List<Member> getMembers() {
        return memberMapper.GetAllMembers();
    }
}

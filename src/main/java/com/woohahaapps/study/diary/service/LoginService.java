package com.woohahaapps.study.diary.service;

import com.woohahaapps.study.diary.domain.Member;
import com.woohahaapps.study.diary.domain.User;
import com.woohahaapps.study.diary.mapper.MemberMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LoginService implements UserDetailsService {

//    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final MemberMapper memberMapper;

    public LoginService(MemberMapper memberMapper) {
        this.memberMapper = memberMapper;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Member> member = memberMapper.GetMember(username);
        if (member.isEmpty())
            throw new UsernameNotFoundException("해당 이름의 사용자가 존재하지 않습니다: " + username);
        return new User(member.get());
    }
}

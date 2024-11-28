package com.woohahaapps.study.diary.controller;

import com.woohahaapps.study.diary.domain.Member;
import com.woohahaapps.study.diary.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/member")
public class MemberApiController {

    private final MemberService memberService;

    @Autowired
    public MemberApiController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("")
    public ResponseEntity<List<Member>> getMembers() {
        List<Member> members = memberService.getMembers();
        return ResponseEntity.ok().body(members);
    }
}

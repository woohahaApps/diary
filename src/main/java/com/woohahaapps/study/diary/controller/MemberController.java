package com.woohahaapps.study.diary.controller;

import com.woohahaapps.study.diary.domain.Member;
import com.woohahaapps.study.diary.exception.MemberException;
import com.woohahaapps.study.diary.service.MemberService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Optional;

@Controller
@Slf4j
public class MemberController {

    private final MemberService memberService;

    @Autowired
    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("/signup")
    public String signUp(Model model) {
        model.addAttribute("member", new Member());
        return "signup";
    }

    @PostMapping("/signup")
    public String signUp(@Valid Member member, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "signup";
        }
        try {
            //memberService.signUp(member.getEmail(), member.getPassword(), member.getName());
            memberService.signUp(member);
        } catch (MemberException e) {
            log.error(e.getMessage());
            model.addAttribute("error", e.getMessage());
            return "signup";
        } catch (Exception e) {
            log.error(e.getMessage());
            model.addAttribute("exception", e.getMessage());
            return "signup";
        }
        System.out.println("[REDIRECT] MemberController. /signup. redirect:/");
        return "redirect:/";
    }
}

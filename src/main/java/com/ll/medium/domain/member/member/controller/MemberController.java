package com.ll.medium.domain.member.member.controller;

import com.ll.medium.domain.member.member.entity.Member;
import com.ll.medium.domain.member.member.form.MemberJoinForm;
import com.ll.medium.domain.member.member.service.MemberService;
import com.ll.medium.global.rq.Rq;
import com.ll.medium.global.rsData.RsData;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {
    private final MemberService memberService;
    private final Rq rq;

    @PreAuthorize("isAnonymous()")
    @GetMapping("/join")
    public String showJoin() {
        return "member/member/join";
    }

    @PreAuthorize("isAnonymous()")
    @PostMapping("/join")
    public String join(@Valid MemberJoinForm joinForm, BindingResult bindingResult) {

        RsData<Member> joinRs = memberService.join(
                joinForm.getUsername(),
                joinForm.getPassword(),
                joinForm.getPasswordConfirm(),
                bindingResult
        );
        return rq.redirectOrBack("/member/login", joinRs);
    }

    @PreAuthorize("isAnonymous()")
    @GetMapping("/login")
    public String showLogin() {

        return "member/member/login";
    }
}

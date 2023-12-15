package com.ll.medium.domain.member.member.service;

import com.ll.medium.domain.member.member.entity.Member;
import com.ll.medium.domain.member.member.repository.MemberRepository;
import com.ll.medium.global.rq.Rq;
import com.ll.medium.global.rsData.RsData;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final Rq rq;

    public RsData<Member> join(String username,
                               String password,
                               String passwordConfirm
    ) {
        if (!password.equals(passwordConfirm)) {
            return RsData.of("400", "비밀번호가 일치하지 않습니다.", null);
        }
        if (findByUsername(username).isPresent()) {
            return RsData.of("400", "이미 사용중인 아이디입니다.", null);
        }

        Member member = Member.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .build();

        return RsData.of("200",
                "회원가입이 완료되었습니다.",
                memberRepository.save(member));
    }

    public RsData<Member> join(String username,
                               String password,
                               String passwordConfirm,
                               BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            return RsData.of(
                    "400",
                    bindingResult.getFieldError().getDefaultMessage(),
                    null
            );
        }

        return join(username, password, passwordConfirm);
    }

    public Optional<Member> findByUsername(String username) {
        return memberRepository.findByUsername(username);
    }

    public Optional<Member> findLatest() {
        return memberRepository.findFirstByOrderByIdDesc();
    }
}

package com.ll.medium.global.security;

import com.ll.medium.domain.member.member.entity.Member;
import com.ll.medium.domain.member.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.ll.medium.domain.member.member.entity.MemberRole.*;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final MemberService memberService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Member> memberOp = memberService.findByUsername(username);
        if (memberOp.isEmpty()) {
            throw new UsernameNotFoundException("사용자를 찾을 수 없습니다.");
        }

        Member member = memberOp.get();
        List<GrantedAuthority> authorites = new ArrayList<>();

        if ("admin".equals(username)) authorites.add(new SimpleGrantedAuthority(ADMIN.getValue()));
        if (member.isPaid()) authorites.add(new SimpleGrantedAuthority(PAID.getValue()));
        else authorites.add(new SimpleGrantedAuthority(USER.getValue()));

        return new SecurityUser(
                member.getId(),
                member.getUsername(),
                member.getPassword(),
                member.getAuthorities()
        );
    }
}

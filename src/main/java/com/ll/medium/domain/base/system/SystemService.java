package com.ll.medium.domain.base.system;

import com.ll.medium.domain.member.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SystemService {
    private final MemberRepository memberRepository;

    public boolean isSampleDataCreated() {
        return (memberRepository.count() > 0);
    }
}

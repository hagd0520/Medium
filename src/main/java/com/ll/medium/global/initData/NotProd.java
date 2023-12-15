package com.ll.medium.global.initData;

import com.ll.medium.domain.article.article.service.ArticleService;
import com.ll.medium.domain.base.system.SystemService;
import com.ll.medium.domain.member.member.entity.Member;
import com.ll.medium.domain.member.member.service.MemberService;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.stream.IntStream;

@Configuration
public class NotProd {
    @Bean
    public ApplicationRunner initNotProd(
            SystemService systemService,
            MemberService memberService,
            ArticleService articleService
    ) {
        return args -> {
            if (systemService.isSampleDataCreated()) return;

            Member memberAdmin = memberService.join("admin", "1234", "1234").getData();
            Member memberUser1 = memberService.join("user1", "1234", "1234").getData();
            Member memberUser2 = memberService.join("user2", "1234", "1234").getData();

            articleService.write("제목1", "내용1", memberAdmin, true);
            articleService.write("제목2", "내용2", memberUser1, true);
            articleService.write("제목3", "내용3", memberUser1, true);
            articleService.write("제목4", "내용4", memberUser2, true);
            articleService.write("제목5", "내용5", memberUser2, true);

            IntStream.rangeClosed(6, 100)
                    .forEach(i -> articleService.write(
                                    "제목%d".formatted(i),
                                    "내용%d".formatted(i),
                                    memberUser1,
                                    true
                            )
                    );
        };
    }
}

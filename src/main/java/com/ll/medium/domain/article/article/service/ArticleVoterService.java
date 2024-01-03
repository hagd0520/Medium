package com.ll.medium.domain.article.article.service;

import com.ll.medium.domain.article.article.entity.Article;
import com.ll.medium.domain.article.article.entity.ArticleVoter;
import com.ll.medium.domain.article.article.repository.ArticleVoterRepository;
import com.ll.medium.domain.member.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ArticleVoterService {
    private final ArticleVoterRepository articleVoterRepository;

    @Transactional
    public void addVote(Member actor, Article article) {
        ArticleVoter articleVoter = ArticleVoter.builder()
                .actor(actor)
                .article(article)
                .build();

        articleVoterRepository.save(articleVoter);
    }

    @Transactional
    public void cancelVote(Member actor, Article article) {
        articleVoterRepository.deleteByActorAndArticle(actor, article);
    }

    public boolean canVote(Member actor, Article article) {
        if (actor == null) return false;

        return !articleVoterRepository.existsByActorAndArticle(actor, article);
    }
}

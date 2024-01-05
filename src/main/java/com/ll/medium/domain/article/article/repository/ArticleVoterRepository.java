package com.ll.medium.domain.article.article.repository;

import com.ll.medium.domain.article.article.entity.Article;
import com.ll.medium.domain.article.article.entity.ArticleVoter;
import com.ll.medium.domain.member.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleVoterRepository extends JpaRepository<ArticleVoter, Long> {
    void deleteByActorAndArticle(Member actor, Article article);

    boolean existsByActorAndArticle(Member actor, Article article);

    long countByArticle(Article article);
}

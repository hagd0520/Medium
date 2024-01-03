package com.ll.medium.domain.article.article.repository;

import com.ll.medium.domain.article.article.entity.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ArticleRepository extends JpaRepository<Article, Long> {
    Optional<Article> findFirstByOrderByIdDesc();

    Page<Article> findByAuthorUsername(String username, Pageable pageable);

    Page<Article> findByAuthorUsernameContainingAndIsPublishedTrue(String username, Pageable pageable);

    Optional<Article> findByAuthorUsername(String username);

    Page<Article> findByIsPublishedTrue(Pageable pageable);

    List<Article> findTop30ByIsPublishedTrueOrderByCreateDateDesc();
}

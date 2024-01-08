package com.ll.medium.domain.article.comment.repository;

import com.ll.medium.domain.article.article.entity.Article;
import com.ll.medium.domain.article.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByArticleOrderByIdDesc(Article article);
}

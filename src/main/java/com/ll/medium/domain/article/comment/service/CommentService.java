package com.ll.medium.domain.article.comment.service;

import com.ll.medium.domain.article.article.entity.Article;
import com.ll.medium.domain.article.comment.entity.Comment;
import com.ll.medium.domain.article.comment.repository.CommentRepository;
import com.ll.medium.domain.member.member.entity.Member;
import com.ll.medium.global.rsData.RsData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {
    private final CommentRepository commentRepository;

    @Transactional
    public RsData<Comment> write(Article article, Member author, String body) {
        Comment comment = Comment.builder()
                .author(author)
                .article(article)
                .body(body)
                .build();

        commentRepository.save(comment);

        return RsData.of("200",
                "댓글이 작성되었습니다.",
                comment);
    }

    public List<Comment> findByArticle(Article article) {
        return commentRepository.findByArticleOrderByIdDesc(article);
    }
}

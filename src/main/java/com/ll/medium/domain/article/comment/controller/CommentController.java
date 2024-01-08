package com.ll.medium.domain.article.comment.controller;

import com.ll.medium.domain.article.article.entity.Article;
import com.ll.medium.domain.article.article.service.ArticleService;
import com.ll.medium.domain.article.comment.entity.Comment;
import com.ll.medium.domain.article.comment.form.CommentForm;
import com.ll.medium.domain.article.comment.service.CommentService;
import com.ll.medium.global.rq.Rq;
import com.ll.medium.global.rsData.RsData;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/comment")
@PreAuthorize("isAuthenticated()")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;
    private final ArticleService articleService;
    private final Rq rq;

    @PostMapping("/write/{id}")
    public String writeComment(
            @PathVariable long id,
            @Valid CommentForm commentForm,
            BindingResult bindingResult
    ) {
        Article article = articleService.findById(id).get();

        RsData<Comment> commentWriteRs;

        if (bindingResult.hasErrors()) {
            commentWriteRs = RsData.of(
                    "400",
                    bindingResult.getFieldError().getDefaultMessage(),
                    null
            );

            return rq.redirectOrBack("/article/%d/detail".formatted(id), commentWriteRs);
        }

        commentWriteRs = commentService.write(
                article,
                rq.getMember(),
                commentForm.getBody()
        );

        return rq.redirectOrBack("/article/%d/detail".formatted(id), commentWriteRs);
    }
}

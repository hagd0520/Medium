package com.ll.medium.domain.article.comment.controller;

import com.ll.medium.domain.article.article.entity.Article;
import com.ll.medium.domain.article.article.service.ArticleService;
import com.ll.medium.domain.article.comment.service.CommentService;
import com.ll.medium.domain.member.member.entity.Member;
import com.ll.medium.global.rq.Rq;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/comment")
@PreAuthorize("isAuthenticated()")
@RequiredArgsConstructor
@Validated
public class CommentController {
    private final CommentService commentService;
    private final ArticleService articleService;
    private final Rq rq;

    @PostMapping("/write/{id}")
    public String writeComment(
            @PathVariable long id,
            @NotEmpty String body,
            BindingResult bindingResult
    ) {
        Article article = articleService.findById(id);
        Member member = rq.getMember();
        if (bindingResult.hasErrors()) {

        }
    }
}

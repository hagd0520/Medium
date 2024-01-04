package com.ll.medium.domain.article.comment.controller;

import com.ll.medium.domain.article.comment.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/Comment")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;
}

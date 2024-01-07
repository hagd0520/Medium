package com.ll.medium.domain.home.home.controller;

import com.ll.medium.domain.article.article.entity.Article;
import com.ll.medium.domain.article.article.service.ArticleService;
import com.ll.medium.global.rq.Rq;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class HomeController {
    private final ArticleService articleService;
    private final Rq rq;

    @GetMapping("/")
    public String goToMain() {
        return rq.redirect("/home");
    }

    @GetMapping("/home")
    public String showMain() {
        List<Article> articleList = articleService.getLast30Article();
        rq.attr("articleList", articleList);
        return "home/home/main";
    }
}

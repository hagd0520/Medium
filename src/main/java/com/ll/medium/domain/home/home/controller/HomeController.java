package com.ll.medium.domain.home.home.controller;

import com.ll.medium.domain.article.article.entity.Article;
import com.ll.medium.domain.article.article.service.ArticleService;
import com.ll.medium.global.rq.Rq;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class HomeController {
    private final Rq rq;
    private final ArticleService articleService;

    @GetMapping("/")
    public String showMain() {
        return rq.redirect("/home");
    }

    @GetMapping("/home")
    public String showMain(Model model) {
        List<Article> articleList = articleService.getLast30Article();
        model.addAttribute("articleList", articleList);
        return "home/home/main";
    }
}

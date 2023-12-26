package com.ll.medium.domain.article.article.controller;


import com.ll.medium.domain.article.article.entity.Article;
import com.ll.medium.domain.article.article.service.ArticleService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class ArticleControllerTest {
    @Autowired
    private MockMvc mvc;
    @Autowired
    private ArticleService articleService;

    @Test
    @DisplayName("게시물 목록 페이지를 보여준다")
    void t1() throws Exception {
        // WHEN
        ResultActions resultActions = mvc
                .perform(get("/article/list"))
                .andDo(print());

        Article article = articleService.findLatest().get();

        // THEN
        resultActions
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(ArticleController.class))
                .andExpect(handler().methodName("showList"))
                .andExpect(content().string(containsString("""
                        게시글 목록
                        """.stripIndent().trim())))
                .andExpect(content().string(containsString("""
                        %s
                        """.formatted(article.getAuthor().getUsername()).stripIndent().trim())))
                .andExpect(content().string(containsString("""
                        %s
                        """.formatted(article.getTitle()).stripIndent().trim())));
    }
}

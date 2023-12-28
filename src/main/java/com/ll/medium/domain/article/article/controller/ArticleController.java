package com.ll.medium.domain.article.article.controller;

import com.ll.medium.domain.article.article.entity.Article;
import com.ll.medium.domain.article.article.entity.ArticleModifyForm;
import com.ll.medium.domain.article.article.entity.ArticleWriteForm;
import com.ll.medium.domain.article.article.service.ArticleService;
import com.ll.medium.global.rq.Rq;
import com.ll.medium.global.rsData.RsData;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/article")
@RequiredArgsConstructor
public class ArticleController {
    private final ArticleService articleService;
    private final Rq rq;

    @GetMapping("/list")
    public String showList(Model model,
                           @RequestParam(value = "kwUsername", defaultValue = "") String kwUsername,
                           @RequestParam(value = "page", defaultValue = "0") int page
    ) {
        Page<Article> paging = null;
        if (kwUsername.equals("")) paging = articleService.getList(page);
        if (!kwUsername.equals("")) paging = articleService.searchListByUsername(kwUsername, page);
        model.addAttribute("paging", paging);
        model.addAttribute("kwUsername", kwUsername);
        return "article/article/list";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/myList")
    public String showMyList(Model model,
                             @RequestParam(value = "page", defaultValue = "0") int page
    ) {
        Page<Article> paging = articleService.getMyList(page);
        model.addAttribute("paging", paging);
        return "article/article/myList";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/write")
    public String showWrite() {
        return "article/article/write";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/write")
    public String write(@Valid ArticleWriteForm writeForm, BindingResult bindingResult) {


        RsData<Article> writeRs = articleService.write(
                writeForm.getTitle(),
                writeForm.getBody(),
                rq.getMember(),
                writeForm.isPublished(),
                bindingResult
        );
        return rq.redirectOrBack("/", writeRs);
    }

    @GetMapping("/{id}/detail")
    public String showDetail(Model model, @PathVariable long id) {
        Article article = articleService.findById(id).get();

        model.addAttribute("article", article);

        return "article/article/detail";
    }

    @GetMapping("/{id}/delete")
    public String delete(@PathVariable long id) {
        Article article = articleService.findById(id).get();

        if (!articleService.canDelete(rq.getMember(), article)) throw new RuntimeException("삭제 권한이 없습니다.");

        articleService.delete(article);

        return rq.redirect("/", "%d번 게시물이 삭제되었습니다.".formatted(id));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}/modify")
    public String showModify(Model model, @PathVariable long id) {
        Article article = articleService.findById(id).get();

        if (!articleService.canModify(rq.getMember(), article)) throw new RuntimeException("수정 권한이 없습니다.");

        model.addAttribute("article", article);

        return "article/article/modify";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("{id}/modify")
    public String modify(@PathVariable long id, @Valid ArticleModifyForm modifyForm) {
        Article article = articleService.findById(id).get();

        if (!articleService.canModify(rq.getMember(), article)) throw new RuntimeException("수정 권한이 없습니다.");

        articleService.modify(article, modifyForm.getTitle(), modifyForm.getBody(), modifyForm.isPublished());

        return rq.redirect("/article/detail/%d".formatted(id), "게시물이 수정되었습니다.");
    }
}

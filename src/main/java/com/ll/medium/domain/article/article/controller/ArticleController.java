package com.ll.medium.domain.article.article.controller;

import com.ll.medium.domain.article.article.entity.Article;
import com.ll.medium.domain.article.article.entity.ArticleWriteForm;
import com.ll.medium.domain.article.article.service.ArticleService;
import com.ll.medium.domain.exceptions.GlobalException.GlobalException;
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
    public String showList(
            Model model,
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
    public String showMyList(
            Model model,
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
    public String write(
            @Valid ArticleWriteForm writeForm,
            BindingResult bindingResult
    ) {
        RsData<Article> writeRs = articleService.write(
                writeForm.getTitle(),
                writeForm.getBody(),
                rq.getMember(),
                writeForm.isPublished(),
                writeForm.isPaid(),
                bindingResult
        );
        return rq.redirectOrBack("/", writeRs);
    }

    @GetMapping("/{id}/detail")
    public String showDetail(
            Model model,
            @PathVariable long id
    ) {
        Article article = articleService.findById(id).get();

        article.addHit();

        model.addAttribute("article", article);

        return "article/article/detail";
    }

    @DeleteMapping("/{id}/delete")
    @PreAuthorize("isAuthenticated()")
    public String delete(
            @PathVariable long id
    ) {
        Article article = articleService.findById(id).get();

        if (!articleService.canDelete(rq.getMember(), article)) throw new RuntimeException("삭제 권한이 없습니다.");

        articleService.delete(article);

        return rq.redirect("/", "%d번 게시물이 삭제되었습니다.".formatted(id));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}/modify")
    public String showModify(
            Model model,
            @PathVariable long id
    ) {
        Article article = articleService.findById(id).get();

        if (!articleService.canModify(rq.getMember(), article)) throw new RuntimeException("수정 권한이 없습니다.");

        model.addAttribute("article", article);

        return "article/article/modify";
    }

    @PreAuthorize("isAuthenticated()")
    @PutMapping("/{id}/modify")
    public String modify(
            @PathVariable long id,
            @Valid ArticleWriteForm writeForm,
            BindingResult bindingResult
    ) {
        Article article = articleService.findById(id).get();

        if (!articleService.canModify(rq.getMember(), article)) throw new RuntimeException("수정 권한이 없습니다.");

        RsData<Article> modifyRs = articleService.modify(
                article,
                writeForm.getTitle(),
                writeForm.getBody(),
                writeForm.isPublished(),
                writeForm.isPaid(),
                bindingResult
        );

        return rq.redirectOrBack("/article/%d/detail".formatted(id), modifyRs);
    }

    @PostMapping("/{id}/addVote")
    @PreAuthorize("isAuthenticated()")
    public String addVote(
            @PathVariable long id
    ) {
        Article article = articleService.findById(id).orElseThrow(() -> new GlobalException("400", "존재하지 않는 글입니다."));
        articleService.addVote(rq.getMember(), article);

        return rq.historyBack("추천하셨습니다.");
    }

    @DeleteMapping("/{id}/cancelVote")
    @PreAuthorize("isAuthenticated()")
    public String cancelVote(
            @PathVariable long id
    ) {
        Article article = articleService.findById(id).orElseThrow(() -> new GlobalException("400", "존재하지 않는 글입니다."));
        articleService.cancelVote(rq.getMember(), article);

        return rq.historyBack("추천하셨습니다.");
    }
}

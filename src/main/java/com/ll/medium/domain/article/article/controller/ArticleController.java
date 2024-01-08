package com.ll.medium.domain.article.article.controller;

import com.ll.medium.domain.article.article.entity.Article;
import com.ll.medium.domain.article.article.form.ArticleWriteForm;
import com.ll.medium.domain.article.article.service.ArticleService;
import com.ll.medium.domain.article.comment.entity.Comment;
import com.ll.medium.domain.article.comment.service.CommentService;
import com.ll.medium.domain.exceptions.GlobalException.GlobalException;
import com.ll.medium.global.rq.Rq;
import com.ll.medium.global.rsData.RsData;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/article")
@RequiredArgsConstructor
public class ArticleController {
    private final ArticleService articleService;
    private final CommentService commentService;
    private final Rq rq;

    @GetMapping("/list")
    public String showList(
            @RequestParam(value = "kwUsername", defaultValue = "") String kwUsername,
            @RequestParam(value = "page", defaultValue = "0") int page
    ) {
        Page<Article> paging = null;
        if (kwUsername.equals("")) paging = articleService.getList(page);
        if (!kwUsername.equals("")) paging = articleService.searchListByUsername(kwUsername, page);
        rq.attr("paging", paging);
        rq.attr("kwUsername", kwUsername);

        return "article/article/list";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/myList")
    public String showMyList(
            @RequestParam(value = "page", defaultValue = "0") int page
    ) {
        Page<Article> paging = articleService.getMyList(page);
        rq.attr("paging", paging);
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
        RsData<Article> writeRs;

        if (bindingResult.hasErrors()) {
            writeRs = RsData.of(
                    "400",
                    bindingResult.getFieldError().getDefaultMessage(),
                    null
            );
            return rq.redirectOrBack("/", writeRs);

        }

        writeRs = articleService.write(
                writeForm.getTitle(),
                writeForm.getBody(),
                rq.getMember(),
                writeForm.isPublished(),
                writeForm.isPaid()
        );
        return rq.redirectOrBack("/", writeRs);
    }

    @GetMapping("/{id}/detail")
    public String showDetail(
            @PathVariable long id
    ) {
        Article article = articleService.findById(id).get();
        List<Comment> comments = commentService.findByArticle(article);

        article.addHit();

        rq.attr("article", article);
        rq.attr("comments", comments);

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
            @PathVariable long id
    ) {
        Article article = articleService.findById(id).get();

        if (!articleService.canModify(rq.getMember(), article)) throw new RuntimeException("수정 권한이 없습니다.");

        rq.attr("article", article);

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

        RsData<Article> modifyRs;

        if (bindingResult.hasErrors()) {
            modifyRs = RsData.of(
                    "400",
                    bindingResult.getFieldError().getDefaultMessage(),
                    null
            );
            return rq.redirectOrBack("/article/%d/detail".formatted(id), modifyRs);
        }

        modifyRs = articleService.modify(
                article,
                writeForm.getTitle(),
                writeForm.getBody(),
                writeForm.isPublished(),
                writeForm.isPaid()
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

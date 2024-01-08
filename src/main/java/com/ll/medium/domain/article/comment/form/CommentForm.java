package com.ll.medium.domain.article.comment.form;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentForm {
    @NotEmpty(message = "내용을 입력해주세요.")
    private String body;
}

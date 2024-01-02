package com.ll.medium.domain.article.article.entity;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ArticleModifyForm {
    @NotEmpty(message = "제목을 입력해주세요.")
    @Size(max = 200, message = "제목은 200자를 넘길 수 없습니다.")
    private String title;

    @NotEmpty(message = "내용을 입력해주세요.")
    private String body;

    private boolean isPublished;

    private boolean isPaid;
}

package com.ll.medium.domain.article.article.entity;

import com.ll.medium.domain.article.comment.entity.Comment;
import com.ll.medium.domain.member.member.entity.Member;
import com.ll.medium.global.jpa.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.CascadeType.ALL;
import static jakarta.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@SuperBuilder
@AllArgsConstructor(access = PROTECTED)
@NoArgsConstructor(access = PROTECTED)
@Getter
@Setter
@ToString(callSuper = true)
public class Article extends BaseEntity {
    @ManyToOne(fetch = LAZY)
    private Member author;
    private String title;
    private String body;
    private long hit;
    private boolean isPublished;
    private boolean isPaid;
    @OneToMany(mappedBy = "article", cascade = ALL, orphanRemoval = true)
    @Builder.Default
    private List<Comment> comments = new ArrayList<>();

    public boolean isPaid() {
        return isPaid;
    }

    public void addHit() {
        hit = hit + 1;
    }

    public void addComment(Member author, String body) {
        Comment comment = Comment.builder()
                .article(this)
                .author(author)
                .body(body)
                .build();
    }
}

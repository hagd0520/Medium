package com.ll.medium.domain.member.member.entity;

import lombok.Getter;

@Getter
public enum MemberRole {
    ADMIN("ROLE_ADMIN"),
    USER("ROLE_USER"),
    PAID("ROLE_PAID");

    MemberRole(String value) {
        this.value = value;
    }

    private String value;
}

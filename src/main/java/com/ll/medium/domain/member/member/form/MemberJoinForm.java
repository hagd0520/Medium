package com.ll.medium.domain.member.member.form;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberJoinForm {
    @NotEmpty(message = "아이디를 입력해주세요.")
    private String username;
    @NotEmpty(message = "비밀번호를 입력해주세요.")
    private String password;
    @NotEmpty(message = "비밀번호 확인을 입력해주세요.")
    private String passwordConfirm;
}

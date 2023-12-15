package com.ll.medium.domain.member.member.controller;

import com.ll.medium.domain.member.member.entity.Member;
import com.ll.medium.domain.member.member.service.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class MemberControllerTest {
    @Autowired
    private MockMvc mvc;
    @Autowired
    private MemberService memberService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    @DisplayName("회원가입 폼")
    void t1() throws Exception {
        // WHEN
        ResultActions resultActions = mvc
                .perform(get("/member/join"))
                .andDo(print());

        // THEN
        resultActions
                .andExpect(status().isOk())
                .andExpect(view().name("member/member/join"))
                .andExpect(handler().handlerType(MemberController.class))
                .andExpect(handler().methodName("showJoin"))
                .andExpect(content().string(containsString("""
                        회원가입
                        """.stripIndent().trim())))
                .andExpect(content().string(containsString("""
                        medium
                        """.stripIndent().trim())))
                .andExpect(content().string(containsString("""
                        <input type="text" name="username"
                        """.stripIndent().trim())));
    }

    @Test
    @DisplayName("회원가입 처리")
    void t2() throws Exception {
        // WHEN
        ResultActions resultActions = mvc
                .perform(
                        post("/member/join")
                                .with(csrf())
                                .param("username", "newuser")
                                .param("password", "1234")
                                .param("passwordConfirm", "1234")
                )
                .andDo(print());

        // THEN
        resultActions
                .andExpect(status().is3xxRedirection())
                .andExpect(handler().handlerType(MemberController.class))
                .andExpect(handler().methodName("join"))
                .andExpect(redirectedUrlPattern("/member/login**"));

        Member member = memberService.findLatest().get();

        assertThat(member.getUsername()).isEqualTo("newuser");
        assertThat(passwordEncoder.matches("1234", member.getPassword())).isTrue();
    }

    @Test
    @DisplayName("아이디 중복 처리")
    void t3() throws Exception {
        // WHEN
        ResultActions resultActions1 = mvc
                .perform(
                        post("/member/join")
                                .with(csrf())
                                .param("username", "overlappingTestUser")
                                .param("password", "1234")
                                .param("passwordConfirm", "1234")
                )
                .andDo(print());

        ResultActions resultActions2 = mvc
                .perform(
                        post("/member/join")
                                .with(csrf())
                                .param("username", "overlappingTestUser")
                                .param("password", "1234")
                                .param("passwordConfirm", "1234")
                )
                .andDo(print());

        // THEN
        resultActions1
                .andExpect(status().is3xxRedirection())
                .andExpect(handler().handlerType(MemberController.class))
                .andExpect(handler().methodName("join"));

        resultActions2
                .andExpect(status().is4xxClientError())
                .andExpect(handler().handlerType(MemberController.class))
                .andExpect(handler().methodName("join"));
    }

    @Test
    @DisplayName("로그인 페이지를 보여준다.")
    void t4() throws Exception {
        // WHEN
        ResultActions resultActions = mvc
                .perform(get("/member/login"))
                .andDo(print());

        // THEN
        resultActions
                .andExpect(status().isOk())
                .andExpect(view().name("member/member/login"))
                .andExpect(handler().handlerType(MemberController.class))
                .andExpect(handler().methodName("showLogin"))
                .andExpect(content().string(containsString("""
                        로그인
                        """.stripIndent().trim())))
                .andExpect(content().string(containsString("""
                        <input type="text" name="username"
                        """.stripIndent().trim())))
                .andExpect(content().string(containsString("""
                        <input type="password" name="password"
                        """.stripIndent().trim())));
    }

    @Test
    @DisplayName("잘못된 로그인 정보")
    void t5() throws Exception {
        mvc.perform(
                        formLogin("/member/login")
                                .user("username", "user1")
                                .password("password", "12345")
                )
                .andDo(print())
                .andExpect(unauthenticated());
    }

    @Test
    @DisplayName("로그인 처리")
    void t6() throws Exception {
        mvc.perform(
                        formLogin("/member/login")
                                .user("username", "user1")
                                .password("password", "1234")
                )
                .andDo(print())
                .andExpect(authenticated());
    }
}

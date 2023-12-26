package com.ll.medium.global.rq;

import com.ll.medium.domain.member.member.entity.Member;
import com.ll.medium.domain.member.member.service.MemberService;
import com.ll.medium.global.rsData.RsData;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import java.net.URLEncoder;
import java.util.Date;

import static java.nio.charset.StandardCharsets.UTF_8;

@RequestScope
@Component
@RequiredArgsConstructor
public class Rq {
    private final HttpServletRequest req;
    private final HttpServletResponse resp;
    private final MemberService memberService;
    private User user;
    private Member member;

    @PostConstruct
    public void init() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication.getPrincipal() instanceof User) {
            this.user = (User) authentication.getPrincipal();
        }
    }

    public String redirect(String path) {
        return "redirect:" + path;
    }

    public String redirect(String path, RsData<?> rsData) {
        return redirect(path, rsData.getMsg());
    }

    public String redirect(String path, String msg) {
        if (msg == null) return "redirect:" + path;

        boolean containsTtl = msg.contains(";ttl=");

        if (containsTtl) {
            msg = msg.split(";ttl=", 2)[0];
        }

        msg = URLEncoder.encode(msg, UTF_8);
        msg += ";ttl=" + (new Date().getTime() + 1000 * 5);

        return redirect(path) + "?msg=" + msg;
    }

    public String historyBack(String msg) {
        resp.setStatus(400);
        req.setAttribute("msg", msg);

        return "global/js";
    }

    public String historyBack(RsData<?> rs) {
        return historyBack(rs.getMsg());
    }

    public String redirectOrBack(String url, RsData<?> rs) {
        if (rs.isFail()) return historyBack(rs);
        return redirect(url, rs);
    }

    public boolean isLogined() {
        return user != null;
    }

    public String getMemberUsername() {
        return user.getUsername();
    }

    public Member getMember() {
        if (!isLogined()) {
            return null;
        }

        if (member == null)
            member = memberService.findByUsername(getMemberUsername()).get();


        return member;
    }
}

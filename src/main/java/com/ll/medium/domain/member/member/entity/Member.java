package com.ll.medium.domain.member.member.entity;

import com.ll.medium.global.jpa.BaseEntity;
import jakarta.persistence.Entity;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;

import static lombok.AccessLevel.PROTECTED;

@Entity
@SuperBuilder
@AllArgsConstructor(access = PROTECTED)
@NoArgsConstructor(access = PROTECTED)
@Getter
@Setter
@ToString(callSuper = true)
public class Member extends BaseEntity {
    private String username;
    private String password;
    private boolean isPaid;

    public boolean isAdmin() {
        return username.equals("admin");
    }

    public List<SimpleGrantedAuthority> getAuthorities() {
        if (isAdmin()) {
            return List.of(new SimpleGrantedAuthority("ROLE_ADMIN"), new SimpleGrantedAuthority("ROLE_MEMBER"));
        }

        return List.of(new SimpleGrantedAuthority("ROLE_MEMBER"));
    }
}

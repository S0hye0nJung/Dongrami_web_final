package com.lec.dto;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import com.lec.entity.Member;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

@Getter
public class CustomUserDetails implements UserDetails, OAuth2User {

    private final String userId;
    private final String password;
    private final String email;
    private final String loginType;
    private Map<String, Object> attributes;

    // 일반 로그인용 생성자
    public CustomUserDetails(Member member) {
        this.userId = member.getUserId();
        this.password = member.getPassword();
        this.email = member.getEmail();
        this.loginType = "local";
    }

    // OAuth2 로그인용 생성자
    public CustomUserDetails(Member member, Map<String, Object> attributes) {
        this.userId = member.getUserId();
        this.password = member.getPassword();
        this.email = member.getEmail();
        this.loginType = member.getProvider();
        this.attributes = attributes;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getUsername() {
        return userId;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String getName() {
        return userId;
    }
}
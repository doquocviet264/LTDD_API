package com.ltdd.QuanLyThuChiAPI.Response.User;

import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class JwtResponse {
    String token;

    private String type = "Bearer";

    private String username;

    private Collection< ?extends GrantedAuthority> role;

    public JwtResponse(String token, String type, String username, Collection<? extends GrantedAuthority> role) {
        this.token = token;
        this.type = type;
        this.username = username;
        this.role = role;
    }


    public JwtResponse(String token, String username, Collection<? extends GrantedAuthority> authorities) {
        this.token = token;
        this.username = username;
        this.role = authorities;
    }
}

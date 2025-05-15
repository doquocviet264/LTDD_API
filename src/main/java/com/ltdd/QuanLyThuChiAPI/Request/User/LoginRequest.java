package com.ltdd.QuanLyThuChiAPI.Request.User;

import lombok.Data;

@Data
public class LoginRequest {
    private String username;
    private String password;
}

package com.ltdd.QuanLyThuChiAPI.Request.User;

import lombok.Data;

@Data
public class UpdateProfileRequest {
    private String firstname;
    private String lastname;
    private String avatar;
}

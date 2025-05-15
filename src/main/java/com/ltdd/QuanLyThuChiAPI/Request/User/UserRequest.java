package com.ltdd.QuanLyThuChiAPI.Request.User;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Builder
@Data
public class UserRequest {
    private String userName;

    private String passWord;

    private String firstName;

    private String lastName;

    @NotNull(message = "Email không đươc để trống")
    @Email(message = "Email không đúng định dạng")
    private String email;

    private String roleName;

    private String avatar;
}

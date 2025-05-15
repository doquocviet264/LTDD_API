package com.ltdd.QuanLyThuChiAPI.Response.User;

import com.ltdd.QuanLyThuChiAPI.Model.Role.RoleModel;
import com.ltdd.QuanLyThuChiAPI.Model.User.UserInfoModel;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class UserResponse{
    private boolean status;

    private String message;

    private UserInfoModel userInfoModel;

    private RoleModel roleModel;
}

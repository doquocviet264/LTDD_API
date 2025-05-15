package com.ltdd.QuanLyThuChiAPI.Response.User;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PasswordResponse {
    private  boolean status;
    private String message;
    private int errorCode;
}

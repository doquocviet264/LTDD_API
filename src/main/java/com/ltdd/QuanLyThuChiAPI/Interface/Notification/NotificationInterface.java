package com.ltdd.QuanLyThuChiAPI.Interface.Notification;

import com.ltdd.QuanLyThuChiAPI.Response.Api.ApiResponse;

public interface NotificationInterface {
    public ApiResponse<Object> getAllNotificationNotRead(String username);

    public ApiResponse<Object> updateIsRead(String username, Long idNotifi);
}

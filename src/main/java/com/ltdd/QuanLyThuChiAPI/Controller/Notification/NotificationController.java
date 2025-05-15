package com.ltdd.QuanLyThuChiAPI.Controller.Notification;

import com.ltdd.QuanLyThuChiAPI.Response.Api.ApiResponse;
import com.ltdd.QuanLyThuChiAPI.Service.Notification.NotificationService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
@Slf4j
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping(value = "/api/notification")
public class NotificationController {
    @Autowired
    private NotificationService notificationService;

    @GetMapping(value = "/all")
    public ApiResponse<Object> getAllNotifi(Principal principal)
    {
        return notificationService.getAllNotificationNotRead(principal.getName());
    }

    @DeleteMapping(value = "/delete/{id}")
    public ApiResponse<Object> updateIsRead(Principal principal, @PathVariable("id") Long id)
    {
        return notificationService.updateIsRead(principal.getName(), id);
    }
}

package com.ltdd.QuanLyThuChiAPI.Controller.User;

import com.ltdd.QuanLyThuChiAPI.Request.User.UpdateProfileRequest;
import com.ltdd.QuanLyThuChiAPI.Response.Api.ApiResponse;
import com.ltdd.QuanLyThuChiAPI.Response.User.UserResponse;
import com.ltdd.QuanLyThuChiAPI.Service.User.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.security.Principal;

@RestController
@SecurityRequirement(name = "bearerAuth")
@RequestMapping(value = "/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping(value = "/getAll")
    public ApiResponse<Object> getUsers()
    {
        return userService.getAllUser();
    }
//
    @GetMapping(value = "/profile")
    public Mono<UserResponse> getUserCurrent(Principal principal)
    {
        return Mono.just(userService.getProfileUser(principal.getName()));
    }

    @PutMapping(value = "/profile")
    public ApiResponse<Object> updateProfile(Principal principal, @RequestBody UpdateProfileRequest updateProfileRequest)
    {
        return userService.updateProfile(principal.getName(), updateProfileRequest);
    }

    @PutMapping(value = "/delete/{id}")
    public ApiResponse<Object> blockAccount(@PathVariable("id") Long id)
    {
        return userService.deleteAccountUser(id);
    }

    @PutMapping(value = "/open/{id}")
    public ApiResponse<Object> openAccount(@PathVariable("id") Long id)
    {
        return userService.openAccountUser(id);
    }
}

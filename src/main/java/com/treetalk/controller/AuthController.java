package com.treetalk.controller;

import com.treetalk.dto.ApiResponse;
import com.treetalk.dto.UserDto;
import com.treetalk.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@Tag(name = "认证管理", description = "用户注册、登录等认证相关接口")
public class AuthController {

    private final UserService userService;

    @Autowired
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    @Operation(summary = "用户注册", description = "新用户注册接口")
    public ApiResponse<UserDto.LoginResponse> register(
            @Parameter(description = "注册请求信息", required = true)
            @Valid @RequestBody UserDto.RegisterRequest request) {
        UserDto.LoginResponse userResponse = userService.register(request);
        UserDto.LoginRequest loginRequest = new UserDto.LoginRequest();
        loginRequest.setUsername(request.getEmail());
        loginRequest.setPassword(request.getPassword());
        String token = userService.login(loginRequest);
        userResponse.setToken(token);
        return ApiResponse.success("注册成功", userResponse);
    }

    @PostMapping("/login")
    @Operation(summary = "用户登录", description = "用户登录接口，返回JWT令牌")
    public ApiResponse<String> login(
            @Parameter(description = "登录请求信息", required = true)
            @Valid @RequestBody UserDto.LoginRequest request) {
        String token = userService.login(request);
        return ApiResponse.success("登录成功", token);
    }
}
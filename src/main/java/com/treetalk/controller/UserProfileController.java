package com.treetalk.controller;

import com.treetalk.dto.ApiResponse;
import com.treetalk.dto.UserDto;
import com.treetalk.dto.UserProfileUpdateDto;
import com.treetalk.entity.UserProfile;
import com.treetalk.service.UserProfileService;
import com.treetalk.util.SecurityUserUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/profile")
@Tag(name = "用户资料管理", description = "用户个人资料相关接口")
public class UserProfileController {

    private final UserProfileService userProfileService;

    @Autowired
    public UserProfileController(UserProfileService userProfileService) {
        this.userProfileService = userProfileService;
    }

    @GetMapping
    @Operation(summary = "获取用户资料", description = "获取当前登录用户的个人资料信息")
    public ApiResponse<UserDto.UserProfileResponse> getUserProfile(@Parameter(hidden = true) @RequestAttribute("userId") Long userId) {
        return ApiResponse.success("获取用户资料成功", userProfileService.getUserProfile(userId));
    }

    @PutMapping
    @Operation(summary = "更新用户资料", description = "更新当前用户的个人资料信息")
    public ApiResponse<UserDto.UserProfileResponse> updateUserProfile(
            @Parameter(hidden = true) @RequestAttribute("userId") Long userId,
            @Parameter(description = "用户资料更新信息", required = true)
            @RequestBody UserProfileUpdateDto updatedProfile) {
        return ApiResponse.success("用户资料更新成功", userProfileService.updateUserProfile(userId, updatedProfile));
    }

    @PostMapping("/check-in")
    @Operation(summary = "用户签到", description = "用户每日签到，增加连续签到天数")
    public ApiResponse<UserDto.UserProfileResponse> checkIn(
            @Parameter(hidden = true) @RequestAttribute("userId") Long userId) {
        userProfileService.incrementCheckInDays(userId);
        return ApiResponse.success("签到成功", userProfileService.getUserProfile(userId));
    }
}
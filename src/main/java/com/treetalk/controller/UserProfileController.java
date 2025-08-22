package com.treetalk.controller;

import com.treetalk.dto.ApiResponse;
import com.treetalk.entity.UserProfile;
import com.treetalk.service.UserProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profile")
@Tag(name = "用户资料管理", description = "用户个人资料相关接口")
public class UserProfileController {

    private final UserProfileService userProfileService;

    @Autowired
    public UserProfileController(UserProfileService userProfileService) {
        this.userProfileService = userProfileService;
    }

    @GetMapping
    @Operation(summary = "获取用户资料", description = "获取当前登录用户的个人资料信息")
    public ResponseEntity<ApiResponse<UserProfile>> getUserProfile(
            @Parameter(hidden = true) @RequestAttribute("userId") Long userId) {
        UserProfile profile = userProfileService.getUserProfile(userId);
        return ResponseEntity.ok(ApiResponse.success("获取用户资料成功", profile));
    }

    @PutMapping
    @Operation(summary = "更新用户资料", description = "更新当前用户的个人资料信息")
    public ResponseEntity<ApiResponse<UserProfile>> updateUserProfile(
            @Parameter(hidden = true) @RequestAttribute("userId") Long userId,
            @Parameter(description = "用户资料更新信息", required = true)
            @RequestBody UserProfile updatedProfile) {
        try {
            UserProfile profile = userProfileService.updateUserProfile(userId, updatedProfile);
            return ResponseEntity.ok(ApiResponse.success("用户资料更新成功", profile));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    @PostMapping("/check-in")
    @Operation(summary = "用户签到", description = "用户每日签到，增加连续签到天数")
    public ResponseEntity<ApiResponse<String>> checkIn(
            @Parameter(hidden = true) @RequestAttribute("userId") Long userId) {
        try {
            userProfileService.incrementCheckInDays(userId);
            return ResponseEntity.ok(ApiResponse.success("签到成功", null));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(e.getMessage()));
        }
    }
}
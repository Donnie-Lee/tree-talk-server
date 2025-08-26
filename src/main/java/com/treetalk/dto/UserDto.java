package com.treetalk.dto;

import com.treetalk.entity.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;

public class UserDto {

    public static class RegisterRequest {
        @Email(message = "请输入有效的邮箱地址")
        @NotBlank(message = "邮箱不能为空")
        private String email;

        @NotBlank(message = "密码不能为空")
        @Size(min = 6, max = 20, message = "密码长度必须在6-20位之间")
        private String password;

        @NotBlank(message = "昵称不能为空")
        @Size(min = 2, max = 50, message = "昵称长度必须在2-50个字符之间")
        private String nickname;

        // Getters and Setters
        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }
    }

    public static class LoginRequest {
        @NotBlank(message = "用户名不能为空")
        private String username; // 可以是邮箱或手机号

        @NotBlank(message = "密码不能为空")
        private String password;

        // Getters and Setters
        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }

    public static class LoginResponse {
        private Long id;
        private String email;
        private String phone;
        private String nickname;
        private String avatarUrl;
        private Integer userType;
        private LocalDateTime registerTime;
        private LocalDateTime lastLoginTime;
        private String token;

        // Getters and Setters
        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getAvatarUrl() {
            return avatarUrl;
        }

        public void setAvatarUrl(String avatarUrl) {
            this.avatarUrl = avatarUrl;
        }

        public Integer getUserType() {
            return userType;
        }

        public void setUserType(Integer userType) {
            this.userType = userType;
        }

        public LocalDateTime getRegisterTime() {
            return registerTime;
        }

        public void setRegisterTime(LocalDateTime registerTime) {
            this.registerTime = registerTime;
        }

        public LocalDateTime getLastLoginTime() {
            return lastLoginTime;
        }

        public void setLastLoginTime(LocalDateTime lastLoginTime) {
            this.lastLoginTime = lastLoginTime;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }
    }

    @Data
    public static class UserProfileResponse {
        private Long id;
        private Long userId;
        private String userEmail;
        private String userNickname;
        private String userAvatarUrl;
        private Integer continuousCheckInDays;
        private Integer totalCheckInDays;
        private Integer totalMeditationMinutes;
        private Integer totalChatCount;
        private Integer privacySetting;
        private LocalDateTime createdTime;
        private LocalDateTime updatedTime;

    }
}
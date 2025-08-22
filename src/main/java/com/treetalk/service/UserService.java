package com.treetalk.service;

import com.treetalk.dto.UserDto;
import com.treetalk.entity.User;
import com.treetalk.entity.UserProfile;
import com.treetalk.repository.UserProfileRepository;
import com.treetalk.repository.UserRepository;
import com.treetalk.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Autowired
    public UserService(UserRepository userRepository, 
                      UserProfileRepository userProfileRepository,
                      PasswordEncoder passwordEncoder,
                      JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.userProfileRepository = userProfileRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public UserDto.LoginResponse register(UserDto.RegisterRequest request) {
        // 检查邮箱是否已存在
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("邮箱已被注册");
        }

        // 创建新用户
        User user = new User();
        user.setEmail(request.getEmail());
        user.setNickname(request.getNickname());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setUserType(0); // 普通用户

        user = userRepository.save(user);

        // 创建用户资料
        UserProfile profile = new UserProfile();
        profile.setUser(user);
        userProfileRepository.save(profile);

        return convertToResponse(user);
    }

    public String login(UserDto.LoginRequest request) {
        User user = userRepository.findByEmailOrPhone(request.getUsername())
                .orElseThrow(() -> new RuntimeException("用户不存在"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new RuntimeException("密码错误");
        }

        // 更新最后登录时间
        user.setLastLoginTime(java.time.LocalDateTime.now());
        userRepository.save(user);

        return jwtUtil.generateToken(user.getId().toString());
    }

    public UserDto.LoginResponse getUserById(Long userId) {
        User user = userRepository.findActiveUserById(userId)
                .orElseThrow(() -> new RuntimeException("用户不存在"));
        return convertToResponse(user);
    }

    private UserDto.LoginResponse convertToResponse(User user) {
        UserDto.LoginResponse response = new UserDto.LoginResponse();
        response.setId(user.getId());
        response.setEmail(user.getEmail());
        response.setPhone(user.getPhone());
        response.setNickname(user.getNickname());
        response.setAvatarUrl(user.getAvatarUrl());
        response.setUserType(user.getUserType());
        response.setRegisterTime(user.getRegisterTime());
        response.setLastLoginTime(user.getLastLoginTime());
        return response;
    }
}
package com.treetalk.service;

import com.treetalk.entity.User;
import com.treetalk.entity.UserProfile;
import com.treetalk.repository.UserProfileRepository;
import com.treetalk.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserProfileService {

    private final UserProfileRepository userProfileRepository;
    private final UserRepository userRepository;

    @Autowired
    public UserProfileService(UserProfileRepository userProfileRepository,
                             UserRepository userRepository) {
        this.userProfileRepository = userProfileRepository;
        this.userRepository = userRepository;
    }

    public UserProfile getUserProfile(Long userId) {
        return userProfileRepository.findActiveProfileByUserId(userId)
                .orElseGet(() -> {
                    // 如果用户资料不存在，创建一个新的
                    User user = userRepository.findActiveUserById(userId)
                            .orElseThrow(() -> new RuntimeException("用户不存在"));
                    
                    UserProfile profile = new UserProfile();
                    profile.setUser(user);
                    return userProfileRepository.save(profile);
                });
    }

    public UserProfile updateUserProfile(Long userId, UserProfile updatedProfile) {
        UserProfile profile = userProfileRepository.findActiveProfileByUserId(userId)
                .orElseThrow(() -> new RuntimeException("用户资料不存在"));

        if (!profile.getUser().getId().equals(userId)) {
            throw new RuntimeException("无权修改他人的用户资料");
        }

        // 更新允许修改的字段
        profile.setPrivacySetting(updatedProfile.getPrivacySetting());
        profile.setDiaryPassword(updatedProfile.getDiaryPassword());
        profile.setNotificationSetting(updatedProfile.getNotificationSetting());

        return userProfileRepository.save(profile);
    }

    public void incrementCheckInDays(Long userId) {
        UserProfile profile = userProfileRepository.findActiveProfileByUserId(userId)
                .orElseThrow(() -> new RuntimeException("用户资料不存在"));

        profile.setTotalCheckInDays(profile.getTotalCheckInDays() + 1);
        profile.setContinuousCheckInDays(profile.getContinuousCheckInDays() + 1);

        userProfileRepository.save(profile);
    }

    public void incrementMeditationMinutes(Long userId, int minutes) {
        UserProfile profile = userProfileRepository.findActiveProfileByUserId(userId)
                .orElseThrow(() -> new RuntimeException("用户资料不存在"));

        profile.setTotalMeditationMinutes(profile.getTotalMeditationMinutes() + minutes);
        userProfileRepository.save(profile);
    }

    public void incrementChatCount(Long userId) {
        UserProfile profile = userProfileRepository.findActiveProfileByUserId(userId)
                .orElseThrow(() -> new RuntimeException("用户资料不存在"));

        profile.setTotalChatCount(profile.getTotalChatCount() + 1);
        userProfileRepository.save(profile);
    }
}
package com.treetalk.entity;

import jakarta.persistence.*;
import com.fasterxml.jackson.databind.JsonNode;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "user_profiles")
public class UserProfile extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(name = "continuous_check_in_days", nullable = false)
    private Integer continuousCheckInDays = 0;

    @Column(name = "total_check_in_days", nullable = false)
    private Integer totalCheckInDays = 0;

    @Column(name = "total_meditation_minutes", nullable = false)
    private Integer totalMeditationMinutes = 0;

    @Column(name = "total_chat_count", nullable = false)
    private Integer totalChatCount = 0;

    @Column(name = "privacy_setting", nullable = false)
    private Integer privacySetting = 0; // 0-默认，1-日记加密

    @Column(name = "diary_password", length = 255)
    private String diaryPassword;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "notification_setting", columnDefinition = "jsonb")
    private JsonNode notificationSetting;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Integer getContinuousCheckInDays() {
        return continuousCheckInDays;
    }

    public void setContinuousCheckInDays(Integer continuousCheckInDays) {
        this.continuousCheckInDays = continuousCheckInDays;
    }

    public Integer getTotalCheckInDays() {
        return totalCheckInDays;
    }

    public void setTotalCheckInDays(Integer totalCheckInDays) {
        this.totalCheckInDays = totalCheckInDays;
    }

    public Integer getTotalMeditationMinutes() {
        return totalMeditationMinutes;
    }

    public void setTotalMeditationMinutes(Integer totalMeditationMinutes) {
        this.totalMeditationMinutes = totalMeditationMinutes;
    }

    public Integer getTotalChatCount() {
        return totalChatCount;
    }

    public void setTotalChatCount(Integer totalChatCount) {
        this.totalChatCount = totalChatCount;
    }

    public Integer getPrivacySetting() {
        return privacySetting;
    }

    public void setPrivacySetting(Integer privacySetting) {
        this.privacySetting = privacySetting;
    }

    public String getDiaryPassword() {
        return diaryPassword;
    }

    public void setDiaryPassword(String diaryPassword) {
        this.diaryPassword = diaryPassword;
    }

    public JsonNode getNotificationSetting() {
        return notificationSetting;
    }

    public void setNotificationSetting(JsonNode notificationSetting) {
        this.notificationSetting = notificationSetting;
    }
}
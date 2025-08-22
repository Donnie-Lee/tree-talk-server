package com.treetalk.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "emotion_records")
public class EmotionRecord extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "emotion_type", nullable = false)
    private Integer emotionType; // 1-开心，2-平静，3-焦虑，4-低落，5-愤怒，6-疲惫

    @Column(name = "emotion_value", nullable = false)
    private Integer emotionValue; // 1-5分

    @Column(name = "record_date", nullable = false)
    private LocalDate recordDate;

    @Column(name = "record_time", nullable = false)
    private LocalTime recordTime;

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

    public Integer getEmotionType() {
        return emotionType;
    }

    public void setEmotionType(Integer emotionType) {
        this.emotionType = emotionType;
    }

    public Integer getEmotionValue() {
        return emotionValue;
    }

    public void setEmotionValue(Integer emotionValue) {
        this.emotionValue = emotionValue;
    }

    public LocalDate getRecordDate() {
        return recordDate;
    }

    public void setRecordDate(LocalDate recordDate) {
        this.recordDate = recordDate;
    }

    public LocalTime getRecordTime() {
        return recordTime;
    }

    public void setRecordTime(LocalTime recordTime) {
        this.recordTime = recordTime;
    }
}
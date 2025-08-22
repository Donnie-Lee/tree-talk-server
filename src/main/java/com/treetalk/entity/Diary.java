package com.treetalk.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "diaries")
public class Diary extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "emotion_record_id")
    private EmotionRecord emotionRecord;

    @Column(name = "content", nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(name = "is_encrypted", nullable = false)
    private Boolean isEncrypted = false;

    @Column(name = "encrypted_content", columnDefinition = "TEXT")
    private String encryptedContent;

    @Column(name = "record_date", nullable = false)
    private java.time.LocalDate recordDate;

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

    public EmotionRecord getEmotionRecord() {
        return emotionRecord;
    }

    public void setEmotionRecord(EmotionRecord emotionRecord) {
        this.emotionRecord = emotionRecord;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Boolean getIsEncrypted() {
        return isEncrypted;
    }

    public void setIsEncrypted(Boolean isEncrypted) {
        this.isEncrypted = isEncrypted;
    }

    public String getEncryptedContent() {
        return encryptedContent;
    }

    public void setEncryptedContent(String encryptedContent) {
        this.encryptedContent = encryptedContent;
    }

    public java.time.LocalDate getRecordDate() {
        return recordDate;
    }

    public void setRecordDate(java.time.LocalDate recordDate) {
        this.recordDate = recordDate;
    }
}
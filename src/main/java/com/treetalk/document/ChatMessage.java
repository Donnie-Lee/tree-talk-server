package com.treetalk.document;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

@Document(collection = "chat_messages")
public class ChatMessage {

    @Id
    private String id;

    @Field("session_id")
    private Long sessionId;

    @Field("sender_type")
    private Integer senderType; // 0-用户，1-AI

    @Field("content")
    private String content;

    @Field("send_time")
    private LocalDateTime sendTime = LocalDateTime.now();

    @Field("is_voice")
    private Boolean isVoice = false;

    @Field("voice_url")
    private String voiceUrl;

    @Field("voice_duration")
    private Integer voiceDuration;

    @Field("emotion_analysis")
    private EmotionAnalysis emotionAnalysis;

    @Field("is_deleted")
    private Boolean isDeleted = false;

    // 内部类：情绪分析结果
    public static class EmotionAnalysis {
        private String primaryEmotion;
        private Double confidence;
        private java.util.List<String> keywords;

        public EmotionAnalysis() {}

        public EmotionAnalysis(String primaryEmotion, Double confidence, java.util.List<String> keywords) {
            this.primaryEmotion = primaryEmotion;
            this.confidence = confidence;
            this.keywords = keywords;
        }

        public String getPrimaryEmotion() {
            return primaryEmotion;
        }

        public void setPrimaryEmotion(String primaryEmotion) {
            this.primaryEmotion = primaryEmotion;
        }

        public Double getConfidence() {
            return confidence;
        }

        public void setConfidence(Double confidence) {
            this.confidence = confidence;
        }

        public java.util.List<String> getKeywords() {
            return keywords;
        }

        public void setKeywords(java.util.List<String> keywords) {
            this.keywords = keywords;
        }
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getSessionId() {
        return sessionId;
    }

    public void setSessionId(Long sessionId) {
        this.sessionId = sessionId;
    }

    public Integer getSenderType() {
        return senderType;
    }

    public void setSenderType(Integer senderType) {
        this.senderType = senderType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getSendTime() {
        return sendTime;
    }

    public void setSendTime(LocalDateTime sendTime) {
        this.sendTime = sendTime;
    }

    public Boolean getIsVoice() {
        return isVoice;
    }

    public void setIsVoice(Boolean isVoice) {
        this.isVoice = isVoice;
    }

    public String getVoiceUrl() {
        return voiceUrl;
    }

    public void setVoiceUrl(String voiceUrl) {
        this.voiceUrl = voiceUrl;
    }

    public Integer getVoiceDuration() {
        return voiceDuration;
    }

    public void setVoiceDuration(Integer voiceDuration) {
        this.voiceDuration = voiceDuration;
    }

    public EmotionAnalysis getEmotionAnalysis() {
        return emotionAnalysis;
    }

    public void setEmotionAnalysis(EmotionAnalysis emotionAnalysis) {
        this.emotionAnalysis = emotionAnalysis;
    }

    public Boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }
}
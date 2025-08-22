package com.treetalk.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.List;

public class ChatDto {

    public static class CreateSessionRequest {
        @NotBlank(message = "AI名称不能为空")
        @Size(max = 50, message = "AI名称不能超过50字符")
        private String aiName;

        @Size(max = 100, message = "会话标题不能超过100字符")
        private String sessionTitle;

        // Getters and Setters
        public String getAiName() {
            return aiName;
        }

        public void setAiName(String aiName) {
            this.aiName = aiName;
        }

        public String getSessionTitle() {
            return sessionTitle;
        }

        public void setSessionTitle(String sessionTitle) {
            this.sessionTitle = sessionTitle;
        }
    }

    public static class SendMessageRequest {
        @NotBlank(message = "消息内容不能为空")
        @Size(max = 1000, message = "消息内容不能超过1000字符")
        private String content;

        // Getters and Setters
        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }

    public static class SessionResponse {
        private Long id;
        private String aiName;
        private String sessionTitle;
        private LocalDateTime startTime;
        private LocalDateTime endTime;
        private int messageCount;

        // Getters and Setters
        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getAiName() {
            return aiName;
        }

        public void setAiName(String aiName) {
            this.aiName = aiName;
        }

        public String getSessionTitle() {
            return sessionTitle;
        }

        public void setSessionTitle(String sessionTitle) {
            this.sessionTitle = sessionTitle;
        }

        public LocalDateTime getStartTime() {
            return startTime;
        }

        public void setStartTime(LocalDateTime startTime) {
            this.startTime = startTime;
        }

        public LocalDateTime getEndTime() {
            return endTime;
        }

        public void setEndTime(LocalDateTime endTime) {
            this.endTime = endTime;
        }

        public int getMessageCount() {
            return messageCount;
        }

        public void setMessageCount(int messageCount) {
            this.messageCount = messageCount;
        }
    }

    public static class MessageResponse {
        private String id;
        private Long sessionId;
        private String senderType;
        private String content;
        private LocalDateTime sendTime;
        private boolean isVoice;
        private String voiceUrl;
        private Integer voiceDuration;

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

        public String getSenderType() {
            return senderType;
        }

        public void setSenderType(String senderType) {
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

        public boolean isVoice() {
            return isVoice;
        }

        public void setVoice(boolean voice) {
            isVoice = voice;
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
    }

    public static class AIResponse {
        private String content;
        private String emotion;
        private List<String> suggestions;

        // Getters and Setters
        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getEmotion() {
            return emotion;
        }

        public void setEmotion(String emotion) {
            this.emotion = emotion;
        }

        public List<String> getSuggestions() {
            return suggestions;
        }

        public void setSuggestions(List<String> suggestions) {
            this.suggestions = suggestions;
        }
    }
}
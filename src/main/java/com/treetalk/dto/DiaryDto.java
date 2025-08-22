package com.treetalk.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.List;

public class DiaryDto {

    public static class CreateRequest {
        @NotNull(message = "情绪记录ID不能为空")
        private Long emotionRecordId;

        @NotBlank(message = "日记内容不能为空")
        @Size(max = 2000, message = "日记内容不能超过2000字符")
        private String content;

        private boolean isEncrypted = false;
        private String encryptedContent;

        // Getters and Setters
        public Long getEmotionRecordId() {
            return emotionRecordId;
        }

        public void setEmotionRecordId(Long emotionRecordId) {
            this.emotionRecordId = emotionRecordId;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public boolean isEncrypted() {
            return isEncrypted;
        }

        public void setEncrypted(boolean encrypted) {
            isEncrypted = encrypted;
        }

        public String getEncryptedContent() {
            return encryptedContent;
        }

        public void setEncryptedContent(String encryptedContent) {
            this.encryptedContent = encryptedContent;
        }
    }

    public static class UpdateRequest {
        @NotBlank(message = "日记内容不能为空")
        @Size(max = 2000, message = "日记内容不能超过2000字符")
        private String content;

        private boolean isEncrypted = false;
        private String encryptedContent;

        // Getters and Setters
        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public boolean isEncrypted() {
            return isEncrypted;
        }

        public void setEncrypted(boolean encrypted) {
            isEncrypted = encrypted;
        }

        public String getEncryptedContent() {
            return encryptedContent;
        }

        public void setEncryptedContent(String encryptedContent) {
            this.encryptedContent = encryptedContent;
        }
    }

    public static class Response {
        private Long id;
        private Long emotionRecordId;
        private String content;
        private boolean isEncrypted;
        private String encryptedContent;
        private LocalDate recordDate;
        private EmotionRecordDto.Response emotionRecord;

        // Getters and Setters
        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public Long getEmotionRecordId() {
            return emotionRecordId;
        }

        public void setEmotionRecordId(Long emotionRecordId) {
            this.emotionRecordId = emotionRecordId;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public boolean isEncrypted() {
            return isEncrypted;
        }

        public void setEncrypted(boolean encrypted) {
            isEncrypted = encrypted;
        }

        public String getEncryptedContent() {
            return encryptedContent;
        }

        public void setEncryptedContent(String encryptedContent) {
            this.encryptedContent = encryptedContent;
        }

        public LocalDate getRecordDate() {
            return recordDate;
        }

        public void setRecordDate(LocalDate recordDate) {
            this.recordDate = recordDate;
        }

        public EmotionRecordDto.Response getEmotionRecord() {
            return emotionRecord;
        }

        public void setEmotionRecord(EmotionRecordDto.Response emotionRecord) {
            this.emotionRecord = emotionRecord;
        }
    }

    public static class ListResponse {
        private Long id;
        private LocalDate recordDate;
        private EmotionRecordDto.Response emotionRecord;
        private String previewContent;

        // Getters and Setters
        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public LocalDate getRecordDate() {
            return recordDate;
        }

        public void setRecordDate(LocalDate recordDate) {
            this.recordDate = recordDate;
        }

        public EmotionRecordDto.Response getEmotionRecord() {
            return emotionRecord;
        }

        public void setEmotionRecord(EmotionRecordDto.Response emotionRecord) {
            this.emotionRecord = emotionRecord;
        }

        public String getPreviewContent() {
            return previewContent;
        }

        public void setPreviewContent(String previewContent) {
            this.previewContent = previewContent;
        }
    }
}
package com.treetalk.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

import java.time.LocalDate;
import java.time.LocalTime;

public class EmotionRecordDto {

    public static class CreateRequest {
        @NotNull(message = "情绪类型不能为空")
        @Min(value = 1, message = "情绪类型必须在1-6之间")
        @Max(value = 6, message = "情绪类型必须在1-6之间")
        private Integer emotionType;

        @NotNull(message = "情绪值不能为空")
        @Min(value = -5, message = "情绪值必须在1-5之间")
        @Max(value = 5, message = "情绪值必须在1-5之间")
        private Integer emotionValue;

        // Getters and Setters
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
    }

    public static class Response {
        private Long id;
        private Integer emotionType;
        private Integer emotionValue;
        private LocalDate recordDate;
        private LocalTime recordTime;

        // Getters and Setters
        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
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
}
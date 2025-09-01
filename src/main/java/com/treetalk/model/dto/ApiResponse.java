package com.treetalk.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {
    private boolean success;
    private Integer code = 200;
    private String message;
    private T data;
    private Long timestamp;

    public ApiResponse() {
        this.timestamp = System.currentTimeMillis();
    }

    public ApiResponse(boolean success, String message) {
        this();
        this.success = success;
        this.message = message;
    }

    public ApiResponse(boolean success, Integer code, String message) {
        this();
        this.success = success;
        this.code = code;
        this.message = message;
    }

    public ApiResponse(boolean success, String message, T data) {
        this(success, message);
        this.data = data;
    }

    public ApiResponse(boolean success, Integer code, String message, T data) {
        this(success, code, message);
        this.data = data;
    }

    public static <T> ApiResponse<T> success() {
        return new ApiResponse<>(true, "操作成功");
    }

    public static <T> ApiResponse<T> message(String message) {
        return new ApiResponse<>(true, message);
    }

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(true, "操作成功", data);
    }

    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>(true, message, data);
    }

    public static <T> ApiResponse<T> error(String message) {
        return new ApiResponse<>(false, message);
    }

    public static <T> ApiResponse<T> error(String message, T data) {
        return new ApiResponse<>(false, 500, message, data);
    }

}
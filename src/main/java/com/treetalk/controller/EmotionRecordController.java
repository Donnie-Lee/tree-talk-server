package com.treetalk.controller;

import com.treetalk.dto.ApiResponse;
import com.treetalk.dto.EmotionRecordDto;
import com.treetalk.service.EmotionRecordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/emotion")
@Tag(name = "情绪记录管理", description = "用户情绪记录相关接口")
public class EmotionRecordController {

    private final EmotionRecordService emotionRecordService;

    @Autowired
    public EmotionRecordController(EmotionRecordService emotionRecordService) {
        this.emotionRecordService = emotionRecordService;
    }

    @PostMapping("/record")
    @Operation(summary = "创建情绪记录", description = "创建新的情绪记录")
    public ApiResponse<EmotionRecordDto.Response> createEmotionRecord(
            @Parameter(hidden = true) @RequestAttribute("userId") Long userId,
            @Parameter(description = "情绪记录请求信息", required = true)
            @Valid @RequestBody EmotionRecordDto.CreateRequest request) {
        EmotionRecordDto.Response response = emotionRecordService.createEmotionRecord(userId, request);
        return ApiResponse.success("情绪记录创建成功", response);
    }

    @GetMapping("/today")
    @Operation(summary = "获取今日情绪记录", description = "获取用户今日的情绪记录")
    public ApiResponse<EmotionRecordDto.Response> getTodayEmotionRecord(
            @Parameter(hidden = true) @RequestAttribute("userId") Long userId) {
        EmotionRecordDto.Response response = emotionRecordService.getTodayEmotionRecord(userId);
        return ApiResponse.success("获取今日情绪记录成功", response);
    }

    @GetMapping("/records")
    @Operation(summary = "按日期范围获取情绪记录", description = "根据指定日期范围获取情绪记录列表")
    public ApiResponse<List<EmotionRecordDto.Response>> getEmotionRecordsByDateRange(
            @Parameter(hidden = true) @RequestAttribute("userId") Long userId,
            @Parameter(description = "开始日期 (YYYY-MM-DD)", required = true, example = "2024-01-01") 
            @RequestParam("startDate") LocalDate startDate,
            @Parameter(description = "结束日期 (YYYY-MM-DD)", required = true, example = "2024-12-31") 
            @RequestParam("endDate") LocalDate endDate) {
        List<EmotionRecordDto.Response> responses = emotionRecordService
                .getEmotionRecordsByDateRange(userId, startDate, endDate);
        return ApiResponse.success("获取情绪记录列表成功", responses);
    }
}
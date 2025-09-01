package com.treetalk.controller.admin;

import com.treetalk.model.dto.ApiResponse;
import com.treetalk.model.entity.DailyEmotionRecord;
import com.treetalk.service.DailyEmotionRecordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/emotionrecords")
@Tag(name = "情绪记录管理", description = "用户日常情绪记录相关操作接口")
public class DailyEmotionRecordController {
    
    @Autowired
    private DailyEmotionRecordService emotionRecordService;
    
    @GetMapping
    @Operation(summary = "获取所有情绪记录", description = "获取系统中的所有情绪记录")
    public ApiResponse<List<DailyEmotionRecord>> getAllEmotionRecords() {
        List<DailyEmotionRecord> emotionRecords = emotionRecordService.getAllEmotionRecords();
        return ApiResponse.success(emotionRecords);
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "根据ID获取情绪记录", description = "根据记录ID获取特定情绪记录的信息")
    public ApiResponse<DailyEmotionRecord> getEmotionRecordById(
            @Parameter(description = "情绪记录ID") @PathVariable Long id) {
        return emotionRecordService.getEmotionRecordById(id)
                .map(ApiResponse::success)
                .orElseGet(() -> ApiResponse.error("EmotionRecord not found"));
    }
    
    @GetMapping("/account/{accountId}")
    @Operation(summary = "根据账户ID获取情绪记录", description = "根据账户ID获取该用户的所有情绪记录")
    public ApiResponse<List<DailyEmotionRecord>> getEmotionRecordsByAccountId(
            @Parameter(description = "账户ID") @PathVariable Long accountId) {
        List<DailyEmotionRecord> emotionRecords = emotionRecordService.getEmotionRecordsByAccountId(accountId);
        return ApiResponse.success(emotionRecords);
    }
    
    @PostMapping
    @Operation(summary = "创建情绪记录", description = "创建一个新的情绪记录")
    public ApiResponse<DailyEmotionRecord> createEmotionRecord(
            @Parameter(description = "情绪记录信息") @RequestBody DailyEmotionRecord emotionRecord) {
        DailyEmotionRecord savedEmotionRecord = emotionRecordService.saveEmotionRecord(emotionRecord);
        return ApiResponse.success("EmotionRecord created successfully", savedEmotionRecord);
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "更新情绪记录", description = "根据记录ID更新情绪记录信息")
    public ApiResponse<DailyEmotionRecord> updateEmotionRecord(
            @Parameter(description = "情绪记录ID") @PathVariable Long id,
            @Parameter(description = "更新的情绪记录信息") @RequestBody DailyEmotionRecord emotionRecord) {
        emotionRecord.setId(id);
        DailyEmotionRecord updatedEmotionRecord = emotionRecordService.saveEmotionRecord(emotionRecord);
        return ApiResponse.success("EmotionRecord updated successfully", updatedEmotionRecord);
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "删除情绪记录", description = "根据记录ID删除情绪记录")
    public ApiResponse<Void> deleteEmotionRecord(
            @Parameter(description = "情绪记录ID") @PathVariable Long id) {
        emotionRecordService.deleteEmotionRecord(id);
        return ApiResponse.success("EmotionRecord deleted successfully", null);
    }
}
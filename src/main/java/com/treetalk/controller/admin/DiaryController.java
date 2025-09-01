package com.treetalk.controller.admin;

import com.treetalk.model.dto.ApiResponse;
import com.treetalk.model.entity.Diary;
import com.treetalk.service.DiaryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/diaries")
@Tag(name = "日记管理", description = "用户日记相关操作接口")
public class DiaryController {
    
    @Autowired
    private DiaryService diaryService;
    
    @GetMapping
    @Operation(summary = "获取所有日记", description = "获取系统中的所有日记")
    public ApiResponse<List<Diary>> getAllDiaries() {
        List<Diary> diaries = diaryService.getAllDiaries();
        return ApiResponse.success(diaries);
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "根据ID获取日记", description = "根据日记ID获取特定日记的信息")
    public ApiResponse<Diary> getDiaryById(
            @Parameter(description = "日记ID") @PathVariable Long id) {
        return diaryService.getDiaryById(id)
                .map(diary -> ApiResponse.success(diary))
                .orElseGet(() -> ApiResponse.error("Diary not found"));
    }
    
    @GetMapping("/account/{accountId}")
    @Operation(summary = "根据账户ID获取日记", description = "根据账户ID获取该用户的所有日记")
    public ApiResponse<List<Diary>> getDiariesByAccountId(
            @Parameter(description = "账户ID") @PathVariable Long accountId) {
        List<Diary> diaries = diaryService.getDiariesByAccountId(accountId);
        return ApiResponse.success(diaries);
    }
    
    @PostMapping
    @Operation(summary = "创建日记", description = "创建一篇新的日记")
    public ApiResponse<Diary> createDiary(
            @Parameter(description = "日记信息") @RequestBody Diary diary) {
        Diary savedDiary = diaryService.saveDiary(diary);
        return ApiResponse.success("Diary created successfully", savedDiary);
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "更新日记", description = "根据日记ID更新日记内容")
    public ApiResponse<Diary> updateDiary(
            @Parameter(description = "日记ID") @PathVariable Long id,
            @Parameter(description = "更新的日记信息") @RequestBody Diary diary) {
        diary.setId(id);
        Diary updatedDiary = diaryService.saveDiary(diary);
        return ApiResponse.success("Diary updated successfully", updatedDiary);
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "删除日记", description = "根据日记ID删除日记")
    public ApiResponse<Void> deleteDiary(
            @Parameter(description = "日记ID") @PathVariable Long id) {
        diaryService.deleteDiary(id);
        return ApiResponse.success("Diary deleted successfully", null);
    }
}
package com.treetalk.controller;

import com.treetalk.dto.ApiResponse;
import com.treetalk.dto.DiaryDto;
import com.treetalk.service.DiaryService;
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
@RequestMapping("/api/diary")
@Tag(name = "日记管理", description = "用户日记相关接口")
public class DiaryController {

    private final DiaryService diaryService;

    @Autowired
    public DiaryController(DiaryService diaryService) {
        this.diaryService = diaryService;
    }

    @PostMapping
    @Operation(summary = "创建日记", description = "创建新的日记记录")
    public ResponseEntity<ApiResponse<DiaryDto.Response>> createDiary(
            @Parameter(hidden = true) @RequestAttribute("userId") Long userId,
            @Parameter(description = "日记创建请求信息", required = true)
            @Valid @RequestBody DiaryDto.CreateRequest request) {
        try {
            DiaryDto.Response response = diaryService.createDiary(userId, request);
            return ResponseEntity.ok(ApiResponse.success("日记创建成功", response));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    @PutMapping("/{diaryId}")
    @Operation(summary = "更新日记", description = "更新指定日记的内容")
    public ResponseEntity<ApiResponse<DiaryDto.Response>> updateDiary(
            @Parameter(hidden = true) @RequestAttribute("userId") Long userId,
            @Parameter(description = "日记ID", required = true) @PathVariable Long diaryId,
            @Parameter(description = "日记更新请求信息", required = true)
            @Valid @RequestBody DiaryDto.UpdateRequest request) {
        try {
            DiaryDto.Response response = diaryService.updateDiary(userId, diaryId, request);
            return ResponseEntity.ok(ApiResponse.success("日记更新成功", response));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/{diaryId}")
    @Operation(summary = "获取日记详情", description = "根据日记ID获取日记详细信息")
    public ResponseEntity<ApiResponse<DiaryDto.Response>> getDiaryById(
            @Parameter(hidden = true) @RequestAttribute("userId") Long userId,
            @Parameter(description = "日记ID", required = true) @PathVariable Long diaryId) {
        try {
            DiaryDto.Response response = diaryService.getDiaryById(userId, diaryId);
            return ResponseEntity.ok(ApiResponse.success("获取日记成功", response));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping
    @Operation(summary = "获取日记列表", description = "获取当前用户的所有日记列表")
    public ResponseEntity<ApiResponse<List<DiaryDto.ListResponse>>> getAllDiaries(
            @Parameter(hidden = true) @RequestAttribute("userId") Long userId) {
        List<DiaryDto.ListResponse> responses = diaryService.getAllDiaries(userId);
        return ResponseEntity.ok(ApiResponse.success("获取日记列表成功", responses));
    }

    @GetMapping("/range")
    @Operation(summary = "按日期范围获取日记", description = "根据指定的日期范围获取日记列表")
    public ResponseEntity<ApiResponse<List<DiaryDto.ListResponse>>> getDiariesByDateRange(
            @Parameter(hidden = true) @RequestAttribute("userId") Long userId,
            @Parameter(description = "开始日期 (YYYY-MM-DD)", required = true, example = "2024-01-01") 
            @RequestParam("startDate") LocalDate startDate,
            @Parameter(description = "结束日期 (YYYY-MM-DD)", required = true, example = "2024-12-31") 
            @RequestParam("endDate") LocalDate endDate) {
        List<DiaryDto.ListResponse> responses = diaryService.getDiariesByDateRange(userId, startDate, endDate);
        return ResponseEntity.ok(ApiResponse.success("获取日期范围内日记成功", responses));
    }

    @DeleteMapping("/{diaryId}")
    @Operation(summary = "删除日记", description = "删除指定的日记记录")
    public ResponseEntity<ApiResponse<String>> deleteDiary(
            @Parameter(hidden = true) @RequestAttribute("userId") Long userId,
            @Parameter(description = "日记ID", required = true) @PathVariable Long diaryId) {
        try {
            diaryService.deleteDiary(userId, diaryId);
            return ResponseEntity.ok(ApiResponse.success("日记删除成功", null));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(e.getMessage()));
        }
    }
}
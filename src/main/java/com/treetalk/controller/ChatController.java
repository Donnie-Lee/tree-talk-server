package com.treetalk.controller;

import com.treetalk.dto.ApiResponse;
import com.treetalk.dto.ChatDto;
import com.treetalk.service.ChatService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chat")
@Tag(name = "AI聊天管理", description = "AI聊天会话相关接口")
public class ChatController {

    private final ChatService chatService;

    @Autowired
    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @PostMapping("/session")
    @Operation(summary = "创建聊天会话", description = "创建新的AI聊天会话")
    public ResponseEntity<ApiResponse<ChatDto.SessionResponse>> createSession(
            @Parameter(hidden = true) @RequestAttribute("userId") Long userId,
            @Parameter(description = "会话创建请求信息", required = true)
            @Valid @RequestBody ChatDto.CreateSessionRequest request) {
        try {
            ChatDto.SessionResponse response = chatService.createSession(userId, request);
            return ResponseEntity.ok(ApiResponse.success("会话创建成功", response));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/sessions")
    @Operation(summary = "获取用户会话列表", description = "获取当前用户的所有AI聊天会话")
    public ResponseEntity<ApiResponse<List<ChatDto.SessionResponse>>> getUserSessions(
            @Parameter(hidden = true) @RequestAttribute("userId") Long userId) {
        List<ChatDto.SessionResponse> responses = chatService.getUserSessions(userId);
        return ResponseEntity.ok(ApiResponse.success("获取会话列表成功", responses));
    }

    @GetMapping("/session/{sessionId}")
    @Operation(summary = "获取会话详情", description = "根据会话ID获取聊天会话的详细信息")
    public ResponseEntity<ApiResponse<ChatDto.SessionResponse>> getSession(
            @Parameter(hidden = true) @RequestAttribute("userId") Long userId,
            @Parameter(description = "会话ID", required = true) @PathVariable Long sessionId) {
        try {
            ChatDto.SessionResponse response = chatService.getSession(userId, sessionId);
            return ResponseEntity.ok(ApiResponse.success("获取会话成功", response));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    @PostMapping("/session/{sessionId}/message")
    @Operation(summary = "发送消息", description = "在指定会话中发送消息给AI")
    public ResponseEntity<ApiResponse<ChatDto.MessageResponse>> sendMessage(
            @Parameter(hidden = true) @RequestAttribute("userId") Long userId,
            @Parameter(description = "会话ID", required = true) @PathVariable Long sessionId,
            @Parameter(description = "消息发送请求信息", required = true)
            @Valid @RequestBody ChatDto.SendMessageRequest request) {
        try {
            ChatDto.MessageResponse response = chatService.sendMessage(userId, sessionId, request);
            return ResponseEntity.ok(ApiResponse.success("消息发送成功", response));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/session/{sessionId}/messages")
    @Operation(summary = "获取会话消息", description = "分页获取指定会话的消息列表")
    public ResponseEntity<ApiResponse<List<ChatDto.MessageResponse>>> getMessages(
            @Parameter(hidden = true) @RequestAttribute("userId") Long userId,
            @Parameter(description = "会话ID", required = true) @PathVariable Long sessionId,
            @Parameter(description = "页码，从0开始", example = "0") 
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "每页数量", example = "20") 
            @RequestParam(defaultValue = "20") int size) {
        try {
            List<ChatDto.MessageResponse> responses = chatService.getMessages(userId, sessionId, page, size);
            return ResponseEntity.ok(ApiResponse.success("获取消息列表成功", responses));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    @DeleteMapping("/session/{sessionId}")
    @Operation(summary = "删除会话", description = "删除指定的聊天会话及其消息记录")
    public ResponseEntity<ApiResponse<String>> deleteSession(
            @Parameter(hidden = true) @RequestAttribute("userId") Long userId,
            @Parameter(description = "会话ID", required = true) @PathVariable Long sessionId) {
        try {
            chatService.deleteSession(userId, sessionId);
            return ResponseEntity.ok(ApiResponse.success("会话删除成功", null));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(e.getMessage()));
        }
    }
}
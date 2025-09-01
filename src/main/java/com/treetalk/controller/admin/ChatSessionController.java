package com.treetalk.controller.admin;

import com.treetalk.model.dto.ApiResponse;
import com.treetalk.model.entity.ChatSession;
import com.treetalk.service.ChatSessionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chatsessions")
@Tag(name = "聊天会话管理", description = "AI聊天会话相关操作接口")
public class ChatSessionController {
    
    @Autowired
    private ChatSessionService chatSessionService;
    
    @GetMapping
    @Operation(summary = "获取所有聊天会话", description = "获取系统中的所有聊天会话")
    public ApiResponse<List<ChatSession>> getAllChatSessions() {
        List<ChatSession> chatSessions = chatSessionService.getAllChatSessions();
        return ApiResponse.success(chatSessions);
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "根据ID获取聊天会话", description = "根据会话ID获取特定聊天会话的信息")
    public ApiResponse<ChatSession> getChatSessionById(
            @Parameter(description = "聊天会话ID") @PathVariable Long id) {
        return chatSessionService.getChatSessionById(id)
                .map(chatSession -> ApiResponse.success(chatSession))
                .orElseGet(() -> ApiResponse.error("ChatSession not found"));
    }
    
    @GetMapping("/account/{accountId}")
    @Operation(summary = "根据账户ID获取聊天会话", description = "根据账户ID获取该用户的所有聊天会话")
    public ApiResponse<List<ChatSession>> getChatSessionsByAccountId(
            @Parameter(description = "账户ID") @PathVariable Long accountId) {
        List<ChatSession> chatSessions = chatSessionService.getChatSessionsByAccountId(accountId);
        return ApiResponse.success(chatSessions);
    }
    
    @PostMapping
    @Operation(summary = "创建聊天会话", description = "创建一个新的聊天会话")
    public ApiResponse<ChatSession> createChatSession(
            @Parameter(description = "聊天会话信息") @RequestBody ChatSession chatSession) {
        ChatSession savedChatSession = chatSessionService.saveChatSession(chatSession);
        return ApiResponse.success("ChatSession created successfully", savedChatSession);
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "更新聊天会话", description = "根据会话ID更新聊天会话信息")
    public ApiResponse<ChatSession> updateChatSession(
            @Parameter(description = "聊天会话ID") @PathVariable Long id,
            @Parameter(description = "更新的聊天会话信息") @RequestBody ChatSession chatSession) {
        chatSession.setId(id);
        ChatSession updatedChatSession = chatSessionService.saveChatSession(chatSession);
        return ApiResponse.success("ChatSession updated successfully", updatedChatSession);
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "删除聊天会话", description = "根据会话ID删除聊天会话")
    public ApiResponse<Void> deleteChatSession(
            @Parameter(description = "聊天会话ID") @PathVariable Long id) {
        chatSessionService.deleteChatSession(id);
        return ApiResponse.success("ChatSession deleted successfully", null);
    }
}
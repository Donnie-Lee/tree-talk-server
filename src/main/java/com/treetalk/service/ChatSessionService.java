package com.treetalk.service;

import com.treetalk.model.entity.ChatSession;

import java.util.List;
import java.util.Optional;

public interface ChatSessionService {
    List<ChatSession> getAllChatSessions();
    List<ChatSession> getChatSessionsByAccountId(Long accountId);
    Optional<ChatSession> getChatSessionById(Long id);
    ChatSession saveChatSession(ChatSession chatSession);
    void deleteChatSession(Long id);
}
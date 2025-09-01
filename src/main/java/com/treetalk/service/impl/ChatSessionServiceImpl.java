package com.treetalk.service.impl;

import com.treetalk.model.entity.ChatSession;
import com.treetalk.repository.ChatSessionRepository;
import com.treetalk.service.ChatSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ChatSessionServiceImpl implements ChatSessionService {
    
    @Autowired
    private ChatSessionRepository chatSessionRepository;
    
    @Override
    public List<ChatSession> getAllChatSessions() {
        return chatSessionRepository.findAll();
    }
    
    @Override
    public List<ChatSession> getChatSessionsByAccountId(Long accountId) {
        return chatSessionRepository.findByAccountId(accountId);
    }
    
    @Override
    public Optional<ChatSession> getChatSessionById(Long id) {
        return chatSessionRepository.findById(id);
    }
    
    @Override
    public ChatSession saveChatSession(ChatSession chatSession) {
        return chatSessionRepository.save(chatSession);
    }
    
    @Override
    public void deleteChatSession(Long id) {
        chatSessionRepository.deleteById(id);
    }
}
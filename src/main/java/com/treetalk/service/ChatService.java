package com.treetalk.service;

import com.treetalk.document.ChatMessage;
import com.treetalk.dto.ChatDto;
import com.treetalk.entity.ChatSession;
import com.treetalk.entity.User;
import com.treetalk.repository.ChatMessageRepository;
import com.treetalk.repository.ChatSessionRepository;
import com.treetalk.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChatService {

    private final ChatSessionRepository chatSessionRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final UserRepository userRepository;

    @Autowired
    public ChatService(ChatSessionRepository chatSessionRepository,
                      ChatMessageRepository chatMessageRepository,
                      UserRepository userRepository) {
        this.chatSessionRepository = chatSessionRepository;
        this.chatMessageRepository = chatMessageRepository;
        this.userRepository = userRepository;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public ChatDto.SessionResponse createSession(Long userId, ChatDto.CreateSessionRequest request) {
        User user = userRepository.findActiveUserById(userId)
                .orElseThrow(() -> new RuntimeException("用户不存在"));

        ChatSession session = new ChatSession();
        session.setUser(user);
        session.setAiName(request.getAiName());
        session.setSessionTitle(request.getSessionTitle() != null ? request.getSessionTitle() : "新对话");

        session = chatSessionRepository.save(session);
        return convertToSessionResponse(session);
    }

    public List<ChatDto.SessionResponse> getUserSessions(Long userId) {
        List<ChatSession> sessions = chatSessionRepository.findByUserIdOrderByStartTimeDesc(userId);
        return sessions.stream()
                .map(this::convertToSessionResponse)
                .collect(Collectors.toList());
    }

    public ChatDto.SessionResponse getSession(Long userId, Long sessionId) {
        ChatSession session = chatSessionRepository.findByUserIdAndSessionId(userId, sessionId)
                .orElseThrow(() -> new RuntimeException("会话不存在"));
        return convertToSessionResponse(session);
    }

    public ChatDto.MessageResponse sendMessage(Long userId, Long sessionId, ChatDto.SendMessageRequest request) {
        ChatSession session = chatSessionRepository.findByUserIdAndSessionId(userId, sessionId)
                .orElseThrow(() -> new RuntimeException("会话不存在"));

        // 保存用户消息
        ChatMessage userMessage = new ChatMessage();
        userMessage.setSessionId(sessionId);
        userMessage.setSenderType(0); // 0表示用户
        userMessage.setContent(request.getContent());
        userMessage.setSendTime(LocalDateTime.now());
        userMessage.setIsDeleted(false);

        userMessage = chatMessageRepository.save(userMessage);

        // 模拟AI回复（实际项目中这里会调用AI服务）
        String aiResponse = generateAIResponse(request.getContent());
        
        ChatMessage aiMessage = new ChatMessage();
        aiMessage.setSessionId(sessionId);
        aiMessage.setSenderType(1); // 1表示AI
        aiMessage.setContent(aiResponse);
        aiMessage.setSendTime(LocalDateTime.now());
        aiMessage.setIsDeleted(false);

        aiMessage = chatMessageRepository.save(aiMessage);

        // 更新会话消息数量
        long messageCount = chatMessageRepository.countActiveMessagesBySessionId(sessionId);
        session.setMessageCount((int) messageCount);
        chatSessionRepository.save(session);

        return convertToMessageResponse(aiMessage);
    }

    public List<ChatDto.MessageResponse> getMessages(Long userId, Long sessionId, int page, int size) {
        ChatSession session = chatSessionRepository.findByUserIdAndSessionId(userId, sessionId)
                .orElseThrow(() -> new RuntimeException("会话不存在"));

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "sendTime"));
        List<ChatMessage> messages = chatMessageRepository.findBySessionIdAndIsDeletedFalseOrderBySendTimeDesc(sessionId, pageable).getContent();
        
        return messages.stream()
                .map(this::convertToMessageResponse)
                .collect(Collectors.toList());
    }

    public void deleteSession(Long userId, Long sessionId) {
        ChatSession session = chatSessionRepository.findByUserIdAndSessionId(userId, sessionId)
                .orElseThrow(() -> new RuntimeException("会话不存在"));

        // 注意：ChatSession没有deleted字段，这里只需要删除会话记录
        chatSessionRepository.delete(session);
    }

    private String generateAIResponse(String userMessage) {
        // 这里应该是调用实际的AI服务，现在用模拟回复
        String[] responses = {
            "我理解你的感受，让我们一起聊聊这个话题。",
            "听起来你经历了很多，你愿意多分享一些吗？",
            "你的感受很重要，我在这里倾听你。",
            "每个人都会有这样的时刻，这很正常。",
            "谢谢你愿意和我分享这些想法。"
        };
        return responses[(int) (Math.random() * responses.length)];
    }

    private ChatDto.SessionResponse convertToSessionResponse(ChatSession session) {
        ChatDto.SessionResponse response = new ChatDto.SessionResponse();
        response.setId(session.getId());
        response.setUserId(session.getUser().getId());
        response.setUserNickname(session.getUser().getNickname());
        response.setAiName(session.getAiName());
        response.setSessionTitle(session.getSessionTitle());
        response.setStartTime(session.getStartTime());
        response.setEndTime(session.getEndTime());
        response.setMessageCount(session.getMessageCount());
        return response;
    }

    private ChatDto.MessageResponse convertToMessageResponse(ChatMessage message) {
        ChatDto.MessageResponse response = new ChatDto.MessageResponse();
        response.setId(message.getId());
        response.setSessionId(message.getSessionId());
        response.setSenderType(message.getSenderType() == 0 ? "USER" : "AI");
        response.setContent(message.getContent());
        response.setSendTime(message.getSendTime());
        response.setVoice(message.getIsVoice());
        response.setVoiceUrl(message.getVoiceUrl());
        response.setVoiceDuration(message.getVoiceDuration());
        return response;
    }
}
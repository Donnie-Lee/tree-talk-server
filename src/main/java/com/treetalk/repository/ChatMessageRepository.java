package com.treetalk.repository;

import com.treetalk.document.ChatMessage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ChatMessageRepository extends MongoRepository<ChatMessage, String> {

    List<ChatMessage> findBySessionIdOrderBySendTimeAsc(Long sessionId);

    Page<ChatMessage> findBySessionIdAndIsDeletedFalseOrderBySendTimeDesc(Long sessionId, Pageable pageable);

    @Query("{'sessionId': ?0, 'sendTime': {$gte: ?1, $lte: ?2}, 'isDeleted': false}")
    List<ChatMessage> findBySessionIdAndSendTimeBetween(Long sessionId, LocalDateTime start, LocalDateTime end);

    long countBySessionIdAndIsDeletedFalse(Long sessionId);

    @Query(value = "{'sessionId': ?0, 'isDeleted': false}", count = true)
    long countActiveMessagesBySessionId(Long sessionId);
}
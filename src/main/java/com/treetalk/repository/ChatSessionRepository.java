package com.treetalk.repository;

import com.treetalk.entity.ChatSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatSessionRepository extends JpaRepository<ChatSession, Long> {

    @Query("SELECT cs FROM ChatSession cs WHERE cs.user.id = :userId AND cs.isDeleted = false ORDER BY cs.startTime DESC")
    List<ChatSession> findByUserIdOrderByStartTimeDesc(@Param("userId") Long userId);

    @Query("SELECT cs FROM ChatSession cs WHERE cs.user.id = :userId AND cs.id = :sessionId AND cs.isDeleted = false")
    Optional<ChatSession> findByUserIdAndSessionId(@Param("userId") Long userId, @Param("sessionId") Long sessionId);

    @Query("SELECT COUNT(cs) FROM ChatSession cs WHERE cs.user.id = :userId AND cs.isDeleted = false")
    long countByUserId(@Param("userId") Long userId);

    @Query("SELECT cs FROM ChatSession cs WHERE cs.id = :id AND cs.isDeleted = false")
    Optional<ChatSession> findActiveSessionById(@Param("id") Long id);
}
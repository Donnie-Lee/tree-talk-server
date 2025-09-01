package com.treetalk.model.entity;

import jakarta.persistence.*;
import lombok.Data;

/**
 * AI聊天消息实体类
 *
 * @author lizheng
 * @created 2025/8/29 11:25
 */
@Entity
@Table(name = "chat_message")
@Data
public class ChatMessage extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 会话ID
     */
    @Column(name = "session_id", nullable = false, length = 50)
    private String sessionId;

    /**
     * 关联账户
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    /**
     * 角色：ASSISTANT, USER, SYSTEM
     */
    @Column(name = "role", nullable = false, length = 20)
    private String role;

    /**
     * 内容
     */
    @Column(name = "content", nullable = false, columnDefinition = "TEXT")
    private String content;

}
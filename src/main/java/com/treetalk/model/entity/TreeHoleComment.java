package com.treetalk.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 树洞评论实体类
 *
 * @author lizheng
 * @created 2025/8/29 11:17
 */
@Entity
@Table(name = "tree_hole_comment")
@Data
public class TreeHoleComment extends BaseEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 评论日期
     */
    @Column(name = "comment_date", nullable = false)
    private LocalDateTime commentDate;

    /**
     * 评论内容
     */
    @Column(name = "content", nullable = false, columnDefinition = "TEXT")
    private String content;

    /**
     * 关联账户
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    /**
     * 关联树洞
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tree_hole_id", nullable = false)
    private TreeHole treeHole;

    /**
     * 父级评论（用于支持评论回复功能）
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private TreeHoleComment parentComment;
}
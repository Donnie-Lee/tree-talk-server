package com.treetalk.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 树洞实体类
 *
 * @author lizheng
 * @created 2025/8/29 11:14
 */
@Entity
@Table(name = "tree_hole")
@Data
public class TreeHole extends BaseEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 发布日期
     */
    @Column(name = "post_date", nullable = false)
    private LocalDateTime postDate;

    /**
     * 树洞内容
     */
    @Column(name = "content", nullable = false, columnDefinition = "TEXT")
    private String content;

    /**
     * 关联用户
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    /**
     * 是否匿名发布
     */
    @Column(name = "is_anonymous", nullable = false)
    private Boolean isAnonymous = false;

    /**
     * 回复数
     */
    @Column(name = "reply_count", nullable = false)
    private Integer replyCount = 0;

    /**
     * 点赞数
     */
    @Column(name = "like_count", nullable = false)
    private Integer likeCount = 0;

    /**
     * 收藏数
     */
    @Column(name = "favorite_count", nullable = false)
    private Integer favoriteCount = 0;
}
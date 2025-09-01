package com.treetalk.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 树洞点赞实体类
 *
 * @author lizheng
 * @created 2025/8/29 11:20
 */
@Entity
@Table(name = "tree_hole_like")
@Data
public class TreeHoleLike extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 点赞日期
     */
    @Column(name = "like_date", nullable = false)
    private LocalDateTime likeDate;

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
}
package com.treetalk.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 树洞收藏实体类
 *
 * @author lizheng
 * @created 2025/8/29 11:20
 */
@Entity
@Table(name = "tree_hole_favorite")
@Data
public class TreeHoleFavorite extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 收藏日期
     */
    @Column(name = "favorite_date", nullable = false)
    private LocalDateTime favoriteDate;

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
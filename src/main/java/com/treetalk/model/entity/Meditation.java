package com.treetalk.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 冥想实体类
 *
 * @author lizheng
 * @created 2025/8/29 11:25
 */
@Entity
@Table(name = "meditation")
@Data
public class Meditation extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 冥想名称
     */
    @Column(name = "name", nullable = false, length = 100)
    private String name;

    /**
     * 描述
     */
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    /**
     * 图标
     */
    @Column(name = "icon", length = 255)
    private String icon;

    /**
     * 排序
     */
    @Column(name = "sort_order")
    private Integer sortOrder = 0;

    /**
     * 是否启用
     */
    @Column(name = "is_enabled")
    private Boolean isEnabled = true;

    /**
     * 置顶（时间类型）
     */
    @Column(name = "top_time")
    private LocalDateTime topTime;
}
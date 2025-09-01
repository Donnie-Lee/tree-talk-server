package com.treetalk.model.entity;

import jakarta.persistence.*;
import lombok.Data;

/**
 * 呼吸练习阶段实体类
 *
 * @author lizheng
 * @created 2025/8/29 11:25
 */
@Entity
@Table(name = "breathing_exercise_phase")
@Data
public class BreathingExercisePhase extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 关联的呼吸练习
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "breathing_exercise_id", nullable = false)
    private BreathingExercise breathingExercise;

    /**
     * 阶段名称
     */
    @Column(name = "name", nullable = false, length = 100)
    private String name;

    /**
     * 描述
     */
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    /**
     * 排序
     */
    @Column(name = "sort_order")
    private Integer sortOrder = 0;

    /**
     * 时间（秒）
     */
    @Column(name = "duration_seconds")
    private Integer durationSeconds = 0;

    /**
     * 是否启用
     */
    @Column(name = "is_enabled")
    private Boolean isEnabled = true;
}
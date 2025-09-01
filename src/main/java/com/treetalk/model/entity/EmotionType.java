package com.treetalk.model.entity;

import jakarta.persistence.*;
import lombok.Data;

/**
 * 类描述：
 *
 * @author lizheng
 * @created 2025/8/27 17:15
 */
@Entity
@Table(name = "emotion_type")
@Data
public class EmotionType extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "emotion_name", nullable = false)
    private String emotionName;

    @Column(name = "emotion_value", nullable = false)
    private Integer emotionValue;

    @Column(name = "color", nullable = false)
    private String color;

    @Column(name = "icon", nullable = false)
    private String icon;

}

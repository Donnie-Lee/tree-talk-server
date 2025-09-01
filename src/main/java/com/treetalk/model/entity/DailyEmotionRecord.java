package com.treetalk.model.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "emotion_records")
@Data
public class DailyEmotionRecord extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "emotion_type_id", nullable = false)
    private EmotionType emotionType;

    @Column(name = "record_time", nullable = false)
    private LocalDateTime recordTime;


}
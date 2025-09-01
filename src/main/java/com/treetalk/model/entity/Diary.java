package com.treetalk.model.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "diaries")
@Data
public class Diary extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "emotion_record_id")
    private DailyEmotionRecord emotionRecord;

    @Column(name = "content", nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(name = "is_encrypted", nullable = false)
    private Boolean isEncrypted = false;

    @Column(name = "encrypted_content", columnDefinition = "TEXT")
    private String encryptedContent;

    @Column(name = "record_date", nullable = false)
    private java.time.LocalDate recordDate;

}
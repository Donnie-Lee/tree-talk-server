package com.treetalk.model.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "account_profiles")
@Data
public class AccountProfile extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "account_id", nullable = false, unique = true)
    private Account account;

    // 账户设置字段
    @Column(name = "total_check_in_days")
    private Integer totalCheckInDays = 0;

    @Column(name = "continuous_check_in_days")
    private Integer continuousCheckInDays = 0;

    @Column(name = "last_check_in_time")
    private LocalDateTime lastCheckInTime;

    @Column(name = "privacy_setting")
    private Integer privacySetting = 0; // 0-公开 1-仅自己可见

    // 加密设置
    @Column(name = "is_encrypted")
    private Boolean isEncrypted = false; // 是否加密

    @Column(name = "encryption_password", length = 255)
    private String encryptionPassword; // 加密密码
}
package com.treetalk.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.treetalk.model.entity.Account;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * 类描述：
 *
 * @author lizheng
 * @created 2025/9/1 09:58
 */
@Data
public class AccountProfileInfo {

    // 手机号
    @Column(name = "username")
    private String username;

    @Column(name = "avatar")
    private String avatar;

    @Column(name = "nickname", length = 50)
    private String nickname;

    @Column(name = "gender")
    private Integer gender; // 0-未知 1-男 2-女

    @Column(name = "id_card", length = 18)
    private String idCard;

    @Column(name = "address", length = 255)
    private String address;

    @Column(name = "birthday")
    private String birthday;

    @Column(name = "signature", length = 255)
    private String signature;

    @Column(name = "last_login_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastLoginTime;

    // 账户设置字段
    @Column(name = "total_check_in_days")
    private Integer totalCheckInDays = 0;

    @Column(name = "continuous_check_in_days")
    private Integer continuousCheckInDays = 0;

    @Column(name = "last_check_in_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastCheckInTime;

    @Column(name = "privacy_setting")
    private Integer privacySetting = 0; // 0-公开 1-仅自己可见

    // 加密设置
    @Column(name = "is_encrypted")
    private Boolean isEncrypted = false; // 是否加密

    @Column(name = "encryption_password", length = 255)
    private String encryptionPassword; // 加密密码

}
package com.treetalk.model.entity;

import jakarta.persistence.*;
import lombok.Data;

/**
 * 验证码配置实体类
 * 用于存储不同场景下的验证码配置信息
 *
 * @author lizheng
 * @created 2025/8/29 15:17
 */
@Entity
@Table(name = "check_code_config")
@Data
public class CheckCodeConfig {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * 场景编码
     * 用于区分不同业务场景的验证码配置
     */
    @Column(name = "scene_code", length = 50, nullable = false, unique = true)
    private String sceneCode;
    
    /**
     * 验证码位数
     * 指定生成验证码的长度
     */
    @Column(name = "code_length", nullable = false)
    private Integer codeLength;
    
    /**
     * 验证码类型
     * 1: 数字 
     * 2：字母 
     * 3：数字+字母
     */
    @Column(name = "code_type", nullable = false)
    private Integer codeType;
    
    /**
     * 过期时间（分钟）
     * 验证码的有效期
     */
    @Column(name = "expire_minutes", nullable = false)
    private Integer expireMinutes;
    
    /**
     * 发送间隔（秒）
     * 同一场景下发送验证码的最小间隔
     */
    @Column(name = "send_interval", nullable = false)
    private Integer sendInterval;
    
    /**
     * 每日限制次数
     * 每个手机号每日发送验证码的上限
     */
    @Column(name = "daily_limit", nullable = false)
    private Integer dailyLimit;
    
    /**
     * 是否启用
     * 0: 禁用 1: 启用
     */
    @Column(name = "is_enabled", nullable = false)
    private Boolean isEnabled = Boolean.TRUE;
    
    /**
     * 描述信息
     */
    @Column(name = "description", length = 255)
    private String description;
}
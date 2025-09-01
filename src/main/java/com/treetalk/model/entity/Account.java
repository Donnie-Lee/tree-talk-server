package com.treetalk.model.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 类描述：
 *
 * @author lizheng
 * @created 2025/8/29 10:56
 */
@Entity
@Table(name = "account")
@Data
public class Account extends BaseEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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

    @Column(name = "status")
    private Integer status; // 0-正常 1-禁用

    @Column(name = "signature", length = 255)
    private String signature;

    @Column(name = "last_login_time")
    private LocalDateTime lastLoginTime;

    @Column(name = "password")
    private String password;

}
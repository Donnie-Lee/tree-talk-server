package com.treetalk.service;

import com.treetalk.model.dto.AuthDto;

/**
 * 验证码服务接口
 * 提供各类验证码生成和验证功能
 *
 * @author lizheng
 * @created 2025/8/29 15:13
 */
public interface CheckCodeService {

    /**
     * 生成并发送手机短信验证码
     * @param smsCheckCode 包含手机号的参数对象
     */
    void smsCheckCode(AuthDto.SmsCheckCode smsCheckCode);

    /**
     * 验证验证码
     * @param sceneCode 场景码
     * @param username 用户名
     * @param checkCode 验证码
     */
    void verifyCode(String sceneCode,String username, String checkCode);
}
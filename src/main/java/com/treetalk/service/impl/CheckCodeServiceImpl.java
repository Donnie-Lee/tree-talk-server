package com.treetalk.service.impl;

import com.treetalk.model.constants.CheckCodeConstant;
import com.treetalk.model.dto.AuthDto;
import com.treetalk.model.entity.CheckCodeConfig;
import com.treetalk.repository.CheckCodeConfigRepository;
import com.treetalk.service.CheckCodeService;
import com.treetalk.util.RandomUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * 验证码服务实现类
 * 提供各类验证码生成和验证功能
 *
 * @author lizheng
 * @created 2025/8/29 15:14
 */
@Slf4j
@Service
public class CheckCodeServiceImpl implements CheckCodeService {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;
    @Resource
    private CheckCodeConfigRepository checkCodeConfigRepository;

    // 手机验证码
    @Override
    public void smsCheckCode(AuthDto.SmsCheckCode smsCheckCode) {
        String sceneCode = smsCheckCode.sceneCode().toUpperCase();
        CheckCodeConfig checkCodeConfig = checkCodeConfigRepository.findBySceneCodeAndIsEnabledIsTrue(sceneCode)
                .orElseThrow(() -> new RuntimeException("验证码配置不存在"));

        if (!checkCodeConfig.getIsEnabled()) {
            throw new RuntimeException("验证码功能已禁用");
        }
        // 校验用户是否超过最大发送次数
        String dailyMaxNumberKey = CheckCodeConstant.SMS_DAILY_NUMBER_KEY + sceneCode + "_" + smsCheckCode.username();
        Object number = redisTemplate.opsForValue().get(dailyMaxNumberKey);
        if (Objects.nonNull(number) && Integer.parseInt(number.toString()) >= checkCodeConfig.getDailyLimit()) {
            throw new RuntimeException("您今天已经超过最大发送次数，请明天再试");
        }

        // 校验间隔时间
        String intervalKey = CheckCodeConstant.SMS_VERIFICATION_CODE_INTERVAL_KEY + sceneCode + "_" + smsCheckCode.username();
        if (redisTemplate.hasKey(intervalKey)) {
            Object time = redisTemplate.opsForValue().get(intervalKey);
            if (Objects.nonNull(time) && System.currentTimeMillis() - Long.parseLong(time.toString()) < checkCodeConfig.getSendInterval() * 1000) {
                throw new RuntimeException("获取验证码过于频繁，请稍后再试");
            }
        }

        // 生成验证码
        String verificationCode = RandomUtils.generateVerificationCode(checkCodeConfig.getCodeLength(), checkCodeConfig.getCodeType());

        // 在实际应用中，这里应该:
        // 1. 将验证码存储到Redis中，设置过期时间（如5分钟）
        redisTemplate.opsForValue()
                .set(CheckCodeConstant.SMS_VERIFICATION_CODE_KEY + sceneCode + "_" + smsCheckCode.username(),
                        verificationCode,
                        checkCodeConfig.getExpireMinutes(),
                        TimeUnit.MINUTES);
        redisTemplate.opsForValue()
                .set(intervalKey,
                        String.valueOf(System.currentTimeMillis() + checkCodeConfig.getSendInterval() * 1000),
                        checkCodeConfig.getSendInterval(),
                        TimeUnit.SECONDS);
        if (redisTemplate.hasKey(dailyMaxNumberKey)) {
            redisTemplate.opsForValue().increment(dailyMaxNumberKey);
        } else {
            // 当天结束
            redisTemplate.opsForValue().set(dailyMaxNumberKey, 1, getMinutesUntilEndOfDay(), TimeUnit.MINUTES);
        }

        // TODO 2. 调用短信服务API发送验证码到用户手机

        log.info("Generated verification code for {} : {}", smsCheckCode.username(), verificationCode);
    }

    @Override
    public void verifyCode(String sceneCode, String username, String checkCode) {
        sceneCode = sceneCode.toUpperCase();
        checkCodeConfigRepository.findBySceneCodeAndIsEnabledIsTrue(sceneCode)
                .orElseThrow(() -> new RuntimeException("验证码配置不存在"));

        String key = CheckCodeConstant.SMS_VERIFICATION_CODE_KEY + sceneCode + "_" + username;
        Object value = redisTemplate.opsForValue().get(key);
        if (Objects.isNull(value) || !value.toString().equals(checkCode)) {
            throw new RuntimeException("验证码错误");
        }
        redisTemplate.delete(key);
    }

    /**
     * 获取当天23:59:59的时间对象
     *
     * @return 当天23:59:59的时间对象
     */
    private LocalDateTime getEndOfDay() {
        return LocalDateTime.of(LocalDate.now(), LocalTime.MAX);
    }

    /**
     * 获取当前时间到当天结束(23:59:59)还有多少分钟
     *
     * @return 距离当天结束的分钟数
     */
    private long getMinutesUntilEndOfDay() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime endOfDay = getEndOfDay();
        return ChronoUnit.MINUTES.between(now, endOfDay);
    }

}
package com.treetalk.util;

import java.util.Random;

/**
 * 类描述：
 *
 * @author lizheng
 * @created 2025/8/29 17:13
 */
public class RandomUtils {

    /**
     * 根据长度和类型生成验证码
     *
     * @param length 验证码长度
     * @param type   验证码类型 1:数字 2:字母 3:数字+字母
     * @return 生成的验证码
     */
    public static String generateVerificationCode(int length, Integer type) {
        switch (type) {
            case 1: // 数字
                return generateNumericCode(length);
            case 2: // 字母
                return generateAlphaCode(length);
            case 3: // 数字+字母
                return generateAlphaNumericCode(length);
            default:
                return generateNumericCode(length);
        }
    }


    /**
     * 生成纯数字验证码
     *
     * @param length 验证码长度
     * @return 纯数字验证码
     */
    public static String generateNumericCode(int length) {
        Random random = new Random();
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < length; i++) {
            code.append(random.nextInt(10));
        }
        return code.toString();
    }

    /**
     * 生成纯字母验证码
     *
     * @param length 验证码长度
     * @return 纯字母验证码
     */
    public static String generateAlphaCode(int length) {
        Random random = new Random();
        StringBuilder code = new StringBuilder();
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        for (int i = 0; i < length; i++) {
            code.append(characters.charAt(random.nextInt(characters.length())));
        }
        return code.toString();
    }

    /**
     * 生成数字+字母验证码
     *
     * @param length 验证码长度
     * @return 数字+字母验证码
     */
    public static String generateAlphaNumericCode(int length) {
        Random random = new Random();
        StringBuilder code = new StringBuilder();
        String characters = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        for (int i = 0; i < length; i++) {
            code.append(characters.charAt(random.nextInt(characters.length())));
        }
        return code.toString();
    }

}

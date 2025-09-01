package com.treetalk.model.dto;

import com.treetalk.model.enums.LoginType;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 类描述：
 *
 * @author lizheng
 * @created 2025/8/29 14:44
 */
public class AuthDto {

    public record LoginDto(@Schema(description = "用户名")
                           String username,
                           @Schema(description = "密码/验证码")
                           String password,
                           @Schema(description = "登录类型")
                           LoginType loginType) {
    }

    public record SmsCheckCode(@Schema(description = "用户名")
                               String username,
                               @Schema(description = "场景编码")
                               String sceneCode){

    }

}

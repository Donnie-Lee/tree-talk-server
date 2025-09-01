package com.treetalk.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

/**
 * 类描述：
 *
 * @author lizheng
 * @created 2025/9/1 10:20
 */
public record RegisterInfo(@NotBlank(message = "用户名不能为空")
                           @Schema(description = "用户名")
                           String username,
                           @NotBlank(message = "密码不能为空")
                           @Schema(description = "密码")
                           String password,
                           @NotBlank(message = "重复密码不能为空")
                           @Schema(description = "重复密码")
                           String repeatPassword,
                           @NotBlank(message = "验证码不能为空")
                           @Schema(description = "验证码")
                           String checkCode) {
}

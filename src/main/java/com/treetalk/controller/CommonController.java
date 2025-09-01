package com.treetalk.controller;

import com.treetalk.model.dto.ApiResponse;
import com.treetalk.model.dto.AuthDto;
import com.treetalk.service.CheckCodeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 类描述：
 *
 * @author lizheng
 * @created 2025/9/1 10:42
 */
@RestController
@RequestMapping("/common")
@Tag(name = "通用接口", description = "通用接口")
public record CommonController(CheckCodeService checkCodeService) {

    @PostMapping("smsCheckCode")
    @Operation(summary = "短信验证码", description = "获取手机短信验证码")
    public ApiResponse<String> smsCheckCode(@RequestBody AuthDto.SmsCheckCode smsCheckCode) {
        checkCodeService.smsCheckCode(smsCheckCode);
        return ApiResponse.success();
    }

}

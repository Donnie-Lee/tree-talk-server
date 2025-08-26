package com.treetalk.dto;

import com.fasterxml.jackson.databind.JsonNode;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 类描述：
 *
 * @author lizheng
 * @created 2025/8/26 16:48
 */
@Data
public class UserProfileUpdateDto {

    @Schema(description = "隐私设置")
    private Integer privacySetting = 0; // 0-默认，1-日记加密

    @Schema(description = "日记密码")
    private String diaryPassword;

    @Schema(description = "通知设置")
    private JsonNode notificationSetting;

}

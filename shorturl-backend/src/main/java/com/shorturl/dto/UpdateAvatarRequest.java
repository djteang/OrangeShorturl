package com.shorturl.dto;

import lombok.Data;
import javax.validation.constraints.NotBlank;

/**
 * 更新头像请求DTO
 */
@Data
public class UpdateAvatarRequest {
    
    /**
     * 头像Base64数据
     */
    @NotBlank(message = "头像不能为空")
    private String avatarBase64;
}

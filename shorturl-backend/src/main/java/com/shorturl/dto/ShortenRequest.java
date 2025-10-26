package com.shorturl.dto;

import lombok.Data;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * 短链生成请求DTO
 */
@Data
public class ShortenRequest {
    
    /**
     * 原始URL
     */
    @NotBlank(message = "URL不能为空")
    @Pattern(regexp = "^(http|https)://.*", message = "URL格式不正确，必须以http://或https://开头")
    private String originalUrl;
    
    /**
     * 自定义短码（可选）
     */
    @Pattern(regexp = "^[a-zA-Z0-9]{4,20}$", message = "自定义短码只能包含字母和数字，长度为4-20位")
    private String customCode;
    
    /**
     * 过期时间（可选，格式：yyyy-MM-dd HH:mm:ss）
     */
    private String expireTime;
    
    /**
     * 分组ID（可选）
     */
    private Long groupId;
    
    /**
     * 二维码配置（可选）
     */
    private QRConfigDTO qrConfig;
}


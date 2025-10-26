package com.shorturl.dto;

import lombok.Data;
import javax.validation.constraints.NotBlank;

/**
 * UA黑名单请求DTO
 */
@Data
public class UaBlacklistRequest {
    
    /**
     * User-Agent匹配模式
     */
    @NotBlank(message = "UA匹配模式不能为空")
    private String uaPattern;
    
    /**
     * 描述
     */
    private String description;
    
    /**
     * 状态：0-禁用，1-启用
     */
    private Integer status;
}


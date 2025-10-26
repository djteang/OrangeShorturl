package com.shorturl.dto;

import lombok.Data;
import java.util.Date;

/**
 * UA黑名单响应DTO
 */
@Data
public class UaBlacklistResponse {
    
    /**
     * ID
     */
    private Long id;
    
    /**
     * User-Agent匹配模式
     */
    private String uaPattern;
    
    /**
     * 描述
     */
    private String description;
    
    /**
     * 创建时间
     */
    private Date createTime;
    
    /**
     * 状态：0-禁用，1-启用
     */
    private Integer status;
}


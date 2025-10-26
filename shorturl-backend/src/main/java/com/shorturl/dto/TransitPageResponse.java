package com.shorturl.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 中转页响应DTO
 */
@Data
public class TransitPageResponse {
    
    /**
     * ID
     */
    private Long id;
    
    /**
     * 用户ID
     */
    private Long userId;
    
    /**
     * 中转页标题
     */
    private String title;
    
    /**
     * 中转页提示内容
     */
    private String content;
    
    /**
     * 等待时间（秒）
     */
    private Integer waitTime;
    
    /**
     * 背景颜色
     */
    private String backgroundColor;
    
    /**
     * 文字颜色
     */
    private String textColor;
    
    /**
     * 按钮颜色
     */
    private String buttonColor;
    
    /**
     * Powered by 名称
     */
    private String poweredByName;
    
    /**
     * Powered by 图标（Base64）
     */
    private String poweredByIcon;
    
    /**
     * 状态：0-禁用，1-启用
     */
    private Integer status;
    
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}


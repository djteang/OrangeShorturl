package com.shorturl.dto;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 中转页请求DTO
 */
@Data
public class TransitPageRequest {
    
    /**
     * 中转页标题
     */
    @NotBlank(message = "标题不能为空")
    private String title;
    
    /**
     * 中转页提示内容
     */
    @NotBlank(message = "提示内容不能为空")
    private String content;
    
    /**
     * 等待时间（秒）
     */
    @NotNull(message = "等待时间不能为空")
    @Min(value = 0, message = "等待时间不能小于0秒")
    @Max(value = 60, message = "等待时间不能超过60秒")
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
    @NotNull(message = "状态不能为空")
    private Integer status;
}


package com.shorturl.dto;

import lombok.Data;

/**
 * 链接分组请求DTO
 */
@Data
public class UrlGroupRequest {
    
    /**
     * 分组名称
     */
    private String name;
    
    /**
     * 分组描述
     */
    private String description;
    
    /**
     * 分组图标
     */
    private String icon;
    
    /**
     * 分组颜色
     */
    private String color;
    
    /**
     * 排序顺序
     */
    private Integer sortOrder;
}


package com.shorturl.dto;

import lombok.Data;
import java.util.Date;

/**
 * 链接分组响应DTO
 */
@Data
public class UrlGroupResponse {
    
    /**
     * 主键ID
     */
    private Long id;
    
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
    
    /**
     * 链接数量
     */
    private Long urlCount;
    
    /**
     * 创建时间
     */
    private Date createTime;
    
    /**
     * 更新时间
     */
    private Date updateTime;
}


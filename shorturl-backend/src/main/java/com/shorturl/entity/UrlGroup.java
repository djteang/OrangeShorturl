package com.shorturl.entity;

import lombok.Data;
import java.util.Date;

/**
 * 链接分组实体类
 */
@Data
public class UrlGroup {
    
    /**
     * 主键ID
     */
    private Long id;
    
    /**
     * 用户ID
     */
    private Long userId;
    
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
     * 状态：0-禁用，1-启用
     */
    private Integer status;
    
    /**
     * 创建时间
     */
    private Date createTime;
    
    /**
     * 更新时间
     */
    private Date updateTime;
}


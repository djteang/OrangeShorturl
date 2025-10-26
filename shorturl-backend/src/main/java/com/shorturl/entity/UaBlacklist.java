package com.shorturl.entity;

import lombok.Data;
import java.util.Date;

/**
 * User-Agent黑名单实体类
 */
@Data
public class UaBlacklist {
    
    /**
     * 主键ID
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


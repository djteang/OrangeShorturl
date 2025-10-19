package com.shorturl.entity;

import lombok.Data;
import java.util.Date;

/**
 * URL映射实体类
 */
@Data
public class UrlMapping {
    
    /**
     * 主键ID
     */
    private Long id;
    
    /**
     * 创建用户ID
     */
    private Long userId;
    
    /**
     * 短码
     */
    private String shortCode;
    
    /**
     * 原始URL
     */
    private String originalUrl;
    
    /**
     * 创建时间
     */
    private Date createTime;
    
    /**
     * 过期时间
     */
    private Date expireTime;
    
    /**
     * 访问次数
     */
    private Integer visitCount;
    
    /**
     * 最后访问时间
     */
    private Date lastVisitTime;
    
    /**
     * 状态：0-无效，1-有效
     */
    private Integer status;
}


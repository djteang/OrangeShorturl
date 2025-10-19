package com.shorturl.entity;

import lombok.Data;
import java.util.Date;

/**
 * 访问记录实体类
 */
@Data
public class VisitLog {
    
    /**
     * 主键ID
     */
    private Long id;
    
    /**
     * 短码
     */
    private String shortCode;
    
    /**
     * 访问时间
     */
    private Date visitTime;
    
    /**
     * 访问IP
     */
    private String ipAddress;
    
    /**
     * 浏览器UA
     */
    private String userAgent;
    
    /**
     * 来源
     */
    private String referer;
}


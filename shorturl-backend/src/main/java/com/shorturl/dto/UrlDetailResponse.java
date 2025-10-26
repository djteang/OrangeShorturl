package com.shorturl.dto;

import lombok.Data;
import java.util.Date;

/**
 * URL详情响应DTO
 */
@Data
public class UrlDetailResponse {
    
    /**
     * 短码
     */
    private String shortCode;
    
    /**
     * 原始URL
     */
    private String originalUrl;
    
    /**
     * 链接标题
     */
    private String title;
    
    /**
     * 完整短链接URL
     */
    private String shortUrl;
    
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
     * 分组ID
     */
    private Long groupId;
    
    /**
     * 二维码配置
     */
    private QRConfigDTO qrConfig;
}


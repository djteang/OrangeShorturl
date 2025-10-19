package com.shorturl.dto;

import lombok.Data;
import java.util.Date;

/**
 * 评价响应DTO
 */
@Data
public class ReviewResponse {
    
    /**
     * 评价ID
     */
    private Long id;
    
    /**
     * 用户名
     */
    private String username;
    
    /**
     * 用户头像
     */
    private String avatarBase64;
    
    /**
     * 评价内容
     */
    private String content;
    
    /**
     * 评分
     */
    private Integer rating;
    
    /**
     * 创建时间
     */
    private Date createTime;
}


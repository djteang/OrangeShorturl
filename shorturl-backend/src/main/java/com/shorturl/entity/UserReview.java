package com.shorturl.entity;

import lombok.Data;
import java.util.Date;

/**
 * 用户评价实体类
 */
@Data
public class UserReview {
    
    /**
     * 主键ID
     */
    private Long id;
    
    /**
     * 用户ID
     */
    private Long userId;
    
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
    
    /**
     * 状态
     */
    private Integer status;
}


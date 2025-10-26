package com.shorturl.entity;

import lombok.Data;
import java.util.Date;

/**
 * 用户实体类
 */
@Data
public class User {
    
    /**
     * 主键ID
     */
    private Long id;
    
    /**
     * 用户名
     */
    private String username;
    
    /**
     * 密码
     */
    private String password;
    
    /**
     * 邮箱
     */
    private String email;
    
    /**
     * 头像Base64
     */
    private String avatarBase64;
    
    /**
     * OAuth提供商
     */
    private String oauthProvider;
    
    /**
     * OAuth用户ID
     */
    private String oauthId;
    
    /**
     * 创建时间
     */
    private Date createTime;
    
    /**
     * 最后登录时间
     */
    private Date lastLoginTime;
    
    /**
     * 状态
     */
    private Integer status;
    
    /**
     * 用户角色：admin-管理员，user-普通用户
     */
    private String role;
}


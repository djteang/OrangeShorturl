package com.shorturl.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * OAuth 配置
 */
@Data
@Component
@ConfigurationProperties(prefix = "oauth.linux-do")
public class OAuthConfig {
    
    /**
     * 客户端ID
     */
    private String clientId;
    
    /**
     * 客户端密钥
     */
    private String clientSecret;
    
    /**
     * 回调地址
     */
    private String redirectUri;
    
    /**
     * 授权地址
     */
    private String authorizeUrl;
    
    /**
     * 获取Token地址
     */
    private String tokenUrl;
    
    /**
     * 获取用户信息地址
     */
    private String userInfoUrl;
}





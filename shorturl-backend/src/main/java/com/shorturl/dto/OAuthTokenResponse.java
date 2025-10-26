package com.shorturl.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * OAuth Token 响应
 */
@Data
public class OAuthTokenResponse {
    
    /**
     * 访问令牌
     */
    @JsonProperty("access_token")
    private String accessToken;
    
    /**
     * 令牌类型
     */
    @JsonProperty("token_type")
    private String tokenType;
    
    /**
     * 过期时间（秒）
     */
    @JsonProperty("expires_in")
    private Integer expiresIn;
    
    /**
     * 刷新令牌
     */
    @JsonProperty("refresh_token")
    private String refreshToken;
    
    /**
     * 授权范围
     */
    private String scope;
}






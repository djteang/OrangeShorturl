package com.shorturl.service;

import com.shorturl.dto.LinuxDoUserInfo;
import com.shorturl.dto.UserResponse;

/**
 * OAuth 服务接口
 */
public interface OAuthService {
    
    /**
     * 获取授权URL
     * 
     * @return 授权URL
     */
    String getAuthorizationUrl();
    
    /**
     * 通过授权码获取用户信息并登录
     * 
     * @param code 授权码
     * @return 用户响应
     */
    UserResponse loginWithLinuxDo(String code);
    
    /**
     * 通过访问令牌获取Linux.do用户信息
     * 
     * @param accessToken 访问令牌
     * @return Linux.do用户信息
     */
    LinuxDoUserInfo getLinuxDoUserInfo(String accessToken);
}






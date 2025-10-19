package com.shorturl.service;

import com.shorturl.dto.LoginRequest;
import com.shorturl.dto.RegisterRequest;
import com.shorturl.dto.SendVerifyCodeRequest;
import com.shorturl.dto.UpdateAvatarRequest;
import com.shorturl.dto.UserResponse;

/**
 * 用户服务接口
 */
public interface UserService {
    
    /**
     * 注册
     */
    UserResponse register(RegisterRequest request);
    
    /**
     * 登录
     */
    UserResponse login(LoginRequest request);
    
    /**
     * 根据Token获取用户信息
     */
    UserResponse getUserByToken(String token);
    
    /**
     * OAuth登录（linux.do）
     */
    UserResponse oauthLogin(String provider, String oauthId, String username, String avatarUrl);
    
    /**
     * 发送邮箱验证码
     */
    void sendVerifyCode(SendVerifyCodeRequest request);
    
    /**
     * 更新用户头像
     */
    UserResponse updateAvatar(Long userId, UpdateAvatarRequest request);
    
    /**
     * 登出
     */
    void logout(Long userId);
}


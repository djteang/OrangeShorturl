package com.shorturl.service;

/**
 * Token服务接口
 */
public interface TokenService {
    
    /**
     * 保存Token到Redis
     * @param userId 用户ID
     * @param token Token
     */
    void saveToken(Long userId, String token);
    
    /**
     * 验证Token是否有效（检查Redis中是否存在）
     * @param userId 用户ID
     * @param token Token
     * @return 是否有效
     */
    boolean validateToken(Long userId, String token);
    
    /**
     * 删除Token（退出登录）
     * @param userId 用户ID
     */
    void deleteToken(Long userId);
    
    /**
     * 刷新Token过期时间
     * @param userId 用户ID
     * @param token Token
     */
    void refreshToken(Long userId, String token);
}


package com.shorturl.service.impl;

import com.shorturl.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * Token服务实现类
 */
@Service
public class TokenServiceImpl implements TokenService {
    
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    
    // Redis key前缀
    private static final String TOKEN_PREFIX = "token:";
    
    // Token过期时间（7天）
    private static final long TOKEN_EXPIRATION = 7;
    
    @Override
    public void saveToken(Long userId, String token) {
        String key = TOKEN_PREFIX + userId;
        redisTemplate.opsForValue().set(key, token, TOKEN_EXPIRATION, TimeUnit.DAYS);
    }
    
    @Override
    public boolean validateToken(Long userId, String token) {
        String key = TOKEN_PREFIX + userId;
        String cachedToken = (String) redisTemplate.opsForValue().get(key);
        return token.equals(cachedToken);
    }
    
    @Override
    public void deleteToken(Long userId) {
        String key = TOKEN_PREFIX + userId;
        redisTemplate.delete(key);
    }
    
    @Override
    public void refreshToken(Long userId, String token) {
        String key = TOKEN_PREFIX + userId;
        // 刷新过期时间
        redisTemplate.expire(key, TOKEN_EXPIRATION, TimeUnit.DAYS);
    }
}


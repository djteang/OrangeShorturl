package com.shorturl.service.impl;

import com.shorturl.config.OAuthConfig;
import com.shorturl.dto.LinuxDoUserInfo;
import com.shorturl.dto.OAuthTokenResponse;
import com.shorturl.dto.UserResponse;
import com.shorturl.entity.User;
import com.shorturl.mapper.UserMapper;
import com.shorturl.service.OAuthService;
import com.shorturl.service.TokenService;
import com.shorturl.utils.JwtUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.UUID;

/**
 * OAuth 服务实现类
 */
@Service
public class OAuthServiceImpl implements OAuthService {
    
    private static final Logger logger = LoggerFactory.getLogger(OAuthServiceImpl.class);
    
    @Autowired
    private OAuthConfig oauthConfig;
    
    @Autowired
    private UserMapper userMapper;
    
    @Autowired
    private TokenService tokenService;
    
    private final RestTemplate restTemplate = new RestTemplate();
    
    @Override
    public String getAuthorizationUrl() {
        return oauthConfig.getAuthorizeUrl() +
                "?client_id=" + oauthConfig.getClientId() +
                "&redirect_uri=" + oauthConfig.getRedirectUri() +
                "&response_type=code" +
                "&scope=read";
    }
    
    @Override
    public UserResponse loginWithLinuxDo(String code) {
        try {
            // 1. 通过授权码获取访问令牌
            OAuthTokenResponse tokenResponse = getAccessToken(code);
            
            // 2. 通过访问令牌获取用户信息
            LinuxDoUserInfo linuxDoUser = getLinuxDoUserInfo(tokenResponse.getAccessToken());
            
            // 3. 查找或创建用户
            User user = findOrCreateUser(linuxDoUser);
            
            // 4. 更新最后登录时间
            userMapper.updateLastLoginTime(user.getId());
            
            // 5. 生成JWT Token
            String token = JwtUtils.generateToken(user.getId(), user.getUsername());
            
            // 6. 保存Token到Redis
            tokenService.saveToken(user.getId(), token);
            logger.info("保存Token到Redis成功: userId={}", user.getId());
            
            // 7. 返回用户信息
            return UserResponse.builder()
                    .id(user.getId())
                    .username(user.getUsername())
                    .email(user.getEmail())
                    .avatarBase64(user.getAvatarBase64())
                    .token(token)
                    .build();
                    
        } catch (Exception e) {
            logger.error("Linux.do OAuth登录失败", e);
            throw new RuntimeException("OAuth登录失败: " + e.getMessage());
        }
    }
    
    @Override
    public LinuxDoUserInfo getLinuxDoUserInfo(String accessToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(accessToken);
            headers.setContentType(MediaType.APPLICATION_JSON);
            
            HttpEntity<String> entity = new HttpEntity<>(headers);
            
            ResponseEntity<LinuxDoUserInfo> response = restTemplate.exchange(
                    oauthConfig.getUserInfoUrl(),
                    HttpMethod.GET,
                    entity,
                    LinuxDoUserInfo.class
            );
            
            logger.info("获取Linux.do用户信息成功: {}", response.getBody());
            return response.getBody();
            
        } catch (Exception e) {
            logger.error("获取Linux.do用户信息失败", e);
            throw new RuntimeException("获取用户信息失败: " + e.getMessage());
        }
    }
    
    /**
     * 通过授权码获取访问令牌
     */
    private OAuthTokenResponse getAccessToken(String code) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            
            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("grant_type", "authorization_code");
            params.add("client_id", oauthConfig.getClientId());
            params.add("client_secret", oauthConfig.getClientSecret());
            params.add("code", code);
            params.add("redirect_uri", oauthConfig.getRedirectUri());
            
            HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(params, headers);
            
            ResponseEntity<OAuthTokenResponse> response = restTemplate.exchange(
                    oauthConfig.getTokenUrl(),
                    HttpMethod.POST,
                    entity,
                    OAuthTokenResponse.class
            );
            
            logger.info("获取访问令牌成功");
            return response.getBody();
            
        } catch (Exception e) {
            logger.error("获取访问令牌失败", e);
            throw new RuntimeException("获取访问令牌失败: " + e.getMessage());
        }
    }
    
    /**
     * 查找或创建用户
     */
    private User findOrCreateUser(LinuxDoUserInfo linuxDoUser) {
        // 优先通过邮箱查找
        User user = null;
        if (linuxDoUser.getEmail() != null && !linuxDoUser.getEmail().isEmpty()) {
            user = userMapper.selectByEmail(linuxDoUser.getEmail());
        }
        
        // 如果邮箱找不到，尝试通过用户名查找
        if (user == null) {
            user = userMapper.selectByUsername(linuxDoUser.getUsername());
        }
        
        // 如果用户不存在，创建新用户
        if (user == null) {
            user = new User();
            user.setUsername(linuxDoUser.getUsername());
            user.setEmail(linuxDoUser.getEmail());
            // OAuth登录不需要密码，设置一个随机密码
            user.setPassword(UUID.randomUUID().toString());
            user.setCreateTime(new Date());
            user.setStatus(1);
            
            if (linuxDoUser.getAvatarUrl() != null && !linuxDoUser.getAvatarUrl().isEmpty()) {
                user.setAvatarBase64(linuxDoUser.getAvatarUrl());
            }
            
            userMapper.insert(user);
            logger.info("创建新用户: username={}, email={}", user.getUsername(), user.getEmail());
        } else {
            // 用户已存在，更新头像URL（如果有变化）
            if (linuxDoUser.getAvatarUrl() != null && !linuxDoUser.getAvatarUrl().isEmpty()) {
                if (!linuxDoUser.getAvatarUrl().equals(user.getAvatarBase64())) {
                    user.setAvatarBase64(linuxDoUser.getAvatarUrl());
                    userMapper.update(user);
                    logger.info("更新用户头像URL: username={}, url={}", user.getUsername(), linuxDoUser.getAvatarUrl());
                }
            }
        }
        
        return user;
    }
    
}


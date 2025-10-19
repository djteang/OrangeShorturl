package com.shorturl.service.impl;

import com.shorturl.dto.LoginRequest;
import com.shorturl.dto.RegisterRequest;
import com.shorturl.dto.SendVerifyCodeRequest;
import com.shorturl.dto.UpdateAvatarRequest;
import com.shorturl.dto.UserResponse;
import com.shorturl.entity.User;
import com.shorturl.mapper.UserMapper;
import com.shorturl.service.MailService;
import com.shorturl.service.TokenService;
import com.shorturl.service.UserService;
import com.shorturl.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.Date;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * 用户服务实现类
 */
@Service
public class UserServiceImpl implements UserService {
    
    @Autowired
    private UserMapper userMapper;
    
    @Autowired
    private MailService mailService;
    
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    
    @Autowired
    private TokenService tokenService;
    
    // Redis key前缀
    private static final String VERIFY_CODE_PREFIX = "verify_code:";
    
    @Override
    public UserResponse register(RegisterRequest request) {
        // 验证邮箱验证码
        String key = VERIFY_CODE_PREFIX + request.getEmail();
        String cachedCode = (String) redisTemplate.opsForValue().get(key);
        
        if (cachedCode == null) {
            throw new RuntimeException("请先获取验证码");
        }
        if (!cachedCode.equals(request.getVerifyCode())) {
            throw new RuntimeException("验证码错误");
        }
        
        // 检查用户名是否已存在
        User existUser = userMapper.selectByUsername(request.getUsername());
        if (existUser != null) {
            throw new RuntimeException("用户名已存在");
        }
        
        // 检查邮箱是否已存在
        User existEmail = userMapper.selectByEmail(request.getEmail());
        if (existEmail != null) {
            throw new RuntimeException("邮箱已被注册");
        }
        
        // 验证成功后移除验证码
        redisTemplate.delete(key);
        
        // 创建用户
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(encryptPassword(request.getPassword()));
        user.setEmail(request.getEmail());
        user.setCreateTime(new Date());
        user.setStatus(1);
        
        userMapper.insert(user);
        
        // 生成Token
        String token = JwtUtils.generateToken(user.getId(), user.getUsername());
        
        // 保存Token到Redis
        tokenService.saveToken(user.getId(), token);
        
        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .avatarBase64(user.getAvatarBase64())
                .token(token)
                .build();
    }
    
    @Override
    public UserResponse login(LoginRequest request) {
        // 查询用户（支持用户名或邮箱登录）
        User user = null;
        String usernameOrEmail = request.getUsername();
        
        // 判断是邮箱还是用户名
        if (usernameOrEmail.contains("@")) {
            user = userMapper.selectByEmail(usernameOrEmail);
        } else {
            user = userMapper.selectByUsername(usernameOrEmail);
        }
        
        if (user == null) {
            throw new RuntimeException("用户名/邮箱或密码错误");
        }
        
        // 验证密码
        String encryptedPassword = encryptPassword(request.getPassword());
        if (!encryptedPassword.equals(user.getPassword())) {
            throw new RuntimeException("用户名/邮箱或密码错误");
        }
        
        // 更新最后登录时间
        userMapper.updateLastLoginTime(user.getId());
        
        // 生成Token
        String token = JwtUtils.generateToken(user.getId(), user.getUsername());
        
        // 保存Token到Redis
        tokenService.saveToken(user.getId(), token);
        
        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .avatarBase64(user.getAvatarBase64())
                .token(token)
                .build();
    }
    
    @Override
    public UserResponse getUserByToken(String token) {
        if (!JwtUtils.validateToken(token)) {
            throw new RuntimeException("Token无效");
        }
        
        Long userId = JwtUtils.getUserIdFromToken(token);
        User user = userMapper.selectById(userId);
        
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        
        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .avatarBase64(user.getAvatarBase64())
                .build();
    }
    
    @Override
    public UserResponse oauthLogin(String provider, String oauthId, String username, String avatarUrl) {
        // 查询是否已有OAuth账号
        User user = userMapper.selectByOAuth(provider, oauthId);
        
        if (user == null) {
            // 创建新用户
            user = new User();
            user.setUsername(username);
            user.setOauthProvider(provider);
            user.setOauthId(oauthId);
            
            // 下载头像并转为Base64（简化处理，实际应该异步下载）
            if (avatarUrl != null && !avatarUrl.isEmpty()) {
                // 这里简化处理，实际应该下载图片并转Base64
                user.setAvatarBase64(avatarUrl);
            }
            
            user.setCreateTime(new Date());
            user.setStatus(1);
            
            userMapper.insert(user);
        } else {
            // 更新最后登录时间
            userMapper.updateLastLoginTime(user.getId());
        }
        
        // 生成Token
        String token = JwtUtils.generateToken(user.getId(), user.getUsername());
        
        // 保存Token到Redis
        tokenService.saveToken(user.getId(), token);
        
        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .avatarBase64(user.getAvatarBase64())
                .token(token)
                .build();
    }
    
    @Override
    public void sendVerifyCode(SendVerifyCodeRequest request) {
        String email = request.getEmail();
        
        // 检查邮箱是否已注册
        User existUser = userMapper.selectByEmail(email);
        if (existUser != null) {
            throw new RuntimeException("该邮箱已被注册");
        }
        
        // 生成6位数字验证码
        String code = String.format("%06d", new Random().nextInt(1000000));
        
        // 存储验证码到Redis（5分钟有效）
        String key = VERIFY_CODE_PREFIX + email;
        redisTemplate.opsForValue().set(key, code, 5, TimeUnit.MINUTES);
        
        // 发送验证码邮件
        try {
            mailService.sendVerifyCodeMail(email, code);
            System.out.println("验证码邮件已发送: email=" + email + ", code=" + code);
        } catch (Exception e) {
            // 发送失败时，打印到控制台作为备用
            System.out.println("=================================");
            System.out.println("邮件发送失败，备用信息：");
            System.out.println("邮箱: " + email);
            System.out.println("验证码: " + code);
            System.out.println("有效期: 5分钟");
            System.out.println("=================================");
            throw new RuntimeException("邮件发送失败，请检查邮箱配置: " + e.getMessage());
        }
    }
    
    @Override
    public UserResponse updateAvatar(Long userId, UpdateAvatarRequest request) {
        // 查询用户
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        
        // 更新头像
        user.setAvatarBase64(request.getAvatarBase64());
        userMapper.update(user);
        
        // 返回更新后的用户信息
        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .avatarBase64(user.getAvatarBase64())
                .token(null)  // 不返回token
                .build();
    }
    
    @Override
    public void logout(Long userId) {
        // 从Redis中删除Token
        tokenService.deleteToken(userId);
    }
    
    /**
     * 加密密码
     */
    private String encryptPassword(String password) {
        return DigestUtils.md5DigestAsHex(password.getBytes());
    }
}


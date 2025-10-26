package com.shorturl.controller;

import com.shorturl.dto.*;
import com.shorturl.service.UserService;
import com.shorturl.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 用户控制器
 */
@RestController
@RequestMapping("/user")
public class UserController {
    
    @Autowired
    private UserService userService;
    
    /**
     * 注册
     */
    @PostMapping("/register")
    public Result<UserResponse> register(@Validated @RequestBody RegisterRequest request) {
        try {
            UserResponse response = userService.register(request);
            return Result.success(response);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    /**
     * 登录
     */
    @PostMapping("/login")
    public Result<UserResponse> login(@Validated @RequestBody LoginRequest request) {
        try {
            UserResponse response = userService.login(request);
            return Result.success(response);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    /**
     * 获取当前用户信息
     */
    @GetMapping("/info")
    public Result<UserResponse> getUserInfo(@RequestHeader("Authorization") String authorization) {
        try {
            String token = authorization.replace("Bearer ", "");
            UserResponse response = userService.getUserByToken(token);
            return Result.success(response);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    /**
     * 发送邮箱验证码
     */
    @PostMapping("/send-verify-code")
    public Result<String> sendVerifyCode(@Validated @RequestBody SendVerifyCodeRequest request) {
        try {
            userService.sendVerifyCode(request);
            return Result.success("验证码已发送");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    /**
     * 更新用户头像
     */
    @PostMapping("/update-avatar")
    public Result<UserResponse> updateAvatar(@RequestHeader("Authorization") String authorization,
                                            @Validated @RequestBody UpdateAvatarRequest request) {
        try {
            // 从token中获取用户ID
            String token = authorization.replace("Bearer ", "");
            Long userId = JwtUtils.getUserIdFromToken(token);
            
            UserResponse response = userService.updateAvatar(userId, request);
            return Result.success(response);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    /**
     * OAuth登录（linux.do）
     */
    @PostMapping("/oauth/login")
    public Result<UserResponse> oauthLogin(@RequestParam String provider,
                                           @RequestParam String oauthId,
                                           @RequestParam String username,
                                           @RequestParam(required = false) String avatarUrl) {
        try {
            UserResponse response = userService.oauthLogin(provider, oauthId, username, avatarUrl);
            return Result.success(response);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    /**
     * 登出
     */
    @PostMapping("/logout")
    public Result<String> logout(@RequestHeader("Authorization") String authorization) {
        try {
            // 从token中获取用户ID
            String token = authorization.replace("Bearer ", "");
            Long userId = JwtUtils.getUserIdFromToken(token);
            
            userService.logout(userId);
            return Result.success("登出成功");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    /**
     * 修改用户名
     */
    @PutMapping("/update-username")
    public Result<String> updateUsername(@RequestHeader("Authorization") String authorization,
                                         @RequestBody java.util.Map<String, String> request) {
        try {
            String token = authorization.replace("Bearer ", "");
            Long userId = JwtUtils.getUserIdFromToken(token);
            
            String username = request.get("username");
            if (username == null || username.trim().isEmpty()) {
                return Result.error("用户名不能为空");
            }
            
            userService.updateUsername(userId, username);
            return Result.success("用户名修改成功");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    /**
     * 修改密码
     */
    @PutMapping("/change-password")
    public Result<String> changePassword(@RequestHeader("Authorization") String authorization,
                                         @RequestBody java.util.Map<String, String> request) {
        try {
            String token = authorization.replace("Bearer ", "");
            Long userId = JwtUtils.getUserIdFromToken(token);
            
            String oldPassword = request.get("oldPassword");
            String newPassword = request.get("newPassword");
            
            if (oldPassword == null || oldPassword.trim().isEmpty()) {
                return Result.error("当前密码不能为空");
            }
            if (newPassword == null || newPassword.trim().isEmpty()) {
                return Result.error("新密码不能为空");
            }
            if (newPassword.length() < 6) {
                return Result.error("新密码至少6位");
            }
            
            userService.changePassword(userId, oldPassword, newPassword);
            return Result.success("密码修改成功");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}


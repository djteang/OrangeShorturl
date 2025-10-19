package com.shorturl.controller;

import com.shorturl.dto.Result;
import com.shorturl.dto.UserResponse;
import com.shorturl.service.OAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

/**
 * OAuth 控制器
 */
@RestController
@RequestMapping("/auth/oauth2")
public class OAuthController {
    
    @Autowired
    private OAuthService oauthService;
    
    @Value("${oauth.frontend.callback-url}")
    private String frontendCallbackUrl;
    
    @Value("${oauth.frontend.login-url}")
    private String frontendLoginUrl;
    
    /**
     * 获取Linux.do授权URL
     */
    @GetMapping("/linux-do/authorize")
    public Result<String> getLinuxDoAuthUrl() {
        try {
            String authUrl = oauthService.getAuthorizationUrl();
            return Result.success(authUrl);
        } catch (Exception e) {
            return Result.error("获取授权URL失败: " + e.getMessage());
        }
    }
    
    /**
     * Linux.do OAuth回调处理
     */
    @GetMapping("/linux-do/callback")
    public RedirectView linuxDoCallback(@RequestParam("code") String code) {
        try {
            UserResponse userResponse = oauthService.loginWithLinuxDo(code);
            
            // 重定向到前端回调页面，携带token和用户信息
            String redirectUrl = String.format(
                "%s?token=%s&username=%s&userId=%d&email=%s&avatar=%s",
                frontendCallbackUrl,
                userResponse.getToken(),
                userResponse.getUsername(),
                userResponse.getId(),
                userResponse.getEmail() != null ? userResponse.getEmail() : "",
                userResponse.getAvatarBase64() != null ? userResponse.getAvatarBase64() : ""
            );
            
            return new RedirectView(redirectUrl);
        } catch (Exception e) {
            // 登录失败，重定向到前端登录页面，携带错误信息
            String redirectUrl = frontendLoginUrl + "?error=" + e.getMessage();
            return new RedirectView(redirectUrl);
        }
    }
    
    /**
     * 手动处理回调（用于测试）
     */
    @PostMapping("/linux-do/login")
    public Result<UserResponse> linuxDoLogin(@RequestParam("code") String code) {
        try {
            UserResponse userResponse = oauthService.loginWithLinuxDo(code);
            return Result.success(userResponse);
        } catch (Exception e) {
            return Result.error("OAuth登录失败: " + e.getMessage());
        }
    }
}


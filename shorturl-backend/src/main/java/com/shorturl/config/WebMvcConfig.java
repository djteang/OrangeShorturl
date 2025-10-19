package com.shorturl.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web MVC配置
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    
    @Autowired
    private JwtInterceptor jwtInterceptor;
    
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtInterceptor)
                .addPathPatterns("/api/url/list", "/api/url/detail/**", "/api/url/stats/**", "/api/review/submit", "/api/user/logout", "/api/user/update-avatar")
                .excludePathPatterns(
                        "/api/user/register",
                        "/api/user/login",
                        "/api/user/oauth/login",
                        "/api/user/send-verify-code",
                        "/api/url/shorten",    // 创建短链接不需要认证
                        "/api/url/resolve/**", // 解析短链接不需要认证
                        "/api/review/list",
                        "/{shortCode}"         // 短链接重定向不需要认证
                );
    }
}


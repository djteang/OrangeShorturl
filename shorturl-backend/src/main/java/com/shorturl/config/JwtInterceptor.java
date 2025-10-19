package com.shorturl.config;

import com.shorturl.service.TokenService;
import com.shorturl.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * JWT拦截器
 */
@Component
public class JwtInterceptor implements HandlerInterceptor {
    
    @Autowired
    private TokenService tokenService;
    
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // OPTIONS请求直接放行
        if ("OPTIONS".equals(request.getMethod())) {
            return true;
        }
        
        // 设置响应编码
        response.setContentType("application/json;charset=UTF-8");
        
        // 获取Token
        String authorization = request.getHeader("Authorization");
        
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"code\":401,\"message\":\"未登录，请先登录\"}");
            return false;
        }
        
        String token = authorization.replace("Bearer ", "");
        
        // 验证Token格式
        if (!JwtUtils.validateToken(token)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"code\":401,\"message\":\"Token无效或已过期\"}");
            return false;
        }
        
        // 获取用户ID
        Long userId = JwtUtils.getUserIdFromToken(token);
        
        // 验证Token是否在Redis中存在（检查是否已退出登录或过期）
        if (!tokenService.validateToken(userId, token)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"code\":401,\"message\":\"登录已过期，请重新登录\"}");
            return false;
        }
        
        // 刷新Token过期时间（活跃用户自动续期）
        tokenService.refreshToken(userId, token);
        
        // 将用户ID存入请求属性
        request.setAttribute("userId", userId);
        
        return true;
    }
}


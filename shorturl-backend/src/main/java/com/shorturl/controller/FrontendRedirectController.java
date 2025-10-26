package com.shorturl.controller;

import com.shorturl.annotation.RateLimit;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 前端路由重定向控制器
 * 将后端的/r/{shortCode}请求重定向到前端应用
 */
@Controller
public class FrontendRedirectController {
    
    @Value("${shorturl.frontend-url:http://localhost:5173}")
    private String frontendUrl;
    
    /**
     * 将 /r/{shortCode} 重定向到前端应用
     * 前端会显示Redirect.vue中转页面
     * 限流：每个IP每分钟最多50次请求
     */
    @RateLimit(key = "frontend_redirect", time = 60, count = 50, limitType = RateLimit.LimitType.IP)
    @GetMapping("/r/{shortCode}")
    public void redirectToFrontend(@PathVariable String shortCode, HttpServletResponse response) throws IOException {
        // 重定向到前端的对应路由
        response.sendRedirect(frontendUrl + "/r/" + shortCode);
    }
}


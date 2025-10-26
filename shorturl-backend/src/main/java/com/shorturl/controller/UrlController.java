package com.shorturl.controller;

import com.shorturl.annotation.RateLimit;
import com.shorturl.dto.*;
import com.shorturl.service.UrlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * URL控制器
 */
@RestController
public class UrlController {
    
    @Autowired
    private UrlService urlService;
    
    /**
     * 生成短链接
     * 限流：每个IP每分钟最多10次请求
     */
    @RateLimit(key = "shorten", time = 60, count = 10, limitType = RateLimit.LimitType.IP)
    @PostMapping("/url/shorten")
    public Result<ShortenResponse> shorten(@Validated @RequestBody ShortenRequest request) {
        try {
            ShortenResponse response = urlService.shorten(request);
            return Result.success(response);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    /**
     * 短链接重定向（直接重定向）
     * 限流：每个IP每分钟最多30次请求
     */
    @RateLimit(key = "redirect", time = 60, count = 10, limitType = RateLimit.LimitType.IP)
    @GetMapping("/{shortCode}")
    public void redirect(@PathVariable String shortCode, 
                        HttpServletRequest request,
                        HttpServletResponse response) {
        try {
            String originalUrl = urlService.getOriginalUrl(shortCode, request);
            response.sendRedirect(originalUrl);
        } catch (Exception e) {
            try {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, e.getMessage());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
    
    /**
     * 解析短链接（返回原始URL，不重定向）
     */
    @GetMapping("/url/resolve/{shortCode}")
    public Result<String> resolve(@PathVariable String shortCode, HttpServletRequest request) {
        try {
            String originalUrl = urlService.getOriginalUrl(shortCode, request);
            return Result.success(originalUrl);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    /**
     * 获取URL列表
     */
    @RateLimit(key = "list", time = 60, count = 30, limitType = RateLimit.LimitType.IP)
    @GetMapping("/url/list")
    public Result<PageResult<UrlDetailResponse>> getList(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String keyword) {
        try {
            PageResult<UrlDetailResponse> result = urlService.getUrlList(page, size, keyword);
            return Result.success(result);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    /**
     * 获取URL详情
     */
    @RateLimit(key = "detail", time = 60, count = 30, limitType = RateLimit.LimitType.IP)
    @GetMapping("/url/detail/{shortCode}")
    public Result<UrlDetailResponse> getDetail(@PathVariable String shortCode) {
        try {
            UrlDetailResponse response = urlService.getUrlDetail(shortCode);
            return Result.success(response);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    /**
     * 删除URL
     */
    @RateLimit(key = "delete", time = 60, count = 5, limitType = RateLimit.LimitType.IP)
    @DeleteMapping("/url/{shortCode}")
    public Result<Void> delete(@PathVariable String shortCode) {
        try {
            urlService.deleteUrl(shortCode);
            return Result.success();
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    /**
     * 获取访问统计
     */
    @RateLimit(key = "stats", time = 60, count = 30, limitType = RateLimit.LimitType.IP)
    @GetMapping("/url/stats/{shortCode}")
    public Result<VisitStatsResponse> getStats(@PathVariable String shortCode) {
        try {
            VisitStatsResponse response = urlService.getVisitStats(shortCode);
            return Result.success(response);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}


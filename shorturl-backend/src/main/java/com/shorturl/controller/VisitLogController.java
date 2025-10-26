package com.shorturl.controller;

import com.shorturl.dto.GlobalStatsResponse;
import com.shorturl.dto.PageResult;
import com.shorturl.dto.Result;
import com.shorturl.entity.VisitLog;
import com.shorturl.service.VisitLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 访问日志控制器
 */
@RestController
@RequestMapping("/visit-log")
public class VisitLogController {
    
    @Autowired
    private VisitLogService visitLogService;
    
    /**
     * 获取访问日志列表（需要登录）
     */
    @GetMapping("/list")
    public Result<PageResult<VisitLog>> getList(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String shortCode) {
        try {
            PageResult<VisitLog> result = visitLogService.getVisitLogList(page, size, shortCode);
            return Result.success(result);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    /**
     * 获取全局统计数据（需要登录）
     */
    @GetMapping("/stats")
    public Result<GlobalStatsResponse> getStats() {
        try {
            GlobalStatsResponse stats = visitLogService.getGlobalStats();
            return Result.success(stats);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}


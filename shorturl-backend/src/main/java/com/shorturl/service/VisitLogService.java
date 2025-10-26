package com.shorturl.service;

import com.shorturl.dto.GlobalStatsResponse;
import com.shorturl.dto.PageResult;
import com.shorturl.entity.VisitLog;

/**
 * 访问日志服务接口
 */
public interface VisitLogService {
    
    /**
     * 获取访问日志列表
     */
    PageResult<VisitLog> getVisitLogList(Integer page, Integer size, String shortCode);
    
    /**
     * 获取全局统计数据
     */
    GlobalStatsResponse getGlobalStats();
}


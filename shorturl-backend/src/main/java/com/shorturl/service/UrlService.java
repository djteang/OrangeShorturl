package com.shorturl.service;

import com.shorturl.dto.*;
import javax.servlet.http.HttpServletRequest;

/**
 * URL服务接口
 */
public interface UrlService {
    
    /**
     * 生成短链接
     */
    ShortenResponse shorten(ShortenRequest request);
    
    /**
     * 根据短码获取原始URL
     */
    String getOriginalUrl(String shortCode, HttpServletRequest request);
    
    /**
     * 获取URL列表
     */
    PageResult<UrlDetailResponse> getUrlList(Integer page, Integer size, String keyword);
    
    /**
     * 获取URL详情
     */
    UrlDetailResponse getUrlDetail(String shortCode);
    
    /**
     * 删除URL
     */
    void deleteUrl(String shortCode);
    
    /**
     * 获取访问统计
     */
    VisitStatsResponse getVisitStats(String shortCode);
}


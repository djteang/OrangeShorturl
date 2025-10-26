package com.shorturl.service.impl;

import com.shorturl.dto.GlobalStatsResponse;
import com.shorturl.dto.PageResult;
import com.shorturl.entity.VisitLog;
import com.shorturl.mapper.UrlMappingMapper;
import com.shorturl.mapper.VisitLogMapper;
import com.shorturl.service.VisitLogService;
import com.shorturl.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 访问日志服务实现
 */
@Service
public class VisitLogServiceImpl implements VisitLogService {
    
    @Autowired
    private VisitLogMapper visitLogMapper;
    
    @Autowired
    private UrlMappingMapper urlMappingMapper;
    
    @Override
    public PageResult<VisitLog> getVisitLogList(Integer page, Integer size, String shortCode) {
        // 获取当前登录用户ID
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        String token = request.getHeader("Authorization");
        
        if (token == null || !token.startsWith("Bearer ")) {
            throw new RuntimeException("未登录");
        }
        
        token = token.substring(7);
        Long userId = JwtUtils.getUserIdFromToken(token);
        
        if (userId == null) {
            throw new RuntimeException("未登录");
        }
        
        // 计算偏移量
        int offset = (page - 1) * size;
        
        // 查询数据
        List<VisitLog> list = visitLogMapper.selectByUserIdWithPage(userId, shortCode, offset, size);
        Integer total = visitLogMapper.countByUserId(userId, shortCode);
        
        PageResult<VisitLog> result = new PageResult<>();
        result.setList(list);
        result.setTotal(total != null ? Long.valueOf(total) : 0L);
        
        return result;
    }
    
    @Override
    public GlobalStatsResponse getGlobalStats() {
        GlobalStatsResponse response = new GlobalStatsResponse();
        
        // 基础统计（全局）
        Long totalVisits = visitLogMapper.countTotalVisits();
        Long todayVisits = visitLogMapper.countTodayVisits();
        Long yesterdayVisits = visitLogMapper.countYesterdayVisits();
        Long totalUrls = urlMappingMapper.selectTotalCount();
        
        response.setTotalVisits(totalVisits != null ? totalVisits : 0L);
        response.setTodayVisits(todayVisits != null ? todayVisits : 0L);
        response.setYesterdayVisits(yesterdayVisits != null ? yesterdayVisits : 0L);
        response.setTotalUrls(totalUrls != null ? totalUrls : 0L);
        
        // 最近7天访问趋势
        List<Map<String, Object>> dailyTrendMaps = visitLogMapper.selectDailyTrends(7);
        List<GlobalStatsResponse.DailyTrend> dailyTrends = new ArrayList<>();
        for (Map<String, Object> map : dailyTrendMaps) {
            GlobalStatsResponse.DailyTrend trend = new GlobalStatsResponse.DailyTrend();
            trend.setDate((String) map.get("date"));
            trend.setCount(((Number) map.get("count")).longValue());
            dailyTrends.add(trend);
        }
        response.setDailyTrends(dailyTrends);
        
        // 地域统计
        List<Map<String, Object>> locationMaps = visitLogMapper.selectGlobalLocationStats(10);
        List<GlobalStatsResponse.LocationStat> locationStats = new ArrayList<>();
        for (Map<String, Object> map : locationMaps) {
            GlobalStatsResponse.LocationStat stat = new GlobalStatsResponse.LocationStat();
            stat.setLocation((String) map.get("location"));
            stat.setCount(((Number) map.get("count")).longValue());
            locationStats.add(stat);
        }
        response.setLocationStats(locationStats);
        
        // 设备类型统计
        List<Map<String, Object>> deviceMaps = visitLogMapper.selectGlobalDeviceStats();
        List<GlobalStatsResponse.DeviceStat> deviceStats = new ArrayList<>();
        long totalDeviceCount = 0;
        for (Map<String, Object> map : deviceMaps) {
            totalDeviceCount += ((Number) map.get("count")).longValue();
        }
        for (Map<String, Object> map : deviceMaps) {
            GlobalStatsResponse.DeviceStat stat = new GlobalStatsResponse.DeviceStat();
            stat.setDeviceType((String) map.get("deviceType"));
            long count = ((Number) map.get("count")).longValue();
            stat.setCount(count);
            stat.setPercentage(totalDeviceCount > 0 ? (count * 100.0 / totalDeviceCount) : 0.0);
            deviceStats.add(stat);
        }
        response.setDeviceStats(deviceStats);
        
        // 浏览器统计
        List<Map<String, Object>> browserMaps = visitLogMapper.selectGlobalBrowserStats(10);
        List<GlobalStatsResponse.BrowserStat> browserStats = new ArrayList<>();
        for (Map<String, Object> map : browserMaps) {
            GlobalStatsResponse.BrowserStat stat = new GlobalStatsResponse.BrowserStat();
            stat.setBrowser((String) map.get("browser"));
            stat.setCount(((Number) map.get("count")).longValue());
            browserStats.add(stat);
        }
        response.setBrowserStats(browserStats);
        
        // 操作系统统计
        List<Map<String, Object>> osMaps = visitLogMapper.selectGlobalOsStats(10);
        List<GlobalStatsResponse.OsStat> osStats = new ArrayList<>();
        for (Map<String, Object> map : osMaps) {
            GlobalStatsResponse.OsStat stat = new GlobalStatsResponse.OsStat();
            stat.setOs((String) map.get("os"));
            stat.setCount(((Number) map.get("count")).longValue());
            osStats.add(stat);
        }
        response.setOsStats(osStats);
        
        // 访问量Top 10短链接
        List<Map<String, Object>> topUrlMaps = visitLogMapper.selectTopUrls(10);
        List<GlobalStatsResponse.TopUrl> topUrls = new ArrayList<>();
        for (Map<String, Object> map : topUrlMaps) {
            GlobalStatsResponse.TopUrl topUrl = new GlobalStatsResponse.TopUrl();
            topUrl.setShortCode((String) map.get("shortCode"));
            topUrl.setOriginalUrl((String) map.get("originalUrl"));
            Object visitCount = map.get("visitCount");
            topUrl.setVisitCount(visitCount != null ? ((Number) visitCount).longValue() : 0L);
            topUrls.add(topUrl);
        }
        response.setTopUrls(topUrls);
        
        return response;
    }
}


package com.shorturl.service.impl;

import com.shorturl.dto.*;
import com.shorturl.entity.UrlMapping;
import com.shorturl.entity.VisitLog;
import com.shorturl.mapper.UrlMappingMapper;
import com.shorturl.mapper.VisitLogMapper;
import com.shorturl.service.UrlService;
import com.shorturl.utils.Base62Utils;
import com.shorturl.utils.IpUtils;
import com.shorturl.utils.JwtUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * URL服务实现类
 */
@Service
public class UrlServiceImpl implements UrlService {
    
    @Autowired
    private UrlMappingMapper urlMappingMapper;
    
    @Autowired
    private VisitLogMapper visitLogMapper;
    
    @Value("${shorturl.domain}")
    private String domain;
    
    @Override
    @Transactional
    public ShortenResponse shorten(ShortenRequest request) {
        String shortCode;
        
        // 如果提供了自定义短码
        if (request.getCustomCode() != null && !request.getCustomCode().isEmpty()) {
            shortCode = request.getCustomCode();
            // 检查短码是否已存在
            if (urlMappingMapper.existsByShortCode(shortCode) > 0) {
                throw new RuntimeException("短码已存在，请更换");
            }
        } else {
            // 使用时间戳和随机数生成短码
            long timestamp = System.currentTimeMillis();
            shortCode = Base62Utils.encode(timestamp);
            
            // 确保短码唯一
            while (urlMappingMapper.existsByShortCode(shortCode) > 0) {
                timestamp = System.currentTimeMillis() + (long)(Math.random() * 1000);
                shortCode = Base62Utils.encode(timestamp);
            }
        }
        
        // 获取当前登录用户ID（如果已登录）
        Long userId = getCurrentUserId();
        
        // 创建URL映射
        UrlMapping urlMapping = new UrlMapping();
        urlMapping.setUserId(userId);  // 设置用户ID，未登录时为null
        urlMapping.setShortCode(shortCode);
        urlMapping.setOriginalUrl(request.getOriginalUrl());
        urlMapping.setCreateTime(new Date());
        urlMapping.setStatus(1);
        urlMapping.setVisitCount(0);
        
        // 处理过期时间
        if (request.getExpireTime() != null && !request.getExpireTime().isEmpty()) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                urlMapping.setExpireTime(sdf.parse(request.getExpireTime()));
            } catch (Exception e) {
                throw new RuntimeException("过期时间格式不正确");
            }
        }
        
        // 保存到数据库
        urlMappingMapper.insert(urlMapping);
        
        // 构建响应
        return ShortenResponse.builder()
                .shortCode(shortCode)
                .shortUrl(domain + "/r/" + shortCode)  // 使用/r/前缀，让前端路由拦截
                .originalUrl(request.getOriginalUrl())
                .build();
    }
    
    @Override
    @Transactional
    public String getOriginalUrl(String shortCode, HttpServletRequest request) {
        // 查询URL映射
        UrlMapping urlMapping = urlMappingMapper.selectByShortCode(shortCode);
        
        if (urlMapping == null) {
            throw new RuntimeException("短链接不存在");
        }
        
        // 检查是否过期
        if (urlMapping.getExpireTime() != null && 
            urlMapping.getExpireTime().before(new Date())) {
            throw new RuntimeException("短链接已过期");
        }
        
        // 更新访问信息
        urlMappingMapper.updateVisitInfo(shortCode);
        
        // 记录访问日志
        VisitLog visitLog = new VisitLog();
        visitLog.setShortCode(shortCode);
        visitLog.setVisitTime(new Date());
        visitLog.setIpAddress(IpUtils.getClientIp(request));
        visitLog.setUserAgent(request.getHeader("User-Agent"));
        visitLog.setReferer(request.getHeader("Referer"));
        visitLogMapper.insert(visitLog);
        
        return urlMapping.getOriginalUrl();
    }
    
    @Override
    public PageResult<UrlDetailResponse> getUrlList(Integer page, Integer size, String keyword) {
        // 获取当前登录用户ID
        Long userId = getCurrentUserId();
        
        // 计算偏移量
        int offset = (page - 1) * size;
        
        // 查询列表（只查询当前用户的数据）
        List<UrlMapping> list = urlMappingMapper.selectListByUserId(userId, keyword, offset, size);
        
        // 转换为响应DTO
        List<UrlDetailResponse> responseList = list.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
        
        // 查询总数
        Long total = urlMappingMapper.selectCountByUserId(userId, keyword);
        
        return new PageResult<>(total, responseList);
    }
    
    @Override
    public UrlDetailResponse getUrlDetail(String shortCode) {
        UrlMapping urlMapping = urlMappingMapper.selectByShortCode(shortCode);
        
        if (urlMapping == null) {
            throw new RuntimeException("短链接不存在");
        }
        
        return convertToResponse(urlMapping);
    }
    
    @Override
    @Transactional
    public void deleteUrl(String shortCode) {
        int result = urlMappingMapper.deleteByShortCode(shortCode);
        if (result == 0) {
            throw new RuntimeException("删除失败，短链接不存在");
        }
    }
    
    @Override
    public VisitStatsResponse getVisitStats(String shortCode) {
        // 查询URL映射
        UrlMapping urlMapping = urlMappingMapper.selectByShortCode(shortCode);
        if (urlMapping == null) {
            throw new RuntimeException("短链接不存在");
        }
        
        VisitStatsResponse response = new VisitStatsResponse();
        response.setTotalVisits(urlMapping.getVisitCount());
        
        // 查询最近访问记录
        List<VisitLog> recentLogs = visitLogMapper.selectRecentVisits(shortCode, 10);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        List<VisitStatsResponse.RecentVisit> recentVisits = recentLogs.stream()
                .map(log -> {
                    VisitStatsResponse.RecentVisit visit = new VisitStatsResponse.RecentVisit();
                    visit.setVisitTime(sdf.format(log.getVisitTime()));
                    visit.setIpAddress(log.getIpAddress());
                    visit.setUserAgent(log.getUserAgent());
                    return visit;
                })
                .collect(Collectors.toList());
        response.setRecentVisits(recentVisits);
        
        // 查询每日统计（最近7天）
        List<Map<String, Object>> dailyData = visitLogMapper.selectDailyStats(shortCode, 7);
        List<VisitStatsResponse.DailyStats> dailyStats = dailyData.stream()
                .map(data -> {
                    VisitStatsResponse.DailyStats stats = new VisitStatsResponse.DailyStats();
                    stats.setDate((String) data.get("date"));
                    stats.setCount(((Number) data.get("count")).intValue());
                    return stats;
                })
                .collect(Collectors.toList());
        response.setDailyStats(dailyStats);
        
        return response;
    }
    
    /**
     * 获取当前登录用户ID（如果已登录）
     */
    private Long getCurrentUserId() {
        try {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes == null) {
                return null;
            }
            
            HttpServletRequest request = attributes.getRequest();
            String token = request.getHeader("Authorization");
            
            if (token == null || !token.startsWith("Bearer ")) {
                return null;
            }
            
            token = token.substring(7);
            return JwtUtils.getUserIdFromToken(token);
        } catch (Exception e) {
            // 如果获取失败，返回null（表示未登录或token无效）
            return null;
        }
    }
    
    /**
     * 转换为响应DTO
     */
    private UrlDetailResponse convertToResponse(UrlMapping urlMapping) {
        UrlDetailResponse response = new UrlDetailResponse();
        BeanUtils.copyProperties(urlMapping, response);
        response.setShortUrl(domain + "/r/" + urlMapping.getShortCode());  // 使用/r/前缀，让前端路由拦截
        return response;
    }
}


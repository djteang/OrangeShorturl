package com.shorturl.service.impl;

import com.shorturl.dto.*;
import com.shorturl.entity.UaBlacklist;
import com.shorturl.entity.UrlMapping;
import com.shorturl.entity.VisitLog;
import com.shorturl.exception.BusinessException;
import com.shorturl.mapper.UaBlacklistMapper;
import com.shorturl.mapper.UrlMappingMapper;
import com.shorturl.mapper.VisitLogMapper;
import com.shorturl.service.AiModerationService;
import com.shorturl.service.UrlService;
import com.shorturl.utils.Base62Utils;
import com.shorturl.utils.HtmlUtils;
import com.shorturl.utils.IpUtils;
import com.shorturl.utils.JwtUtils;
import com.shorturl.utils.IP2CityUtil;
import com.shorturl.utils.UserAgentUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
@Service
public class UrlServiceImpl implements UrlService {
    
    @Autowired
    private UrlMappingMapper urlMappingMapper;
    
    @Autowired
    private VisitLogMapper visitLogMapper;
    
    @Autowired
    private UaBlacklistMapper uaBlacklistMapper;
    
    @Autowired
    private AiModerationService aiModerationService;
    
    @Value("${shorturl.domain}")
    private String domain;
    
    @Override
    @Transactional
    public ShortenResponse shorten(ShortenRequest request) {
        try {
            // AI内容审核
            if (!aiModerationService.moderateUrl(request.getOriginalUrl())) {
                throw new BusinessException(aiModerationService.getModerationReason());
            }
            
            String shortCode;
            
            // 如果提供了自定义短码
            if (request.getCustomCode() != null && !request.getCustomCode().isEmpty()) {
                shortCode = request.getCustomCode();
                // 检查短码是否已存在
                if (urlMappingMapper.existsByShortCode(shortCode) > 0) {
                    throw new BusinessException("短码已存在，请更换");
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
        
        // 尝试自动获取网页标题
        String title = HtmlUtils.fetchTitle(request.getOriginalUrl());
        
        // 创建URL映射
        UrlMapping urlMapping = new UrlMapping();
        urlMapping.setUserId(userId);  // 设置用户ID，未登录时为null
        urlMapping.setGroupId(request.getGroupId());  // 设置分组ID
        urlMapping.setShortCode(shortCode);
        urlMapping.setOriginalUrl(request.getOriginalUrl());
        urlMapping.setTitle(title);  // 设置标题，获取失败时为null
        urlMapping.setCreateTime(new Date());
        urlMapping.setStatus(1);
        urlMapping.setVisitCount(0);
        
        // 处理二维码配置
        if (request.getQrConfig() != null) {
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                String qrConfigJson = objectMapper.writeValueAsString(request.getQrConfig());
                urlMapping.setQrConfig(qrConfigJson);
            } catch (Exception e) {
                log.error("序列化二维码配置失败: {}", e.getMessage());
            }
        }
        
            // 处理过期时间
            if (request.getExpireTime() != null && !request.getExpireTime().isEmpty()) {
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    urlMapping.setExpireTime(sdf.parse(request.getExpireTime()));
                } catch (Exception e) {
                    throw new BusinessException("过期时间格式不正确");
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
        } catch (BusinessException e) {
            log.error("生成短链接失败: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("生成短链接异常: ", e);
            throw new BusinessException("生成短链接失败，请稍后重试");
        }
    }
    
    @Override
    @Transactional
    public String getOriginalUrl(String shortCode, HttpServletRequest request) {
        try {
            // 检查User-Agent是否在黑名单中
            String userAgent = request.getHeader("User-Agent");
            if (userAgent != null && isUserAgentBlocked(userAgent)) {
                throw new BusinessException("访问被拒绝");
            }
            
            // 查询URL映射
            UrlMapping urlMapping = urlMappingMapper.selectByShortCode(shortCode);
            
            if (urlMapping == null) {
                throw new BusinessException("短链接不存在");
            }
            
            // 检查是否过期
            if (urlMapping.getExpireTime() != null && 
                urlMapping.getExpireTime().before(new Date())) {
                throw new BusinessException("短链接已过期");
            }
        
        // 更新访问信息
        urlMappingMapper.updateVisitInfo(shortCode);
        
        // 记录访问日志
        VisitLog visitLog = new VisitLog();
        visitLog.setShortCode(shortCode);
        visitLog.setVisitTime(new Date());
        
        // 获取并解析IP地址
        String ipAddress = IpUtils.getClientIp(request);
        visitLog.setIpAddress(ipAddress);
        
        // 解析IP地域信息
        try {
            String cityInfo = IP2CityUtil.getCityInfo(ipAddress);
            if (cityInfo != null) {
                String[] parts = cityInfo.split("-");
                if (parts.length >= 4) {
                    visitLog.setCountry(parts[0]);
                    visitLog.setProvince(parts[2]);
                    visitLog.setCity(parts[3]);
                    if (parts.length > 4) {
                        visitLog.setIsp(parts[4]);
                    }
                }
            }
        } catch (Exception e) {
            log.error("解析IP地域信息失败: {}", e.getMessage());
        }
        
        // 解析User-Agent信息
        String userAgentStr = request.getHeader("User-Agent");
        visitLog.setUserAgent(userAgentStr);
        try {
            Map<String, String> uaInfo = UserAgentUtils.parseUserAgent(userAgentStr);
            visitLog.setBrowser(uaInfo.get("browser"));
            visitLog.setBrowserVersion(uaInfo.get("browserVersion"));
            visitLog.setOs(uaInfo.get("os"));
            visitLog.setOsVersion(uaInfo.get("osVersion"));
            visitLog.setDeviceType(uaInfo.get("deviceType"));
            visitLog.setDeviceBrand(uaInfo.get("deviceBrand"));
            visitLog.setDeviceModel(uaInfo.get("deviceModel"));
        } catch (Exception e) {
            log.error("解析User-Agent信息失败: {}", e.getMessage());
        }
        
            visitLog.setReferer(request.getHeader("Referer"));
            visitLogMapper.insert(visitLog);
            
            return urlMapping.getOriginalUrl();
        } catch (BusinessException e) {
            log.error("获取原始URL失败: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("获取原始URL异常: ", e);
            throw new BusinessException("访问短链接失败，请稍后重试");
        }
    }
    
    @Override
    public PageResult<UrlDetailResponse> getUrlList(Integer page, Integer size, String keyword) {
        try {
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
        } catch (Exception e) {
            log.error("获取URL列表异常: ", e);
            throw new BusinessException("获取链接列表失败，请稍后重试");
        }
    }
    
    @Override
    public UrlDetailResponse getUrlDetail(String shortCode) {
        try {
            UrlMapping urlMapping = urlMappingMapper.selectByShortCode(shortCode);
            
            if (urlMapping == null) {
                throw new BusinessException("短链接不存在");
            }
            
            return convertToResponse(urlMapping);
        } catch (BusinessException e) {
            log.error("获取URL详情失败: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("获取URL详情异常: ", e);
            throw new BusinessException("获取链接详情失败，请稍后重试");
        }
    }
    
    @Override
    @Transactional
    public void deleteUrl(String shortCode) {
        try {
            int result = urlMappingMapper.deleteByShortCode(shortCode);
            if (result == 0) {
                throw new BusinessException("删除失败，短链接不存在");
            }
        } catch (BusinessException e) {
            log.error("删除URL失败: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("删除URL异常: ", e);
            throw new BusinessException("删除链接失败，请稍后重试");
        }
    }
    
    @Override
    public VisitStatsResponse getVisitStats(String shortCode) {
        try {
            // 查询URL映射
            UrlMapping urlMapping = urlMappingMapper.selectByShortCode(shortCode);
            if (urlMapping == null) {
                throw new BusinessException("短链接不存在");
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
                    visit.setCountry(log.getCountry());
                    visit.setProvince(log.getProvince());
                    visit.setCity(log.getCity());
                    visit.setUserAgent(log.getUserAgent());
                    visit.setBrowser(log.getBrowser());
                    visit.setOs(log.getOs());
                    visit.setDeviceType(log.getDeviceType());
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
        
        // 查询地域统计
        List<Map<String, Object>> locationData = visitLogMapper.selectLocationStats(shortCode);
        List<VisitStatsResponse.LocationStats> locationStats = locationData.stream()
                .map(data -> {
                    VisitStatsResponse.LocationStats stats = new VisitStatsResponse.LocationStats();
                    stats.setCountry((String) data.get("country"));
                    stats.setProvince((String) data.get("province"));
                    stats.setCity((String) data.get("city"));
                    stats.setCount(((Number) data.get("count")).intValue());
                    return stats;
                })
                .collect(Collectors.toList());
        response.setLocationStats(locationStats);
        
        // 查询设备类型统计
        List<Map<String, Object>> deviceTypeData = visitLogMapper.selectDeviceTypeStats(shortCode);
        List<VisitStatsResponse.DeviceTypeStats> deviceTypeStats = deviceTypeData.stream()
                .map(data -> {
                    VisitStatsResponse.DeviceTypeStats stats = new VisitStatsResponse.DeviceTypeStats();
                    stats.setDeviceType((String) data.get("deviceType"));
                    stats.setCount(((Number) data.get("count")).intValue());
                    return stats;
                })
                .collect(Collectors.toList());
        response.setDeviceTypeStats(deviceTypeStats);
        
        // 查询浏览器统计
        List<Map<String, Object>> browserData = visitLogMapper.selectBrowserStats(shortCode);
        List<VisitStatsResponse.BrowserStats> browserStats = browserData.stream()
                .map(data -> {
                    VisitStatsResponse.BrowserStats stats = new VisitStatsResponse.BrowserStats();
                    stats.setBrowser((String) data.get("browser"));
                    stats.setCount(((Number) data.get("count")).intValue());
                    return stats;
                })
                .collect(Collectors.toList());
        response.setBrowserStats(browserStats);
        
        // 查询操作系统统计
        List<Map<String, Object>> osData = visitLogMapper.selectOsStats(shortCode);
        List<VisitStatsResponse.OsStats> osStats = osData.stream()
                .map(data -> {
                    VisitStatsResponse.OsStats stats = new VisitStatsResponse.OsStats();
                    stats.setOs((String) data.get("os"));
                    stats.setCount(((Number) data.get("count")).intValue());
                    return stats;
                })
                .collect(Collectors.toList());
            response.setOsStats(osStats);
            
            return response;
        } catch (BusinessException e) {
            log.error("获取访问统计失败: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("获取访问统计异常: ", e);
            throw new BusinessException("获取访问统计失败，请稍后重试");
        }
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
     * 检查User-Agent是否在黑名单中
     */
    private boolean isUserAgentBlocked(String userAgent) {
        if (userAgent == null || userAgent.isEmpty()) {
            return false;
        }
        
        // 获取所有启用的黑名单规则
        List<UaBlacklist> blacklist = uaBlacklistMapper.selectAllEnabled();
        
        // 检查是否匹配任何黑名单规则（支持简单的包含匹配）
        for (UaBlacklist rule : blacklist) {
            if (userAgent.toLowerCase().contains(rule.getUaPattern().toLowerCase())) {
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * 转换为响应DTO
     */
    private UrlDetailResponse convertToResponse(UrlMapping urlMapping) {
        UrlDetailResponse response = new UrlDetailResponse();
        BeanUtils.copyProperties(urlMapping, response);
        response.setShortUrl(domain + "/r/" + urlMapping.getShortCode());  // 使用/r/前缀，让前端路由拦截
        
        // 解析二维码配置
        if (urlMapping.getQrConfig() != null && !urlMapping.getQrConfig().isEmpty()) {
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                QRConfigDTO qrConfig = objectMapper.readValue(urlMapping.getQrConfig(), QRConfigDTO.class);
                response.setQrConfig(qrConfig);
            } catch (Exception e) {
                log.error("反序列化二维码配置失败: {}", e.getMessage());
            }
        }
        
        return response;
    }
}


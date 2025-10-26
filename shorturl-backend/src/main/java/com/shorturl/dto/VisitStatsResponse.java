package com.shorturl.dto;

import lombok.Data;
import java.util.List;
import java.util.Map;

/**
 * 访问统计响应DTO
 */
@Data
public class VisitStatsResponse {
    
    /**
     * 总访问次数
     */
    private Integer totalVisits;
    
    /**
     * 最近访问记录
     */
    private List<RecentVisit> recentVisits;
    
    /**
     * 每日统计
     */
    private List<DailyStats> dailyStats;
    
    /**
     * 地域统计
     */
    private List<LocationStats> locationStats;
    
    /**
     * 设备类型统计
     */
    private List<DeviceTypeStats> deviceTypeStats;
    
    /**
     * 浏览器统计
     */
    private List<BrowserStats> browserStats;
    
    /**
     * 操作系统统计
     */
    private List<OsStats> osStats;
    
    @Data
    public static class RecentVisit {
        private String visitTime;
        private String ipAddress;
        private String country;
        private String province;
        private String city;
        private String userAgent;
        private String browser;
        private String os;
        private String deviceType;
    }
    
    @Data
    public static class DailyStats {
        private String date;
        private Integer count;
    }
    
    @Data
    public static class LocationStats {
        private String country;
        private String province;
        private String city;
        private Integer count;
    }
    
    @Data
    public static class DeviceTypeStats {
        private String deviceType;
        private Integer count;
    }
    
    @Data
    public static class BrowserStats {
        private String browser;
        private Integer count;
    }
    
    @Data
    public static class OsStats {
        private String os;
        private Integer count;
    }
}


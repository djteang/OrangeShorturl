package com.shorturl.dto;

import lombok.Data;
import java.util.List;

/**
 * 全局统计响应DTO
 */
@Data
public class GlobalStatsResponse {
    
    /**
     * 总访问次数
     */
    private Long totalVisits;
    
    /**
     * 总短链接数
     */
    private Long totalUrls;
    
    /**
     * 今日访问次数
     */
    private Long todayVisits;
    
    /**
     * 昨日访问次数
     */
    private Long yesterdayVisits;
    
    /**
     * 最近7天访问趋势
     */
    private List<DailyTrend> dailyTrends;
    
    /**
     * 地域分布统计（Top 10）
     */
    private List<LocationStat> locationStats;
    
    /**
     * 设备类型统计
     */
    private List<DeviceStat> deviceStats;
    
    /**
     * 浏览器统计（Top 10）
     */
    private List<BrowserStat> browserStats;
    
    /**
     * 操作系统统计（Top 10）
     */
    private List<OsStat> osStats;
    
    /**
     * 访问量最高的短链接（Top 10）
     */
    private List<TopUrl> topUrls;
    
    @Data
    public static class DailyTrend {
        private String date;
        private Long count;
    }
    
    @Data
    public static class LocationStat {
        private String location; // 省份或城市
        private Long count;
    }
    
    @Data
    public static class DeviceStat {
        private String deviceType;
        private Long count;
        private Double percentage;
    }
    
    @Data
    public static class BrowserStat {
        private String browser;
        private Long count;
    }
    
    @Data
    public static class OsStat {
        private String os;
        private Long count;
    }
    
    @Data
    public static class TopUrl {
        private String shortCode;
        private String originalUrl;
        private Long visitCount;
    }
}


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
    
    @Data
    public static class RecentVisit {
        private String visitTime;
        private String ipAddress;
        private String userAgent;
    }
    
    @Data
    public static class DailyStats {
        private String date;
        private Integer count;
    }
}


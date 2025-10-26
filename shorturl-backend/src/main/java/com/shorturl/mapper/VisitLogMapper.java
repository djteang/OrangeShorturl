package com.shorturl.mapper;

import com.shorturl.entity.VisitLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Map;

/**
 * 访问记录Mapper接口
 */
@Mapper
public interface VisitLogMapper {
    
    /**
     * 插入访问记录
     */
    int insert(VisitLog visitLog);
    
    /**
     * 查询最近访问记录
     */
    List<VisitLog> selectRecentVisits(@Param("shortCode") String shortCode, 
                                       @Param("limit") Integer limit);
    
    /**
     * 查询每日统计
     */
    List<Map<String, Object>> selectDailyStats(@Param("shortCode") String shortCode, 
                                                @Param("days") Integer days);
    
    /**
     * 根据用户ID查询访问日志列表（分页）
     */
    List<VisitLog> selectByUserIdWithPage(@Param("userId") Long userId,
                                           @Param("shortCode") String shortCode,
                                           @Param("offset") Integer offset,
                                           @Param("size") Integer size);
    
    /**
     * 根据用户ID统计访问日志数量
     */
    Integer countByUserId(@Param("userId") Long userId,
                          @Param("shortCode") String shortCode);
    
    /**
     * 查询地域统计
     */
    List<Map<String, Object>> selectLocationStats(@Param("shortCode") String shortCode);
    
    /**
     * 查询设备类型统计
     */
    List<Map<String, Object>> selectDeviceTypeStats(@Param("shortCode") String shortCode);
    
    /**
     * 查询浏览器统计
     */
    List<Map<String, Object>> selectBrowserStats(@Param("shortCode") String shortCode);
    
    /**
     * 查询操作系统统计
     */
    List<Map<String, Object>> selectOsStats(@Param("shortCode") String shortCode);
}


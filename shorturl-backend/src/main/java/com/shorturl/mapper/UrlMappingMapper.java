package com.shorturl.mapper;

import com.shorturl.entity.UrlMapping;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * URL映射Mapper接口
 */
@Mapper
public interface UrlMappingMapper {
    
    /**
     * 插入URL映射
     */
    int insert(UrlMapping urlMapping);
    
    /**
     * 根据短码查询
     */
    UrlMapping selectByShortCode(@Param("shortCode") String shortCode);
    
    /**
     * 更新访问信息
     */
    int updateVisitInfo(@Param("shortCode") String shortCode);
    
    /**
     * 根据短码删除
     */
    int deleteByShortCode(@Param("shortCode") String shortCode);
    
    /**
     * 分页查询列表
     */
    List<UrlMapping> selectList(@Param("keyword") String keyword, 
                                 @Param("offset") Integer offset, 
                                 @Param("limit") Integer limit);
    
    /**
     * 查询总数
     */
    Long selectCount(@Param("keyword") String keyword);
    
    /**
     * 根据用户ID分页查询列表
     */
    List<UrlMapping> selectListByUserId(@Param("userId") Long userId,
                                         @Param("keyword") String keyword, 
                                         @Param("offset") Integer offset, 
                                         @Param("limit") Integer limit);
    
    /**
     * 根据用户ID查询总数
     */
    Long selectCountByUserId(@Param("userId") Long userId,
                              @Param("keyword") String keyword);
    
    /**
     * 检查短码是否存在
     */
    int existsByShortCode(@Param("shortCode") String shortCode);
}


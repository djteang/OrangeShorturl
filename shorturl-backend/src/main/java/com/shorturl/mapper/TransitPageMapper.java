package com.shorturl.mapper;

import com.shorturl.entity.TransitPage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 自定义中转页Mapper
 */
@Mapper
public interface TransitPageMapper {
    
    /**
     * 根据用户ID查询中转页配置
     */
    TransitPage selectByUserId(@Param("userId") Long userId);
    
    /**
     * 插入中转页配置
     */
    int insert(TransitPage transitPage);
    
    /**
     * 更新中转页配置
     */
    int update(TransitPage transitPage);
    
    /**
     * 更新状态
     */
    int updateStatus(@Param("userId") Long userId, @Param("status") Integer status);
    
    /**
     * 删除配置
     */
    int deleteByUserId(@Param("userId") Long userId);
}


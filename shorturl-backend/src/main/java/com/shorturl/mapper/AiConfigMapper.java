package com.shorturl.mapper;

import com.shorturl.entity.AiConfig;
import org.apache.ibatis.annotations.Mapper;

/**
 * AI配置Mapper
 */
@Mapper
public interface AiConfigMapper {
    
    /**
     * 查询AI配置（只有一条记录）
     */
    AiConfig select();
    
    /**
     * 插入AI配置
     */
    int insert(AiConfig aiConfig);
    
    /**
     * 更新AI配置
     */
    int update(AiConfig aiConfig);
    
    /**
     * 统计配置数量
     */
    int count();
}


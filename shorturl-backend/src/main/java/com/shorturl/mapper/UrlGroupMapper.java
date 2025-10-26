package com.shorturl.mapper;

import com.shorturl.entity.UrlGroup;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * 链接分组Mapper接口
 */
@Mapper
public interface UrlGroupMapper {
    
    /**
     * 插入分组
     */
    int insert(UrlGroup urlGroup);
    
    /**
     * 更新分组
     */
    int update(UrlGroup urlGroup);
    
    /**
     * 根据ID删除
     */
    int deleteById(@Param("id") Long id);
    
    /**
     * 根据ID查询
     */
    UrlGroup selectById(@Param("id") Long id);
    
    /**
     * 根据用户ID查询所有分组
     */
    List<UrlGroup> selectListByUserId(@Param("userId") Long userId);
    
    /**
     * 根据用户ID和分组名称查询
     */
    UrlGroup selectByUserIdAndName(@Param("userId") Long userId, @Param("name") String name);
    
    /**
     * 查询分组下的链接数量
     */
    Long selectUrlCountByGroupId(@Param("groupId") Long groupId);
}


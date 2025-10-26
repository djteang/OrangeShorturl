package com.shorturl.mapper;

import com.shorturl.entity.UaBlacklist;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * User-Agent黑名单Mapper
 */
@Mapper
public interface UaBlacklistMapper {
    
    /**
     * 插入黑名单记录
     */
    @Insert("INSERT INTO ua_blacklist (ua_pattern, description, status) VALUES (#{uaPattern}, #{description}, #{status})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(UaBlacklist uaBlacklist);
    
    /**
     * 查询所有启用的黑名单
     */
    @Select("SELECT * FROM ua_blacklist WHERE status = 1 ORDER BY create_time DESC")
    List<UaBlacklist> selectAllEnabled();
    
    /**
     * 查询所有黑名单（包括禁用的）
     */
    @Select("SELECT * FROM ua_blacklist ORDER BY create_time DESC")
    List<UaBlacklist> selectAll();
    
    /**
     * 根据ID查询
     */
    @Select("SELECT * FROM ua_blacklist WHERE id = #{id}")
    UaBlacklist selectById(Long id);
    
    /**
     * 更新黑名单
     */
    @Update("UPDATE ua_blacklist SET ua_pattern = #{uaPattern}, description = #{description}, status = #{status} WHERE id = #{id}")
    int update(UaBlacklist uaBlacklist);
    
    /**
     * 删除黑名单
     */
    @Delete("DELETE FROM ua_blacklist WHERE id = #{id}")
    int deleteById(Long id);
}


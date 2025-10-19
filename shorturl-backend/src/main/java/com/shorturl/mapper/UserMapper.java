package com.shorturl.mapper;

import com.shorturl.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 用户Mapper接口
 */
@Mapper
public interface UserMapper {
    
    /**
     * 插入用户
     */
    int insert(User user);
    
    /**
     * 根据用户名查询
     */
    User selectByUsername(@Param("username") String username);
    
    /**
     * 根据邮箱查询
     */
    User selectByEmail(@Param("email") String email);
    
    /**
     * 根据ID查询
     */
    User selectById(@Param("id") Long id);
    
    /**
     * 根据OAuth信息查询
     */
    User selectByOAuth(@Param("provider") String provider, @Param("oauthId") String oauthId);
    
    /**
     * 更新最后登录时间
     */
    int updateLastLoginTime(@Param("id") Long id);
    
    /**
     * 更新用户信息
     */
    int update(User user);
}


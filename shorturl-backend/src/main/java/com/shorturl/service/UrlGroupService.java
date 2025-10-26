package com.shorturl.service;

import com.shorturl.dto.UrlGroupRequest;
import com.shorturl.dto.UrlGroupResponse;
import com.shorturl.entity.UrlGroup;
import java.util.List;

/**
 * 链接分组服务接口
 */
public interface UrlGroupService {
    
    /**
     * 创建分组
     */
    UrlGroupResponse createGroup(Long userId, UrlGroupRequest request);
    
    /**
     * 更新分组
     */
    UrlGroupResponse updateGroup(Long userId, Long groupId, UrlGroupRequest request);
    
    /**
     * 删除分组
     */
    void deleteGroup(Long userId, Long groupId);
    
    /**
     * 获取用户的所有分组
     */
    List<UrlGroupResponse> getUserGroups(Long userId);
    
    /**
     * 根据ID获取分组
     */
    UrlGroup getGroupById(Long groupId);
}


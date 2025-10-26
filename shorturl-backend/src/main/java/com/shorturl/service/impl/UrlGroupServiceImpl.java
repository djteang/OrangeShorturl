package com.shorturl.service.impl;

import com.shorturl.dto.UrlGroupRequest;
import com.shorturl.dto.UrlGroupResponse;
import com.shorturl.entity.UrlGroup;
import com.shorturl.exception.BusinessException;
import com.shorturl.mapper.UrlGroupMapper;
import com.shorturl.service.UrlGroupService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 链接分组服务实现类
 */
@Slf4j
@Service
public class UrlGroupServiceImpl implements UrlGroupService {
    
    @Autowired
    private UrlGroupMapper urlGroupMapper;
    
    @Override
    @Transactional
    public UrlGroupResponse createGroup(Long userId, UrlGroupRequest request) {
        try {
            // 检查分组名称是否已存在
            UrlGroup existing = urlGroupMapper.selectByUserIdAndName(userId, request.getName());
            if (existing != null) {
                throw new BusinessException("分组名称已存在");
            }
            
            // 创建分组
            UrlGroup urlGroup = new UrlGroup();
            urlGroup.setUserId(userId);
            urlGroup.setName(request.getName());
            urlGroup.setDescription(request.getDescription());
            urlGroup.setIcon(request.getIcon());
            urlGroup.setColor(request.getColor() != null ? request.getColor() : "#3b82f6");
            urlGroup.setSortOrder(request.getSortOrder() != null ? request.getSortOrder() : 0);
            urlGroup.setStatus(1);
            
            urlGroupMapper.insert(urlGroup);
            
            // 构建响应
            UrlGroupResponse response = new UrlGroupResponse();
            BeanUtils.copyProperties(urlGroup, response);
            response.setUrlCount(0L);
            
            return response;
        } catch (BusinessException e) {
            log.error("创建分组失败: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("创建分组异常: ", e);
            throw new BusinessException("创建分组失败，请稍后重试");
        }
    }
    
    @Override
    @Transactional
    public UrlGroupResponse updateGroup(Long userId, Long groupId, UrlGroupRequest request) {
        try {
            // 检查分组是否存在且属于当前用户
            UrlGroup urlGroup = urlGroupMapper.selectById(groupId);
            if (urlGroup == null || !urlGroup.getUserId().equals(userId)) {
                throw new BusinessException("分组不存在或无权限");
            }
            
            // 如果修改了名称，检查新名称是否已存在
            if (!urlGroup.getName().equals(request.getName())) {
                UrlGroup existing = urlGroupMapper.selectByUserIdAndName(userId, request.getName());
                if (existing != null && !existing.getId().equals(groupId)) {
                    throw new BusinessException("分组名称已存在");
                }
            }
            
            // 更新分组
            urlGroup.setName(request.getName());
            urlGroup.setDescription(request.getDescription());
            urlGroup.setIcon(request.getIcon());
            urlGroup.setColor(request.getColor() != null ? request.getColor() : "#3b82f6");
            urlGroup.setSortOrder(request.getSortOrder() != null ? request.getSortOrder() : 0);
            
            urlGroupMapper.update(urlGroup);
            
            // 构建响应
            UrlGroupResponse response = new UrlGroupResponse();
            BeanUtils.copyProperties(urlGroup, response);
            response.setUrlCount(urlGroupMapper.selectUrlCountByGroupId(groupId));
            
            return response;
        } catch (BusinessException e) {
            log.error("更新分组失败: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("更新分组异常: ", e);
            throw new BusinessException("更新分组失败，请稍后重试");
        }
    }
    
    @Override
    @Transactional
    public void deleteGroup(Long userId, Long groupId) {
        try {
            // 检查分组是否存在且属于当前用户
            UrlGroup urlGroup = urlGroupMapper.selectById(groupId);
            if (urlGroup == null || !urlGroup.getUserId().equals(userId)) {
                throw new BusinessException("分组不存在或无权限");
            }
            
            // 检查分组下是否有链接
            Long urlCount = urlGroupMapper.selectUrlCountByGroupId(groupId);
            if (urlCount > 0) {
                throw new BusinessException("分组下还有链接，无法删除");
            }
            
            urlGroupMapper.deleteById(groupId);
        } catch (BusinessException e) {
            log.error("删除分组失败: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("删除分组异常: ", e);
            throw new BusinessException("删除分组失败，请稍后重试");
        }
    }
    
    @Override
    public List<UrlGroupResponse> getUserGroups(Long userId) {
        try {
            List<UrlGroup> groups = urlGroupMapper.selectListByUserId(userId);
            List<UrlGroupResponse> responses = new ArrayList<>();
            
            for (UrlGroup group : groups) {
                UrlGroupResponse response = new UrlGroupResponse();
                BeanUtils.copyProperties(group, response);
                response.setUrlCount(urlGroupMapper.selectUrlCountByGroupId(group.getId()));
                responses.add(response);
            }
            
            return responses;
        } catch (Exception e) {
            log.error("获取分组列表异常: ", e);
            throw new BusinessException("获取分组列表失败，请稍后重试");
        }
    }
    
    @Override
    public UrlGroup getGroupById(Long groupId) {
        try {
            return urlGroupMapper.selectById(groupId);
        } catch (Exception e) {
            log.error("获取分组详情异常: ", e);
            throw new BusinessException("获取分组详情失败，请稍后重试");
        }
    }
}


package com.shorturl.controller;

import com.shorturl.annotation.RateLimit;
import com.shorturl.dto.Result;
import com.shorturl.dto.UrlGroupRequest;
import com.shorturl.dto.UrlGroupResponse;
import com.shorturl.exception.BusinessException;
import com.shorturl.service.UrlGroupService;
import com.shorturl.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 链接分组控制器
 */
@RestController
@RequestMapping("/url-group")
public class UrlGroupController {
    
    @Autowired
    private UrlGroupService urlGroupService;
    
    /**
     * 创建分组
     */
    @RateLimit(key = "createGroup", time = 60, count = 20, limitType = RateLimit.LimitType.IP)
    @PostMapping("/create")
    public Result<UrlGroupResponse> createGroup(@Validated @RequestBody UrlGroupRequest request, 
                                                  HttpServletRequest httpRequest) {
        try {
            Long userId = JwtUtils.getUserIdFromRequest(httpRequest);
            if (userId == null) {
                return Result.error("请先登录");
            }
            
            UrlGroupResponse response = urlGroupService.createGroup(userId, request);
            return Result.success(response);
        } catch (BusinessException e) {
            return Result.error(e.getMessage());
        } catch (Exception e) {
            return Result.error("操作失败，请稍后重试");
        }
    }
    
    /**
     * 更新分组
     */
    @RateLimit(key = "updateGroup", time = 60, count = 20, limitType = RateLimit.LimitType.IP)
    @PutMapping("/update/{groupId}")
    public Result<UrlGroupResponse> updateGroup(@PathVariable Long groupId,
                                                  @Validated @RequestBody UrlGroupRequest request,
                                                  HttpServletRequest httpRequest) {
        try {
            Long userId = JwtUtils.getUserIdFromRequest(httpRequest);
            if (userId == null) {
                return Result.error("请先登录");
            }
            
            UrlGroupResponse response = urlGroupService.updateGroup(userId, groupId, request);
            return Result.success(response);
        } catch (BusinessException e) {
            return Result.error(e.getMessage());
        } catch (Exception e) {
            return Result.error("操作失败，请稍后重试");
        }
    }
    
    /**
     * 删除分组
     */
    @RateLimit(key = "deleteGroup", time = 60, count = 20, limitType = RateLimit.LimitType.IP)
    @DeleteMapping("/delete/{groupId}")
    public Result<Void> deleteGroup(@PathVariable Long groupId,
                                     HttpServletRequest httpRequest) {
        try {
            Long userId = JwtUtils.getUserIdFromRequest(httpRequest);
            if (userId == null) {
                return Result.error("请先登录");
            }
            
            urlGroupService.deleteGroup(userId, groupId);
            return Result.success();
        } catch (BusinessException e) {
            return Result.error(e.getMessage());
        } catch (Exception e) {
            return Result.error("操作失败，请稍后重试");
        }
    }
    
    /**
     * 获取用户的所有分组
     */
    @RateLimit(key = "getUserGroups", time = 60, count = 30, limitType = RateLimit.LimitType.IP)
    @GetMapping("/list")
    public Result<List<UrlGroupResponse>> getUserGroups(HttpServletRequest httpRequest) {
        try {
            Long userId = JwtUtils.getUserIdFromRequest(httpRequest);
            if (userId == null) {
                return Result.error("请先登录");
            }
            
            List<UrlGroupResponse> groups = urlGroupService.getUserGroups(userId);
            return Result.success(groups);
        } catch (BusinessException e) {
            return Result.error(e.getMessage());
        } catch (Exception e) {
            return Result.error("操作失败，请稍后重试");
        }
    }
}


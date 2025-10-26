package com.shorturl.controller;

import com.shorturl.dto.Result;
import com.shorturl.dto.TransitPageRequest;
import com.shorturl.dto.TransitPageResponse;
import com.shorturl.entity.TransitPage;
import com.shorturl.entity.UrlMapping;
import com.shorturl.mapper.TransitPageMapper;
import com.shorturl.mapper.UrlMappingMapper;
import com.shorturl.utils.JwtUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * 自定义中转页Controller
 */
@RestController
@RequestMapping("/transit")
public class TransitPageController {
    
    @Autowired
    private TransitPageMapper transitPageMapper;
    
    @Autowired
    private UrlMappingMapper urlMappingMapper;
    
    /**
     * 获取当前用户的中转页配置
     */
    @GetMapping
    public Result<TransitPageResponse> getTransitPage(HttpServletRequest request) {
        String authorization = request.getHeader("Authorization");
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            return Result.error(401, "未授权");
        }
        String token = authorization.replace("Bearer ", "");
        Long userId = JwtUtils.getUserIdFromToken(token);
        
        TransitPage transitPage = transitPageMapper.selectByUserId(userId);
        if (transitPage == null) {
            return Result.success(null);
        }
        
        TransitPageResponse response = new TransitPageResponse();
        BeanUtils.copyProperties(transitPage, response);
        return Result.success(response);
    }
    
    /**
     * 保存或更新中转页配置
     */
    @PostMapping("/save")
    public Result<String> saveTransitPage(
            @Valid @RequestBody TransitPageRequest request,
            HttpServletRequest httpRequest) {
        String authorization = httpRequest.getHeader("Authorization");
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            return Result.error(401, "未授权");
        }
        String token = authorization.replace("Bearer ", "");
        Long userId = JwtUtils.getUserIdFromToken(token);
        
        TransitPage existing = transitPageMapper.selectByUserId(userId);
        
        TransitPage transitPage = new TransitPage();
        transitPage.setUserId(userId);
        transitPage.setTitle(request.getTitle());
        transitPage.setContent(request.getContent());
        transitPage.setWaitTime(request.getWaitTime());
        transitPage.setBackgroundColor(request.getBackgroundColor());
        transitPage.setTextColor(request.getTextColor());
        transitPage.setButtonColor(request.getButtonColor());
        transitPage.setPoweredByName(request.getPoweredByName());
        transitPage.setPoweredByIcon(request.getPoweredByIcon());
        transitPage.setStatus(request.getStatus());
        
        int result;
        if (existing == null) {
            result = transitPageMapper.insert(transitPage);
        } else {
            result = transitPageMapper.update(transitPage);
        }
        
        if (result > 0) {
            return Result.success("保存成功");
        } else {
            return Result.error("保存失败");
        }
    }
    
    /**
     * 切换中转页状态
     */
    @PutMapping("/toggle")
    public Result<String> toggleStatus(HttpServletRequest request) {
        String authorization = request.getHeader("Authorization");
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            return Result.error(401, "未授权");
        }
        String token = authorization.replace("Bearer ", "");
        Long userId = JwtUtils.getUserIdFromToken(token);
        
        TransitPage transitPage = transitPageMapper.selectByUserId(userId);
        if (transitPage == null) {
            return Result.error("请先配置中转页");
        }
        
        Integer newStatus = transitPage.getStatus() == 1 ? 0 : 1;
        int result = transitPageMapper.updateStatus(userId, newStatus);
        
        if (result > 0) {
            return Result.success(newStatus == 1 ? "已启用" : "已禁用");
        } else {
            return Result.error("操作失败");
        }
    }
    
    /**
     * 删除中转页配置
     */
    @DeleteMapping
    public Result<String> deleteTransitPage(HttpServletRequest request) {
        String authorization = request.getHeader("Authorization");
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            return Result.error(401, "未授权");
        }
        String token = authorization.replace("Bearer ", "");
        Long userId = JwtUtils.getUserIdFromToken(token);
        
        int result = transitPageMapper.deleteByUserId(userId);
        if (result > 0) {
            return Result.success("删除成功");
        } else {
            return Result.error("删除失败");
        }
    }
    
    /**
     * 根据短链接获取中转页信息（用于显示中转页）
     */
    @GetMapping("/info/{shortCode}")
    public Result<TransitPageResponse> getTransitPageByShortCode(@PathVariable String shortCode) {
        // 根据短链接查询URL映射
        UrlMapping urlMapping = urlMappingMapper.selectByShortCode(shortCode);
        if (urlMapping == null || urlMapping.getUserId() == null) {
            // 短链接不存在或没有关联用户，返回null
            return Result.success(null);
        }
        
        // 根据用户ID查询中转页配置
        TransitPage transitPage = transitPageMapper.selectByUserId(urlMapping.getUserId());
        if (transitPage == null || transitPage.getStatus() == 0) {
            // 没有配置中转页或已禁用，返回null
            return Result.success(null);
        }
        
        // 返回中转页配置
        TransitPageResponse response = new TransitPageResponse();
        BeanUtils.copyProperties(transitPage, response);
        return Result.success(response);
    }
}


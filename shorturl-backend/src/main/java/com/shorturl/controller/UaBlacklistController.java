package com.shorturl.controller;

import com.shorturl.dto.Result;
import com.shorturl.dto.UaBlacklistRequest;
import com.shorturl.dto.UaBlacklistResponse;
import com.shorturl.entity.UaBlacklist;
import com.shorturl.mapper.UaBlacklistMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * UA黑名单控制器
 */
@RestController
@RequestMapping("/ua-blacklist")
public class UaBlacklistController {
    
    @Autowired
    private UaBlacklistMapper uaBlacklistMapper;
    
    /**
     * 获取所有黑名单
     */
    @GetMapping("/list")
    public Result<List<UaBlacklistResponse>> getList() {
        try {
            List<UaBlacklist> list = uaBlacklistMapper.selectAll();
            List<UaBlacklistResponse> responseList = list.stream()
                    .map(this::convertToResponse)
                    .collect(Collectors.toList());
            return Result.success(responseList);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    /**
     * 添加黑名单
     */
    @PostMapping("/add")
    public Result<Void> add(@Validated @RequestBody UaBlacklistRequest request) {
        try {
            UaBlacklist uaBlacklist = new UaBlacklist();
            uaBlacklist.setUaPattern(request.getUaPattern());
            uaBlacklist.setDescription(request.getDescription());
            uaBlacklist.setStatus(request.getStatus() != null ? request.getStatus() : 1);
            
            uaBlacklistMapper.insert(uaBlacklist);
            return Result.success();
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    /**
     * 更新黑名单
     */
    @PutMapping("/update/{id}")
    public Result<Void> update(@PathVariable Long id, @Validated @RequestBody UaBlacklistRequest request) {
        try {
            UaBlacklist uaBlacklist = uaBlacklistMapper.selectById(id);
            if (uaBlacklist == null) {
                return Result.error("黑名单记录不存在");
            }
            
            uaBlacklist.setUaPattern(request.getUaPattern());
            uaBlacklist.setDescription(request.getDescription());
            uaBlacklist.setStatus(request.getStatus() != null ? request.getStatus() : uaBlacklist.getStatus());
            
            uaBlacklistMapper.update(uaBlacklist);
            return Result.success();
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    /**
     * 删除黑名单
     */
    @DeleteMapping("/delete/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        try {
            int result = uaBlacklistMapper.deleteById(id);
            if (result == 0) {
                return Result.error("删除失败，记录不存在");
            }
            return Result.success();
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    /**
     * 切换状态
     */
    @PutMapping("/toggle/{id}")
    public Result<Void> toggleStatus(@PathVariable Long id) {
        try {
            UaBlacklist uaBlacklist = uaBlacklistMapper.selectById(id);
            if (uaBlacklist == null) {
                return Result.error("黑名单记录不存在");
            }
            
            // 切换状态
            uaBlacklist.setStatus(uaBlacklist.getStatus() == 1 ? 0 : 1);
            uaBlacklistMapper.update(uaBlacklist);
            
            return Result.success();
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    /**
     * 转换为响应DTO
     */
    private UaBlacklistResponse convertToResponse(UaBlacklist uaBlacklist) {
        UaBlacklistResponse response = new UaBlacklistResponse();
        BeanUtils.copyProperties(uaBlacklist, response);
        return response;
    }
}


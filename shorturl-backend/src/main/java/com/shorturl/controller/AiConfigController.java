package com.shorturl.controller;

import com.shorturl.dto.AiConfigRequest;
import com.shorturl.dto.AiConfigResponse;
import com.shorturl.dto.Result;
import com.shorturl.entity.AiConfig;
import com.shorturl.entity.User;
import com.shorturl.mapper.AiConfigMapper;
import com.shorturl.mapper.UserMapper;
import com.shorturl.utils.JwtUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * AI配置Controller（仅管理员可访问）
 */
@RestController
@RequestMapping("/admin/ai-config")
public class AiConfigController {
    
    @Autowired
    private AiConfigMapper aiConfigMapper;
    
    @Autowired
    private UserMapper userMapper;
    
    /**
     * 检查是否为管理员
     */
    private boolean isAdmin(HttpServletRequest request) {
        String authorization = request.getHeader("Authorization");
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            return false;
        }
        String token = authorization.replace("Bearer ", "");
        try {
            Long userId = JwtUtils.getUserIdFromToken(token);
            User user = userMapper.selectById(userId);
            return user != null && "admin".equals(user.getRole());
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * 获取AI配置
     */
    @GetMapping
    public Result<AiConfigResponse> getConfig(HttpServletRequest request) {
        if (!isAdmin(request)) {
            return Result.error(403, "无权限访问");
        }
        
        AiConfig config = aiConfigMapper.select();
        if (config == null) {
            return Result.success(null);
        }
        
        AiConfigResponse response = new AiConfigResponse();
        BeanUtils.copyProperties(config, response);
        
        return Result.success(response);
    }
    
    /**
     * 保存或更新AI配置
     */
    @PostMapping("/save")
    public Result<String> saveConfig(
            @Valid @RequestBody AiConfigRequest request,
            HttpServletRequest httpRequest) {
        if (!isAdmin(httpRequest)) {
            return Result.error(403, "无权限操作");
        }
        
        AiConfig config = new AiConfig();
        BeanUtils.copyProperties(request, config);
        
        int count = aiConfigMapper.count();
        int result;
        if (count == 0) {
            result = aiConfigMapper.insert(config);
        } else {
            AiConfig existing = aiConfigMapper.select();
            config.setId(existing.getId());
            result = aiConfigMapper.update(config);
        }
        
        if (result > 0) {
            return Result.success("保存成功");
        } else {
            return Result.error("保存失败");
        }
    }
    
    /**
     * 测试AI接口连接
     */
    @PostMapping("/test")
    public Result<String> testConnection(
            @Valid @RequestBody AiConfigRequest request,
            HttpServletRequest httpRequest) {
        if (!isAdmin(httpRequest)) {
            return Result.error(403, "无权限操作");
        }
        
        try {
            // 使用RestTemplate测试连接
            org.springframework.http.HttpHeaders headers = new org.springframework.http.HttpHeaders();
            headers.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);
            headers.set("Authorization", "Bearer " + request.getApiKey());
            
            // 构建测试消息
            java.util.Map<String, Object> requestBody = new java.util.HashMap<>();
            requestBody.put("model", request.getModelName());
            
            java.util.Map<String, String> message = new java.util.HashMap<>();
            message.put("role", "user");
            message.put("content", "测试连接");
            requestBody.put("messages", new Object[]{message});
            requestBody.put("max_tokens", 10);
            
            org.springframework.http.HttpEntity<java.util.Map<String, Object>> entity = 
                new org.springframework.http.HttpEntity<>(requestBody, headers);
            
            org.springframework.web.client.RestTemplate restTemplate = new org.springframework.web.client.RestTemplate();
            
            // 设置超时时间
            org.springframework.http.client.SimpleClientHttpRequestFactory factory = 
                new org.springframework.http.client.SimpleClientHttpRequestFactory();
            factory.setConnectTimeout(10000);
            factory.setReadTimeout(10000);
            restTemplate.setRequestFactory(factory);
            
            org.springframework.http.ResponseEntity<String> response = restTemplate.postForEntity(
                request.getApiUrl(),
                entity,
                String.class
            );
            
            if (response.getStatusCode().is2xxSuccessful()) {
                return Result.success("API连接测试成功！配置正确");
            } else {
                return Result.error("连接失败: " + response.getStatusCode());
            }
            
        } catch (org.springframework.web.client.HttpClientErrorException e) {
            return Result.error("连接失败: " + e.getMessage());
        } catch (org.springframework.web.client.ResourceAccessException e) {
            return Result.error("连接超时或网络错误: " + e.getMessage());
        } catch (Exception e) {
            return Result.error("连接失败: " + e.getMessage());
        }
    }
}


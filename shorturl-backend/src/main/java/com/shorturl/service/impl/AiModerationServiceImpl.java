package com.shorturl.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shorturl.entity.AiConfig;
import com.shorturl.mapper.AiConfigMapper;
import com.shorturl.service.AiModerationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

/**
 * AI内容审核服务实现类
 */
@Service
public class AiModerationServiceImpl implements AiModerationService {
    
    private static final Logger logger = LoggerFactory.getLogger(AiModerationServiceImpl.class);
    
    @Autowired
    private AiConfigMapper aiConfigMapper;
    
    @Autowired
    private RestTemplate restTemplate;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    private String moderationReason = "";
    
    @Override
    public boolean moderateUrl(String url) {
        // 获取AI配置
        AiConfig config = aiConfigMapper.select();
        
        // 如果未配置或未启用，直接通过
        if (config == null || config.getEnabled() == 0) {
            logger.info("AI审核未启用，URL通过审核: {}", url);
            return true;
        }
        
        try {
            // 构建请求体
            Map<String, Object> requestBody = buildRequestBody(config, url);
            
            // 设置请求头
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", "Bearer " + config.getApiKey());
            
            HttpEntity<String> entity = new HttpEntity<>(
                objectMapper.writeValueAsString(requestBody), 
                headers
            );
            
            // 发送请求
            logger.info("发送AI审核请求: URL={}, Provider={}", url, config.getProvider());
            ResponseEntity<String> response = restTemplate.postForEntity(
                config.getApiUrl(),
                entity,
                String.class
            );
            
            // 解析响应
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                return parseResponse(response.getBody(), config.getProvider());
            } else {
                logger.warn("AI审核请求失败: {}", response.getStatusCode());
                // 请求失败时允许通过，避免影响正常业务
                return true;
            }
            
        } catch (Exception e) {
            logger.error("AI审核异常，允许通过: {}", e.getMessage());
            // 异常时允许通过，避免影响正常业务
            return true;
        }
    }
    
    @Override
    public String getModerationReason() {
        return moderationReason;
    }
    
    /**
     * 构建请求体
     */
    private Map<String, Object> buildRequestBody(AiConfig config, String url) {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", config.getModelName());
        
        // 构建消息
        String userMessage = config.getPrompt().replace("{url}", url);
        
        Map<String, String> message = new HashMap<>();
        message.put("role", "user");
        message.put("content", userMessage);
        
        requestBody.put("messages", new Object[]{message});
        requestBody.put("max_tokens", 65536);
        requestBody.put("temperature", 0.3);
        
        return requestBody;
    }
    
    /**
     * 解析AI响应
     */
    private boolean parseResponse(String responseBody, String provider) {
        try {
            JsonNode root = objectMapper.readTree(responseBody);
            
            // 通用的OpenAI格式解析
            JsonNode choices = root.get("choices");
            if (choices != null && choices.isArray() && choices.size() > 0) {
                JsonNode firstChoice = choices.get(0);
                JsonNode message = firstChoice.get("message");
                if (message != null) {
                    String content = message.get("content").asText().trim();
                    String contentLower = content.toLowerCase();
                    
                    logger.info("AI审核响应: {}", content);
                    
                    // 优先检查明确的合规标识（检查内容开头或前100个字符）
                    String prefix = contentLower.length() > 100 ? contentLower.substring(0, 100) : contentLower;
                    
                    // 如果开头明确表示合规，直接通过
                    if (prefix.startsWith("合规") || prefix.startsWith("通过") || 
                        prefix.startsWith("允许") || prefix.startsWith("正常") ||
                        prefix.startsWith("safe") || prefix.startsWith("compliant") ||
                        prefix.contains("内容合规") || prefix.contains("审核通过")) {
                        moderationReason = "";
                        logger.info("AI审核结果: 合规");
                        return true;
                    }
                    
                    // 检查明确的违规标识
                    if (prefix.startsWith("不合规") || prefix.startsWith("违规") || 
                        prefix.startsWith("不通过") || prefix.startsWith("不允许") ||
                        prefix.startsWith("禁止") || prefix.startsWith("拒绝") ||
                        prefix.startsWith("illegal") || prefix.startsWith("inappropriate") ||
                        prefix.startsWith("dangerous") || prefix.startsWith("violation") ||
                        contentLower.contains("内容不合规") || contentLower.contains("审核不通过") ||
                        contentLower.contains("包含违规") || contentLower.contains("检测到违规")) {
                        moderationReason = "AI检测到URL可能包含违规内容：" + content;
                        logger.warn("AI审核结果: 不合规 - {}", content);
                        return false;
                    }
                    
                    // 如果AI明确说"不包含"违规内容，视为合规
                    if (contentLower.contains("不包含违规") || contentLower.contains("没有违规") ||
                        contentLower.contains("无违规") || contentLower.contains("not contain") ||
                        contentLower.contains("no violation")) {
                        moderationReason = "";
                        logger.info("AI审核结果: 合规（不包含违规内容）");
                        return true;
                    }
                    
                    // 默认通过（避免因为AI回复格式变化导致误判）
                    logger.info("AI审核结果: 默认通过（无明确违规标识）");
                    moderationReason = "";
                    return true;
                }
            }
            
            // 无法解析响应，默认通过
            logger.warn("无法解析AI响应，默认通过");
            return true;
            
        } catch (Exception e) {
            logger.error("解析AI响应失败: {}", e.getMessage());
            return true;
        }
    }
}


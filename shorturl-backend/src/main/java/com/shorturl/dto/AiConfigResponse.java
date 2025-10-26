package com.shorturl.dto;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * AI配置响应DTO
 */
@Data
public class AiConfigResponse {
    
    /**
     * ID
     */
    private Long id;
    
    /**
     * AI厂商
     */
    private String provider;
    
    /**
     * API密钥（脱敏）
     */
    private String apiKey;
    
    /**
     * API地址
     */
    private String apiUrl;
    
    /**
     * 模型名称
     */
    private String modelName;
    
    /**
     * 审核提示词
     */
    private String prompt;
    
    /**
     * 是否启用
     */
    private Integer enabled;
    
    /**
     * 超时时间（秒）
     */
    private Integer timeout;
    
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}


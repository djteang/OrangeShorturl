package com.shorturl.dto;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * AI配置请求DTO
 */
@Data
public class AiConfigRequest {
    
    /**
     * AI厂商
     */
    @NotBlank(message = "AI厂商不能为空")
    private String provider;
    
    /**
     * API密钥
     */
    @NotBlank(message = "API密钥不能为空")
    private String apiKey;
    
    /**
     * API地址
     */
    @NotBlank(message = "API地址不能为空")
    private String apiUrl;
    
    /**
     * 模型名称
     */
    private String modelName;
    
    /**
     * 审核提示词
     */
    @NotBlank(message = "审核提示词不能为空")
    private String prompt;
    
    /**
     * 是否启用
     */
    @NotNull(message = "启用状态不能为空")
    private Integer enabled;
    
    /**
     * 超时时间（秒）
     */
    @NotNull(message = "超时时间不能为空")
    @Min(value = 5, message = "超时时间不能小于5秒")
    @Max(value = 120, message = "超时时间不能超过120秒")
    private Integer timeout;
}


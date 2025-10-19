package com.shorturl.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 短链生成响应DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShortenResponse {
    
    /**
     * 短码
     */
    private String shortCode;
    
    /**
     * 完整短链接URL
     */
    private String shortUrl;
    
    /**
     * 原始URL
     */
    private String originalUrl;
}


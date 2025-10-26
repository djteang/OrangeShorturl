package com.shorturl.dto;

import lombok.Data;

/**
 * 二维码配置DTO
 */
@Data
public class QRConfigDTO {
    
    /**
     * 二维码大小
     */
    private Integer size;
    
    /**
     * 前景色（二维码颜色）
     */
    private String colorDark;
    
    /**
     * 背景色
     */
    private String colorLight;
    
    /**
     * Logo图片（Base64）
     */
    private String logoImage;
    
    /**
     * Logo大小比例（0-0.3）
     */
    private Double logoRatio;
    
    /**
     * 容错率（L/M/Q/H）
     */
    private String correctLevel;
}


package com.shorturl.entity;

import lombok.Data;
import java.util.Date;

/**
 * 访问记录实体类
 */
@Data
public class VisitLog {
    
    /**
     * 主键ID
     */
    private Long id;
    
    /**
     * 短码
     */
    private String shortCode;
    
    /**
     * 访问时间
     */
    private Date visitTime;
    
    /**
     * 访问IP
     */
    private String ipAddress;
    
    /**
     * 国家
     */
    private String country;
    
    /**
     * 省份
     */
    private String province;
    
    /**
     * 城市
     */
    private String city;
    
    /**
     * 运营商
     */
    private String isp;
    
    /**
     * 浏览器UA
     */
    private String userAgent;
    
    /**
     * 浏览器
     */
    private String browser;
    
    /**
     * 浏览器版本
     */
    private String browserVersion;
    
    /**
     * 操作系统
     */
    private String os;
    
    /**
     * 操作系统版本
     */
    private String osVersion;
    
    /**
     * 设备类型（Mobile/Desktop/Tablet）
     */
    private String deviceType;
    
    /**
     * 设备品牌
     */
    private String deviceBrand;
    
    /**
     * 设备型号
     */
    private String deviceModel;
    
    /**
     * 来源
     */
    private String referer;
}


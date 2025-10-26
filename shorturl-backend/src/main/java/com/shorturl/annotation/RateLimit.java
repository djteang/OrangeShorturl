package com.shorturl.annotation;

import java.lang.annotation.*;

/**
 * 接口限流注解
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RateLimit {
    
    /**
     * 限流key前缀
     */
    String key() default "rate_limit";
    
    /**
     * 时间窗口（秒）
     */
    int time() default 60;
    
    /**
     * 允许的请求次数
     */
    int count() default 10;
    
    /**
     * 限流类型（IP或用户）
     */
    LimitType limitType() default LimitType.IP;
    
    enum LimitType {
        IP,      // 基于IP限流
        USER,    // 基于用户限流
        GLOBAL   // 全局限流
    }
}


package com.shorturl.aspect;

import com.shorturl.annotation.RateLimit;
import com.shorturl.dto.Result;
import com.shorturl.utils.IpUtils;
import com.shorturl.utils.JwtUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;

/**
 * 接口限流切面
 */
@Aspect
@Component
public class RateLimitAspect {
    
    private static final Logger logger = LoggerFactory.getLogger(RateLimitAspect.class);
    
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    
    /**
     * 限流Lua脚本
     */
    private DefaultRedisScript<String> rateLimitScript;
    
    /**
     * 初始化Lua脚本
     */
    @PostConstruct
    public void init() {
        rateLimitScript = new DefaultRedisScript<>();
        rateLimitScript.setResultType(String.class);
        try {
            ClassPathResource resource = new ClassPathResource("lua/rate_limit.lua");
            // 使用Spring的工具类读取脚本，兼容Java 8
            org.springframework.util.StreamUtils.copy(
                resource.getInputStream(),
                new java.io.ByteArrayOutputStream()
            );
            
            rateLimitScript.setLocation(resource);
            logger.info("限流Lua脚本加载成功");
        } catch (Exception e) {
            logger.error("限流Lua脚本加载失败", e);
            throw new RuntimeException("限流Lua脚本加载失败", e);
        }
    }
    
    @Around("@annotation(com.shorturl.annotation.RateLimit)")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();
        RateLimit rateLimit = method.getAnnotation(RateLimit.class);
        
        if (rateLimit != null) {
            try {
                String key = getCombineKey(rateLimit);
                
                // 记录调试信息
                logger.debug("限流检查 - key: {}, count: {}, time: {}", 
                    key, rateLimit.count(), rateLimit.time());
                
                // 执行Lua脚本进行限流检查
                List<String> keys = Collections.singletonList(key);
                StringRedisSerializer serializer = new StringRedisSerializer();

                // 使用StringRedisSerializer确保参数作为普通字符串传递
                String resultStr = redisTemplate.execute(
                    rateLimitScript,
                    serializer,
                    serializer,
                    keys,
                    String.valueOf(rateLimit.count()),
                    String.valueOf(rateLimit.time())
                );

                Long result = resultStr != null ? Long.parseLong(resultStr) : null;
                logger.debug("限流检查结果 - key: {}, result: {}", key, result);
                
                // result为0表示被限流，result为1表示允许访问
                if (result != null && result == 0) {
                    logger.warn("接口限流触发：{} 在 {} 秒内超过 {} 次请求", 
                        key, rateLimit.time(), rateLimit.count());
                    
                    // 返回429状态码，表示请求过于频繁
                    return Result.rateLimit("请求过于频繁，请稍后再试");
                }
                
                // result为null或非0值都允许访问
                if (result == null) {
                    logger.warn("限流检查返回null，允许请求通过");
                }
                
            } catch (Exception e) {
                // Redis执行失败时，记录日志但不阻止请求
                logger.error("限流检查失败，允许请求通过", e);
            }
        }
        
        return point.proceed();
    }
    
    /**
     * 生成限流key
     */
    private String getCombineKey(RateLimit rateLimit) {
        StringBuilder key = new StringBuilder("rate_limit:");
        key.append(rateLimit.key());
        key.append(":");
        
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            return key.append("unknown").toString();
        }
        
        HttpServletRequest request = attributes.getRequest();
        
        switch (rateLimit.limitType()) {
            case IP:
                key.append(IpUtils.getClientIp(request));
                break;
            case USER:
                String token = request.getHeader("Authorization");
                if (token != null && token.startsWith("Bearer ")) {
                    try {
                        Long userId = JwtUtils.getUserIdFromToken(token.substring(7));
                        key.append("user_").append(userId);
                    } catch (Exception e) {
                        // token无效，使用IP作为key
                        key.append("ip_").append(IpUtils.getClientIp(request));
                    }
                } else {
                    // 未登录，使用IP作为key
                    key.append("ip_").append(IpUtils.getClientIp(request));
                }
                break;
            case GLOBAL:
                key.append("global");
                break;
        }
        
        return key.toString();
    }
}


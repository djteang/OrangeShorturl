package com.shorturl.service.impl;

import com.shorturl.dto.PageResult;
import com.shorturl.entity.VisitLog;
import com.shorturl.mapper.VisitLogMapper;
import com.shorturl.service.VisitLogService;
import com.shorturl.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 访问日志服务实现
 */
@Service
public class VisitLogServiceImpl implements VisitLogService {
    
    @Autowired
    private VisitLogMapper visitLogMapper;
    
    @Override
    public PageResult<VisitLog> getVisitLogList(Integer page, Integer size, String shortCode) {
        // 获取当前登录用户ID
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        String token = request.getHeader("Authorization");
        
        if (token == null || !token.startsWith("Bearer ")) {
            throw new RuntimeException("未登录");
        }
        
        token = token.substring(7);
        Long userId = JwtUtils.getUserIdFromToken(token);
        
        if (userId == null) {
            throw new RuntimeException("未登录");
        }
        
        // 计算偏移量
        int offset = (page - 1) * size;
        
        // 查询数据
        List<VisitLog> list = visitLogMapper.selectByUserIdWithPage(userId, shortCode, offset, size);
        Integer total = visitLogMapper.countByUserId(userId, shortCode);
        
        PageResult<VisitLog> result = new PageResult<>();
        result.setList(list);
        result.setTotal(total != null ? Long.valueOf(total) : 0L);
        
        return result;
    }
}


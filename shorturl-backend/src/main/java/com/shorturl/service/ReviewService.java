package com.shorturl.service;

import com.shorturl.dto.ReviewRequest;
import com.shorturl.dto.ReviewResponse;
import java.util.List;

/**
 * 评价服务接口
 */
public interface ReviewService {
    
    /**
     * 提交评价
     */
    void submitReview(Long userId, ReviewRequest request);
    
    /**
     * 获取评价列表
     */
    List<ReviewResponse> getReviewList(Integer limit);
}


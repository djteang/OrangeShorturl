package com.shorturl.service.impl;

import com.shorturl.dto.ReviewRequest;
import com.shorturl.dto.ReviewResponse;
import com.shorturl.entity.User;
import com.shorturl.entity.UserReview;
import com.shorturl.mapper.UserMapper;
import com.shorturl.mapper.UserReviewMapper;
import com.shorturl.service.ReviewService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 评价服务实现类
 */
@Service
public class ReviewServiceImpl implements ReviewService {
    
    @Autowired
    private UserReviewMapper reviewMapper;
    
    @Autowired
    private UserMapper userMapper;
    
    @Override
    public void submitReview(Long userId, ReviewRequest request) {
        // 查询用户信息
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        
        // 始终新增评价（支持同一用户多次评价）
        UserReview review = new UserReview();
        review.setUserId(userId);
        review.setUsername(user.getUsername());
        review.setAvatarBase64(user.getAvatarBase64());  // 使用用户当前的头像
        review.setContent(request.getContent());
        review.setRating(request.getRating());
        review.setCreateTime(new Date());
        review.setStatus(1);
        
        reviewMapper.insert(review);
    }
    
    @Override
    public List<ReviewResponse> getReviewList(Integer limit) {
        List<UserReview> reviews = reviewMapper.selectList(limit);
        
        return reviews.stream()
                .map(review -> {
                    ReviewResponse response = new ReviewResponse();
                    BeanUtils.copyProperties(review, response);
                    return response;
                })
                .collect(Collectors.toList());
    }
}


package com.shorturl.controller;

import com.shorturl.dto.Result;
import com.shorturl.dto.ReviewRequest;
import com.shorturl.dto.ReviewResponse;
import com.shorturl.service.ReviewService;
import com.shorturl.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 评价控制器
 */
@RestController
@RequestMapping("/review")
public class ReviewController {
    
    @Autowired
    private ReviewService reviewService;
    
    /**
     * 提交评价
     */
    @PostMapping("/submit")
    public Result<Void> submitReview(@RequestHeader("Authorization") String authorization,
                                     @Validated @RequestBody ReviewRequest request) {
        try {
            String token = authorization.replace("Bearer ", "");
            Long userId = JwtUtils.getUserIdFromToken(token);
            reviewService.submitReview(userId, request);
            return Result.success();
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    /**
     * 获取评价列表
     */
    @GetMapping("/list")
    public Result<List<ReviewResponse>> getReviewList(@RequestParam(defaultValue = "10") Integer limit) {
        try {
            List<ReviewResponse> list = reviewService.getReviewList(limit);
            return Result.success(list);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}


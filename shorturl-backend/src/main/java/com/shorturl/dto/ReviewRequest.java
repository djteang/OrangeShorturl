package com.shorturl.dto;

import lombok.Data;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

/**
 * 评价请求DTO
 */
@Data
public class ReviewRequest {
    
    /**
     * 评价内容
     */
    @NotBlank(message = "评价内容不能为空")
    private String content;
    
    /**
     * 评分
     */
    @Min(value = 1, message = "评分最低1星")
    @Max(value = 5, message = "评分最高5星")
    private Integer rating;
}


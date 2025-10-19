package com.shorturl.mapper;

import com.shorturl.entity.UserReview;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * 用户评价Mapper接口
 */
@Mapper
public interface UserReviewMapper {
    
    /**
     * 插入评价
     */
    int insert(UserReview review);
    
    /**
     * 查询显示的评价列表
     */
    List<UserReview> selectList(@Param("limit") Integer limit);
    
    /**
     * 查询用户的评价
     */
    UserReview selectByUserId(@Param("userId") Long userId);
    
    /**
     * 更新评价
     */
    int update(UserReview review);
    
    /**
     * 删除评价
     */
    int delete(@Param("id") Long id);
}


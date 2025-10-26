package com.shorturl.service;

/**
 * AI内容审核服务接口
 */
public interface AiModerationService {
    
    /**
     * 审核URL内容是否合规
     * @param url 待审核的URL
     * @return true-合规，false-不合规
     */
    boolean moderateUrl(String url);
    
    /**
     * 获取审核失败原因
     * @return 失败原因
     */
    String getModerationReason();
}


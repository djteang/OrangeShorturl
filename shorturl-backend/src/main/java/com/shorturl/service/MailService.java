package com.shorturl.service;

/**
 * 邮件服务接口
 */
public interface MailService {
    
    /**
     * 发送简单文本邮件
     * 
     * @param to 收件人邮箱
     * @param subject 邮件主题
     * @param content 邮件内容
     */
    void sendSimpleMail(String to, String subject, String content);
    
    /**
     * 发送HTML邮件
     * 
     * @param to 收件人邮箱
     * @param subject 邮件主题
     * @param htmlContent HTML内容
     */
    void sendHtmlMail(String to, String subject, String htmlContent);
    
    /**
     * 发送验证码邮件
     * 
     * @param to 收件人邮箱
     * @param code 验证码
     */
    void sendVerifyCodeMail(String to, String code);
}





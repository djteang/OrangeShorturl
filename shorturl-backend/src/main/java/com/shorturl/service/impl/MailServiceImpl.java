package com.shorturl.service.impl;

import com.shorturl.service.MailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

/**
 * 邮件服务实现类
 */
@Service
public class MailServiceImpl implements MailService {
    
    private static final Logger logger = LoggerFactory.getLogger(MailServiceImpl.class);
    
    @Autowired
    private JavaMailSender mailSender;
    
    @Value("${spring.mail.username}")
    private String from;
    
    @Override
    public void sendSimpleMail(String to, String subject, String content) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(from);
            message.setTo(to);
            message.setSubject(subject);
            message.setText(content);
            
            mailSender.send(message);
            logger.info("简单邮件发送成功: to={}, subject={}", to, subject);
        } catch (Exception e) {
            logger.error("简单邮件发送失败: to={}, subject={}", to, subject, e);
            throw new RuntimeException("邮件发送失败: " + e.getMessage());
        }
    }
    
    @Override
    public void sendHtmlMail(String to, String subject, String htmlContent) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            
            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true);
            
            mailSender.send(message);
            logger.info("HTML邮件发送成功: to={}, subject={}", to, subject);
        } catch (MessagingException e) {
            logger.error("HTML邮件发送失败: to={}, subject={}", to, subject, e);
            throw new RuntimeException("邮件发送失败: " + e.getMessage());
        }
    }
    
    @Override
    public void sendVerifyCodeMail(String to, String code) {
        String subject = "Orange ShortUrl - 注册验证码";
        String htmlContent = buildVerifyCodeHtml(code);
        sendHtmlMail(to, subject, htmlContent);
    }
    
    /**
     * 构建验证码邮件HTML内容
     */
    private String buildVerifyCodeHtml(String code) {
        return "<!DOCTYPE html>" +
                "<html>" +
                "<head>" +
                "    <meta charset=\"UTF-8\">" +
                "    <style>" +
                "        body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; }" +
                "        .container { max-width: 600px; margin: 0 auto; padding: 20px; }" +
                "        .header { background: linear-gradient(135deg, #f97316 0%, #fb923c 100%); color: white; padding: 30px; text-align: center; border-radius: 10px 10px 0 0; }" +
                "        .content { background: #f8fafc; padding: 30px; border-radius: 0 0 10px 10px; }" +
                "        .code-box { background: white; border: 2px dashed #f97316; border-radius: 8px; padding: 20px; text-align: center; margin: 20px 0; }" +
                "        .code { font-size: 32px; font-weight: bold; color: #f97316; letter-spacing: 5px; }" +
                "        .footer { text-align: center; margin-top: 20px; color: #64748b; font-size: 12px; }" +
                "    </style>" +
                "</head>" +
                "<body>" +
                "    <div class=\"container\">" +
                "        <div class=\"header\">" +
                "            <h1>🍊 Orange ShortUrl</h1>" +
                "            <p>欢迎注册我们的服务</p>" +
                "        </div>" +
                "        <div class=\"content\">" +
                "            <h2>您的验证码</h2>" +
                "            <p>您正在注册 Orange ShortUrl 账号，请使用以下验证码完成注册：</p>" +
                "            <div class=\"code-box\">" +
                "                <div class=\"code\">" + code + "</div>" +
                "            </div>" +
                "            <p><strong>注意：</strong></p>" +
                "            <ul>" +
                "                <li>验证码有效期为 <strong>5分钟</strong></li>" +
                "                <li>请勿将验证码告诉他人</li>" +
                "                <li>如果这不是您本人的操作，请忽略此邮件</li>" +
                "            </ul>" +
                "        </div>" +
                "        <div class=\"footer\">" +
                "            <p>此邮件由系统自动发送，请勿回复</p>" +
                "            <p>© 2025 Orange ShortUrl - 简洁、高效、易用</p>" +
                "        </div>" +
                "    </div>" +
                "</body>" +
                "</html>";
    }
}






package com.shorturl.dto;

import lombok.Data;
import javax.validation.constraints.NotBlank;

/**
 * 登录请求DTO
 */
@Data
public class LoginRequest {
    
    /**
     * 用户名或邮箱
     */
    @NotBlank(message = "用户名/邮箱不能为空")
    private String username;
    
    /**
     * 密码
     */
    @NotBlank(message = "密码不能为空")
    private String password;
}


package com.shorturl.dto;

import lombok.Data;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * 注册请求DTO
 */
@Data
public class RegisterRequest {
    
    /**
     * 用户名
     */
    @NotBlank(message = "用户名不能为空")
    @Pattern(regexp = "^[a-zA-Z0-9_]{4,20}$", message = "用户名只能包含字母、数字和下划线，长度4-20位")
    private String username;
    
    /**
     * 密码
     */
    @NotBlank(message = "密码不能为空")
    @Pattern(regexp = "^.{6,20}$", message = "密码长度6-20位")
    private String password;
    
    /**
     * 邮箱（必填）
     */
    @NotBlank(message = "邮箱不能为空")
    @Email(message = "邮箱格式不正确")
    private String email;
    
    /**
     * 验证码（必填）
     */
    @NotBlank(message = "验证码不能为空")
    @Pattern(regexp = "^[0-9]{6}$", message = "验证码为6位数字")
    private String verifyCode;
}


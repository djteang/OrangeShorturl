-- Create Database
CREATE DATABASE IF NOT EXISTS shorturl DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE shorturl;

-- URL Mapping Table
CREATE TABLE IF NOT EXISTS url_mapping (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    short_code VARCHAR(20) NOT NULL COMMENT '短码',
    original_url VARCHAR(2048) NOT NULL COMMENT '原始URL',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    expire_time DATETIME DEFAULT NULL COMMENT '过期时间',
    visit_count INT NOT NULL DEFAULT 0 COMMENT '访问次数',
    last_visit_time DATETIME DEFAULT NULL COMMENT '最后访问时间',
    status TINYINT NOT NULL DEFAULT 1 COMMENT '状态：0-无效，1-有效',
    UNIQUE KEY uk_short_code (short_code),
    KEY idx_create_time (create_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='短链接映射表';

-- Visit Log Table
CREATE TABLE IF NOT EXISTS visit_log (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    short_code VARCHAR(20) NOT NULL COMMENT '短码',
    visit_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '访问时间',
    ip_address VARCHAR(50) DEFAULT NULL COMMENT '访问IP',
    user_agent VARCHAR(500) DEFAULT NULL COMMENT '浏览器UA',
    referer VARCHAR(500) DEFAULT NULL COMMENT '来源',
    KEY idx_short_code (short_code),
    KEY idx_visit_time (visit_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='访问记录表';

-- 用户表
CREATE TABLE IF NOT EXISTS user (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    username VARCHAR(50) NOT NULL COMMENT '用户名',
    password VARCHAR(255) DEFAULT NULL COMMENT '密码（本地注册时使用）',
    email VARCHAR(100) DEFAULT NULL COMMENT '邮箱',
    avatar_base64 TEXT DEFAULT NULL COMMENT '头像Base64',
    oauth_provider VARCHAR(20) DEFAULT NULL COMMENT 'OAuth提供商（linux.do等）',
    oauth_id VARCHAR(100) DEFAULT NULL COMMENT 'OAuth用户ID',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    last_login_time DATETIME DEFAULT NULL COMMENT '最后登录时间',
    status TINYINT NOT NULL DEFAULT 1 COMMENT '状态：0-禁用，1-正常',
    UNIQUE KEY uk_username (username),
    UNIQUE KEY uk_oauth (oauth_provider, oauth_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户表';

-- 用户评价表
CREATE TABLE IF NOT EXISTS user_review (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    username VARCHAR(50) NOT NULL COMMENT '用户名',
    avatar_base64 TEXT DEFAULT NULL COMMENT '用户头像',
    content VARCHAR(500) NOT NULL COMMENT '评价内容',
    rating INT NOT NULL DEFAULT 5 COMMENT '评分（1-5星）',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    status TINYINT NOT NULL DEFAULT 1 COMMENT '状态：0-隐藏，1-显示',
    KEY idx_user_id (user_id),
    KEY idx_create_time (create_time),
    KEY idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户评价表';

-- 修改url_mapping表，添加用户ID字段
ALTER TABLE url_mapping ADD COLUMN user_id BIGINT DEFAULT NULL COMMENT '创建用户ID' AFTER id;
ALTER TABLE url_mapping ADD KEY idx_user_id (user_id);


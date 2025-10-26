-- Create Database
CREATE DATABASE IF NOT EXISTS shorturl DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE shorturl;

-- URL Mapping Table
CREATE TABLE IF NOT EXISTS url_mapping (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    user_id BIGINT DEFAULT NULL COMMENT '创建用户ID',
    group_id BIGINT DEFAULT NULL COMMENT '分组ID',
    short_code VARCHAR(20) NOT NULL COMMENT '短码',
    original_url VARCHAR(2048) NOT NULL COMMENT '原始URL',
    title VARCHAR(500) DEFAULT NULL COMMENT '链接标题',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    expire_time DATETIME DEFAULT NULL COMMENT '过期时间',
    visit_count INT NOT NULL DEFAULT 0 COMMENT '访问次数',
    last_visit_time DATETIME DEFAULT NULL COMMENT '最后访问时间',
    status TINYINT NOT NULL DEFAULT 1 COMMENT '状态：0-无效，1-有效',
    qr_config JSON DEFAULT NULL COMMENT '二维码配置（JSON格式）',
    UNIQUE KEY uk_short_code (short_code),
    KEY idx_create_time (create_time),
    KEY idx_user_id (user_id),
    KEY idx_group_id (group_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='短链接映射表';

-- Visit Log Table
CREATE TABLE IF NOT EXISTS visit_log (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    short_code VARCHAR(20) NOT NULL COMMENT '短码',
    visit_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '访问时间',
    ip_address VARCHAR(50) DEFAULT NULL COMMENT '访问IP',
    country VARCHAR(50) DEFAULT NULL COMMENT '国家',
    province VARCHAR(50) DEFAULT NULL COMMENT '省份',
    city VARCHAR(50) DEFAULT NULL COMMENT '城市',
    isp VARCHAR(50) DEFAULT NULL COMMENT '运营商',
    user_agent VARCHAR(500) DEFAULT NULL COMMENT '浏览器UA',
    browser VARCHAR(50) DEFAULT NULL COMMENT '浏览器',
    browser_version VARCHAR(50) DEFAULT NULL COMMENT '浏览器版本',
    os VARCHAR(50) DEFAULT NULL COMMENT '操作系统',
    os_version VARCHAR(50) DEFAULT NULL COMMENT '操作系统版本',
    device_type VARCHAR(20) DEFAULT NULL COMMENT '设备类型（Mobile/Desktop/Tablet）',
    device_brand VARCHAR(50) DEFAULT NULL COMMENT '设备品牌',
    device_model VARCHAR(50) DEFAULT NULL COMMENT '设备型号',
    referer VARCHAR(500) DEFAULT NULL COMMENT '来源',
    KEY idx_short_code (short_code),
    KEY idx_visit_time (visit_time),
    KEY idx_country (country),
    KEY idx_device_type (device_type)
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
    role VARCHAR(20) NOT NULL DEFAULT 'user' COMMENT '用户角色：admin-管理员，user-普通用户',
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

-- 插入默认管理员用户
-- 用户名: admin
-- 密码: admin123
-- 注意：首次使用时请立即修改默认密码！
INSERT INTO user (username, password, email, role, status) 
SELECT 'admin', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIVI8iXYO6VslY0pZ6jP6tVxB0xJqHtm', 'admin@example.com', 'admin', 1
WHERE NOT EXISTS (SELECT 1 FROM user WHERE username = 'admin');

-- User-Agent黑名单表
CREATE TABLE IF NOT EXISTS ua_blacklist (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    ua_pattern VARCHAR(500) NOT NULL COMMENT 'User-Agent匹配模式',
    description VARCHAR(200) DEFAULT NULL COMMENT '描述',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    status TINYINT NOT NULL DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
    KEY idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='UA黑名单表';

-- AI配置表
CREATE TABLE IF NOT EXISTS ai_config (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    provider VARCHAR(50) NOT NULL COMMENT 'AI厂商（openai, zhipu, deepseek等）',
    api_key VARCHAR(500) NOT NULL COMMENT 'API密钥',
    api_url VARCHAR(500) NOT NULL COMMENT 'API地址',
    model_name VARCHAR(100) DEFAULT 'gpt-3.5-turbo' COMMENT '模型名称',
    prompt TEXT NOT NULL COMMENT '审核提示词',
    enabled TINYINT NOT NULL DEFAULT 1 COMMENT '是否启用：0-禁用，1-启用',
    timeout INT NOT NULL DEFAULT 30 COMMENT '超时时间（秒）',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    KEY idx_enabled (enabled)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='AI配置表';

-- 自定义中转页表
CREATE TABLE IF NOT EXISTS transit_page (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    title VARCHAR(100) NOT NULL DEFAULT '链接跳转中...' COMMENT '中转页标题',
    content VARCHAR(500) DEFAULT '您即将离开本站，请注意安全！' COMMENT '中转页提示内容',
    wait_time INT NOT NULL DEFAULT 3 COMMENT '等待时间（秒）',
    background_color VARCHAR(20) DEFAULT '#f3f4f6' COMMENT '背景颜色',
    text_color VARCHAR(20) DEFAULT '#1f2937' COMMENT '文字颜色',
    button_color VARCHAR(20) DEFAULT '#3b82f6' COMMENT '按钮颜色',
    powered_by_name VARCHAR(50) DEFAULT 'Orange ShortUrl' COMMENT 'Powered by 名称',
    powered_by_icon TEXT DEFAULT NULL COMMENT 'Powered by 图标（Base64）',
    status TINYINT NOT NULL DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE KEY uk_user_id (user_id),
    KEY idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='自定义中转页配置表';

-- 链接分组表
CREATE TABLE IF NOT EXISTS url_group (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    name VARCHAR(50) NOT NULL COMMENT '分组名称',
    description VARCHAR(200) DEFAULT NULL COMMENT '分组描述',
    icon VARCHAR(50) DEFAULT NULL COMMENT '分组图标',
    color VARCHAR(20) DEFAULT '#3b82f6' COMMENT '分组颜色',
    sort_order INT NOT NULL DEFAULT 0 COMMENT '排序顺序',
    status TINYINT NOT NULL DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    KEY idx_user_id (user_id),
    KEY idx_status (status),
    KEY idx_sort_order (sort_order)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='链接分组表';


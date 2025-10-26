-- 接口限流Lua脚本
-- KEYS[1]: 限流key
-- ARGV[1]: 限流次数
-- ARGV[2]: 过期时间（秒）
-- 返回值："1"表示允许访问，"0"表示被限流

local key = KEYS[1]
local limit = tonumber(ARGV[1])
local expire = tonumber(ARGV[2])

-- 先增加计数（INCR命令会自动创建key并初始化为0再加1）
local current = redis.call('incr', key)

-- 如果是第一次访问（计数为1），设置过期时间
if current == 1 then
    redis.call('expire', key, expire)
end

-- 判断是否超过限制
if current > limit then
    return "0"
end

-- 返回1表示允许访问
return "1"


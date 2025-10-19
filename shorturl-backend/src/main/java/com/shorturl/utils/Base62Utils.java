package com.shorturl.utils;

/**
 * Base62编码工具类
 */
public class Base62Utils {
    
    private static final String BASE62_CHARS = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final int BASE = 62;
    
    /**
     * 将数字编码为Base62字符串
     */
    public static String encode(long num) {
        if (num == 0) {
            return String.valueOf(BASE62_CHARS.charAt(0));
        }
        
        StringBuilder sb = new StringBuilder();
        while (num > 0) {
            sb.append(BASE62_CHARS.charAt((int) (num % BASE)));
            num /= BASE;
        }
        
        return sb.reverse().toString();
    }
    
    /**
     * 将Base62字符串解码为数字
     */
    public static long decode(String str) {
        long num = 0;
        for (int i = 0; i < str.length(); i++) {
            num = num * BASE + BASE62_CHARS.indexOf(str.charAt(i));
        }
        return num;
    }
}


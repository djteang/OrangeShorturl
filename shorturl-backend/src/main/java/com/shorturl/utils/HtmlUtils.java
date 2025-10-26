package com.shorturl.utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * HTML工具类
 */
public class HtmlUtils {
    
    private static final Logger logger = LoggerFactory.getLogger(HtmlUtils.class);
    
    /**
     * 获取网页标题
     * @param url 网页URL
     * @return 网页标题，获取失败返回null
     */
    public static String fetchTitle(String url) {
        try {
            // 设置超时时间为5秒，避免阻塞太久
            Document doc = Jsoup.connect(url)
                    .timeout(10000)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36")
                    .get();
            
            String title = doc.title();
            
            // 如果标题太长，截断到500字符
            if (title != null && title.length() > 500) {
                title = title.substring(0, 497) + "...";
            }
            
            return title;
        } catch (Exception e) {
            logger.warn("获取网页标题失败: {}, 错误: {}", url, e.getMessage());
            return null;
        }
    }
}


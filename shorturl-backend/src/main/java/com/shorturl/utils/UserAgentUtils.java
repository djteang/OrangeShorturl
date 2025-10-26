package com.shorturl.utils;

import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * User-Agent解析工具类
 */
@Slf4j
public class UserAgentUtils {

    /**
     * 解析User-Agent字符串
     * @param userAgent User-Agent字符串
     * @return 包含浏览器、操作系统和设备信息的Map
     */
    public static Map<String, String> parseUserAgent(String userAgent) {
        Map<String, String> result = new HashMap<>();
        
        if (userAgent == null || userAgent.isEmpty()) {
            result.put("browser", "未知");
            result.put("browserVersion", "");
            result.put("os", "未知");
            result.put("osVersion", "");
            result.put("deviceType", "未知");
            result.put("deviceBrand", "");
            result.put("deviceModel", "");
            return result;
        }
        
        // 解析浏览器信息
        parseBrowser(userAgent, result);
        
        // 解析操作系统信息
        parseOS(userAgent, result);
        
        // 解析设备信息
        parseDevice(userAgent, result);
        
        return result;
    }
    
    /**
     * 解析浏览器信息
     */
    private static void parseBrowser(String userAgent, Map<String, String> result) {
        // Chrome
        if (userAgent.contains("Edg/") || userAgent.contains("Edge/")) {
            result.put("browser", "Microsoft Edge");
            Pattern pattern = Pattern.compile("Edg[e]?/([0-9.]+)");
            Matcher matcher = pattern.matcher(userAgent);
            if (matcher.find()) {
                result.put("browserVersion", matcher.group(1));
            }
        } else if (userAgent.contains("Chrome/") && !userAgent.contains("Chromium")) {
            result.put("browser", "Chrome");
            Pattern pattern = Pattern.compile("Chrome/([0-9.]+)");
            Matcher matcher = pattern.matcher(userAgent);
            if (matcher.find()) {
                result.put("browserVersion", matcher.group(1));
            }
        } else if (userAgent.contains("Firefox/")) {
            result.put("browser", "Firefox");
            Pattern pattern = Pattern.compile("Firefox/([0-9.]+)");
            Matcher matcher = pattern.matcher(userAgent);
            if (matcher.find()) {
                result.put("browserVersion", matcher.group(1));
            }
        } else if (userAgent.contains("Safari/") && !userAgent.contains("Chrome")) {
            result.put("browser", "Safari");
            Pattern pattern = Pattern.compile("Version/([0-9.]+)");
            Matcher matcher = pattern.matcher(userAgent);
            if (matcher.find()) {
                result.put("browserVersion", matcher.group(1));
            }
        } else if (userAgent.contains("OPR/") || userAgent.contains("Opera/")) {
            result.put("browser", "Opera");
            Pattern pattern = Pattern.compile("(?:OPR|Opera)/([0-9.]+)");
            Matcher matcher = pattern.matcher(userAgent);
            if (matcher.find()) {
                result.put("browserVersion", matcher.group(1));
            }
        } else if (userAgent.contains("MSIE") || userAgent.contains("Trident/")) {
            result.put("browser", "Internet Explorer");
            Pattern pattern = Pattern.compile("(?:MSIE |rv:)([0-9.]+)");
            Matcher matcher = pattern.matcher(userAgent);
            if (matcher.find()) {
                result.put("browserVersion", matcher.group(1));
            }
        } else {
            result.put("browser", "其他");
            result.put("browserVersion", "");
        }
    }
    
    /**
     * 解析操作系统信息
     */
    private static void parseOS(String userAgent, Map<String, String> result) {
        if (userAgent.contains("Windows NT")) {
            result.put("os", "Windows");
            if (userAgent.contains("Windows NT 10.0")) {
                result.put("osVersion", "10");
            } else if (userAgent.contains("Windows NT 6.3")) {
                result.put("osVersion", "8.1");
            } else if (userAgent.contains("Windows NT 6.2")) {
                result.put("osVersion", "8");
            } else if (userAgent.contains("Windows NT 6.1")) {
                result.put("osVersion", "7");
            } else if (userAgent.contains("Windows NT 6.0")) {
                result.put("osVersion", "Vista");
            } else if (userAgent.contains("Windows NT 5.1")) {
                result.put("osVersion", "XP");
            } else {
                Pattern pattern = Pattern.compile("Windows NT ([0-9.]+)");
                Matcher matcher = pattern.matcher(userAgent);
                if (matcher.find()) {
                    result.put("osVersion", matcher.group(1));
                }
            }
        } else if (userAgent.contains("Mac OS X")) {
            result.put("os", "macOS");
            Pattern pattern = Pattern.compile("Mac OS X ([0-9_]+)");
            Matcher matcher = pattern.matcher(userAgent);
            if (matcher.find()) {
                result.put("osVersion", matcher.group(1).replace("_", "."));
            }
        } else if (userAgent.contains("iPhone OS")) {
            result.put("os", "iOS");
            Pattern pattern = Pattern.compile("iPhone OS ([0-9_]+)");
            Matcher matcher = pattern.matcher(userAgent);
            if (matcher.find()) {
                result.put("osVersion", matcher.group(1).replace("_", "."));
            }
        } else if (userAgent.contains("Android")) {
            result.put("os", "Android");
            Pattern pattern = Pattern.compile("Android ([0-9.]+)");
            Matcher matcher = pattern.matcher(userAgent);
            if (matcher.find()) {
                result.put("osVersion", matcher.group(1));
            }
        } else if (userAgent.contains("Linux")) {
            result.put("os", "Linux");
            result.put("osVersion", "");
        } else {
            result.put("os", "其他");
            result.put("osVersion", "");
        }
    }
    
    /**
     * 解析设备信息
     */
    private static void parseDevice(String userAgent, Map<String, String> result) {
        if (userAgent.contains("Mobile") || userAgent.contains("Android") || userAgent.contains("iPhone")) {
            result.put("deviceType", "Mobile");
            
            // 解析设备品牌和型号
            if (userAgent.contains("iPhone")) {
                result.put("deviceBrand", "Apple");
                result.put("deviceModel", "iPhone");
            } else if (userAgent.contains("iPad")) {
                result.put("deviceType", "Tablet");
                result.put("deviceBrand", "Apple");
                result.put("deviceModel", "iPad");
            } else if (userAgent.contains("Android")) {
                // 尝试提取Android设备信息
                Pattern pattern = Pattern.compile("\\(Linux; Android [^;]+; ([^)]+)\\)");
                Matcher matcher = pattern.matcher(userAgent);
                if (matcher.find()) {
                    String deviceInfo = matcher.group(1);
                    result.put("deviceModel", deviceInfo);
                    
                    // 尝试识别品牌
                    if (deviceInfo.contains("Xiaomi") || deviceInfo.contains("MI ") || deviceInfo.contains("Redmi")) {
                        result.put("deviceBrand", "Xiaomi");
                    } else if (deviceInfo.contains("HUAWEI") || deviceInfo.contains("Honor")) {
                        result.put("deviceBrand", "Huawei");
                    } else if (deviceInfo.contains("OPPO")) {
                        result.put("deviceBrand", "OPPO");
                    } else if (deviceInfo.contains("vivo")) {
                        result.put("deviceBrand", "vivo");
                    } else if (deviceInfo.contains("Samsung")) {
                        result.put("deviceBrand", "Samsung");
                    } else {
                        result.put("deviceBrand", "Android");
                    }
                } else {
                    result.put("deviceBrand", "Android");
                    result.put("deviceModel", "");
                }
            } else {
                result.put("deviceBrand", "");
                result.put("deviceModel", "");
            }
        } else if (userAgent.contains("Tablet") || userAgent.contains("iPad")) {
            result.put("deviceType", "Tablet");
            if (userAgent.contains("iPad")) {
                result.put("deviceBrand", "Apple");
                result.put("deviceModel", "iPad");
            } else {
                result.put("deviceBrand", "");
                result.put("deviceModel", "");
            }
        } else {
            result.put("deviceType", "Desktop");
            result.put("deviceBrand", "");
            result.put("deviceModel", "");
        }
    }
}


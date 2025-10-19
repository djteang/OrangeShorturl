package com.shorturl;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 短链接系统启动类
 */
@SpringBootApplication
@MapperScan("com.shorturl.mapper")
public class ShortUrlApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShortUrlApplication.class, args);
        printStartupBanner();
    }

    /**
     * 打印启动横幅
     */
    private static void printStartupBanner() {
        String orange = "\033[38;5;214m"; // 橙色
        String green = "\033[32m";        // 绿色
        String blue = "\033[34m";         // 蓝色
        String reset = "\033[0m";         // 重置颜色
        String bold = "\033[1m";          // 加粗

        System.out.println("\n" + orange + bold + 
            "╔═══════════════════════════════════════════════════════════════╗\n" +
            "║                                                               ║\n" +
            "║     ____                                                      ║\n" +
            "║    / __ \\_________ _____  ____ ____                          ║\n" +
            "║   / / / / ___/ __ `/ __ \\/ __ `/ _ \\                         ║\n" +
            "║  / /_/ / /  / /_/ / / / / /_/ /  __/                         ║\n" +
            "║  \\____/_/   \\__,_/_/ /_/\\__, /\\___/                          ║\n" +
            "║                        /____/                                ║\n" +
            "║   _____ __               __  __  __      __                  ║\n" +
            "║  / ___// /_  ____  _____/ /_/ / / /___  / /                  ║\n" +
            "║  \\__ \\/ __ \\/ __ \\/ ___/ __/ / / / __ \\/ /                   ║\n" +
            "║ ___/ / / / / /_/ / /  / /_/ /_/ / /_/ / /                    ║\n" +
            "║/____/_/ /_/\\____/_/   \\__/\\____/\\____/_/                     ║\n" +
            "║                                                               ║\n" +
            "║               " + reset + "🍊 简洁、高效、易用的短链接系统 🍊" + orange + "                ║\n" +
            "║                                                               ║\n" +
            "╚═══════════════════════════════════════════════════════════════╝" + reset);
        
        System.out.println("\n" + green + "✨ Orange ShortUrl 启动成功！" + reset);
        System.out.println(blue + "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━" + reset);
        System.out.println(bold + "  🔗 访问地址:     " + reset + "http://localhost:9003");
        System.out.println(bold + "  🏠 前端地址:     " + reset + "http://localhost:5173");
        System.out.println(bold + "  🍊 项目版本:     " + reset + "v1.0.0");
        System.out.println(bold + "  ⚡ Spring Boot:  " + reset + "v2.7.18");
        System.out.println(blue + "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━" + reset);
        System.out.println(green + "🎉 系统已就绪，开始享受短链接服务吧！" + reset + "\n");
    }
}


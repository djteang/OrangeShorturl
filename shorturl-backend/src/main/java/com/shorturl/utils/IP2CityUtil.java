package com.shorturl.utils;

import lombok.extern.slf4j.Slf4j;
import org.lionsoul.ip2region.DataBlock;
import org.lionsoul.ip2region.DbConfig;
import org.lionsoul.ip2region.DbSearcher;
import org.springframework.core.io.ClassPathResource;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 根据ip获取归属地
 * @Author: orange
 * @Date: 2025/08/05/15:51
 */
@Slf4j
public class IP2CityUtil {

    private static final String dbName = "ip2region.db";

    private static final String regex = "\\|";

    //中划线
    private static final String dash = "-";

    private static final Integer zero = 0;

    private static final Integer two = 2;

    private static final Integer three = 3;

    private static final Integer four = 4;

    public static byte[] toByteArray(InputStream input) throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        byte[] buffer = new byte[4096];
        int n;
        while (-1 != (n = input.read(buffer))) {
            output.write(buffer, 0, n);
        }
        return output.toByteArray();
    }

    public static String getCityInfo(String ip) {
        try {
            ClassPathResource classPathResource = new ClassPathResource(dbName);
            InputStream inputStream = classPathResource.getInputStream();
            DbConfig config = new DbConfig();
            DbSearcher searcher = new DbSearcher(config, toByteArray(inputStream));
            DataBlock dataBlock = searcher.memorySearch(ip);
            String[] splitCity = dataBlock.getRegion().split(regex);
            String city = splitCity[zero] + dash + splitCity[two] + dash + splitCity[three] + dash + splitCity[four];
            return city;
        } catch (Exception e) {
            log.error("getCityInfo error.", e);
        }
        return null;
    }

    public static void main(String[] args) {
        String city = getCityInfo("85.234.83.131");
        String[] split = city.split("\\-");
        System.out.println(String.format("{%s}.{%s}.{%s}.{%s}", split[0], split[1], split[2], split[3]));
    }
}

package com.xiaoma.im.utils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * @Author Xiaoma
 * @Date 2021/2/7 0007 11:59
 * @Email 1468835254@qq.com
 */
public class DateUtils {
    
     // 获取时间戳
    public static Long getTimeStamp() {
        return LocalDateTime.now().toInstant(ZoneOffset.of("+8")).toEpochMilli();
    }

    // 格式化时间
    public static String stringToLocalDateTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return LocalDateTime.now(ZoneOffset.of("+8")).format(formatter);
    }

    // 格式化时间
    public static LocalDateTime localDateTimeToString(String dateTimeStr) {
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return LocalDateTime.parse(dateTimeStr, df);
    }

    // 将java.util.Date 转换为java8 的java.time.LocalDateTime,默认时区为东8区
    public static LocalDateTime dateConvertToLocalDateTime() {
        return new Date().toInstant().atOffset(ZoneOffset.of("+8")).toLocalDateTime();
    }


    // 将java8 的 java.time.LocalDateTime 转换为 java.util.Date，默认时区为东8区
    public static Date localDateTimeConvertToDate() {
        ZoneId zoneId = ZoneId.systemDefault();
        LocalDateTime localDateTime = LocalDateTime.now();
        ZonedDateTime zdt = localDateTime.atZone(zoneId);
        return Date.from(zdt.toInstant());
    }
}

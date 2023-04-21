package com.widus.springbootauth.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Created by Sshs0702 on 2023. 4. 21.
 *
 * 날짜 포맷 변경을 위한 Util
 */
public class CustomDateUtil {

    public static String toStringFormat(LocalDateTime localDateTime) {
        return localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

}

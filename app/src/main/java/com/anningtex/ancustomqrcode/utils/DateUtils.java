package com.anningtex.ancustomqrcode.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Author Song
 * @Desc:Date
 */
public class DateUtils {

    public static String getDate(Date date) {
        if (date == null) {
            date = new Date();
        }
        return new SimpleDateFormat("yyyy-MM-dd").format(date);
    }
}

package com.anningtex.ancustomqrcode.utils;

import android.annotation.SuppressLint;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Author Song
 * @Desc:Date
 */
public class DateUtils {
    /**
     * 时间转换
     */
    public static String getDate(Date date) {
        if (date == null) {
            date = new Date();
        }
        return new SimpleDateFormat("yyyy-MM-dd").format(date);
    }

    /**
     * 获取当天的日期
     */
    public static String today() {
        return new SimpleDateFormat("yyyy-MM-dd").format(new Date());
    }

    /**
     * 将时间转换为时间戳
     */
    @SuppressLint("SimpleDateFormat")
    public static String dateToStamp(String s) throws ParseException {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = simpleDateFormat.parse(s);
        long ts = date.getTime();
        res = String.valueOf(ts);
        return res;
    }
}

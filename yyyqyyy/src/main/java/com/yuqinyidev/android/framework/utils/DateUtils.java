package com.yuqinyidev.android.framework.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by RDX64 on 2017/6/30.
 */

public class DateUtils {
    private static final String DEFAULT_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private static SimpleDateFormat sdf;

    private static SimpleDateFormat getSimpleDateFormat() {
        return getSimpleDateFormat(DEFAULT_FORMAT);
    }

    private static SimpleDateFormat getSimpleDateFormat(String format) {
        sdf = new SimpleDateFormat(format, Locale.CHINA);
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+:08:00"));
        return sdf;
    }

    public static String formatMills(long timestamp, String format) {
        getSimpleDateFormat(format);
        Date dt = new Date(timestamp);
        return sdf.format(dt);
    }

    public static String formatMills(long timestamp) {
        return formatMills(timestamp, DEFAULT_FORMAT);
    }

    public static int date2Int() {
        return date2Int(new Date());
    }

    public static int date2Int(Date date) {
        return Integer.parseInt(new SimpleDateFormat("yyyyMMdd", Locale.CHINA).format(date));
    }

    public static String dateToString(Date _date, String _format) {
        return new SimpleDateFormat(_format, Locale.getDefault()).format(_date);
    }

}

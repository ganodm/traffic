package com.brkc.common.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Administrator on 16-4-18.
 */
public class DateUtil {
    public static Calendar createCanlendar(){
        return Calendar.getInstance();
    }

    public static int getYear(Calendar calendar){
        return calendar.get(Calendar.YEAR);
    }

    public static int getMonthOfYear(Calendar calendar){
        return calendar.get(Calendar.MONTH) + 1;
    }

    public static int getDayOfMonth(Calendar calendar){
        return calendar.get(Calendar.DATE);
    }

    /**
     * return 2016-04-18
     * @param calendar
     * @return
     */
    public static String getDate(Calendar calendar){
        return getDate(calendar, "-");
    }

    public static String getDate(Calendar calendar, String sep) {
        return DateUtil.getYear(calendar) + sep +
                DateUtil.getMonthOfYear(calendar) + sep +
                DateUtil.getDayOfMonth(calendar);
    }

    /**
     * 默认日期格式是YYYY-MM-DD
     * @param dateStr
     * @return
     */
    public static Calendar parseDate(String dateStr){
        return parseDate(dateStr, "yyyy-MM-dd");
    }

    public static Calendar parseDate(String dateStr, String formatStr){
        SimpleDateFormat sdf = new SimpleDateFormat(formatStr);
        try {
            java.util.Date date  = sdf.parse(dateStr);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            return calendar;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 默认日期格式是HH:mm
     * @param dateStr
     * @return
     */
    public static Calendar parseTime(String dateStr){
        return parseTime(dateStr, "HH:mm");
    }

    /**
     *
     * @param timeStr
     * @param formatStr HH:mm:ss
     * @return
     */
    public static Calendar parseTime(String timeStr, String formatStr){
        SimpleDateFormat sdf = new SimpleDateFormat(formatStr);
        try {
            java.util.Date date  = sdf.parse(timeStr);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            return calendar;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Calendar parseDateTime(String dateStr){
        return parseDateTime(dateStr, "yyyy-MM-dd HH:mm:ss");
    }

    public static Calendar parseDateTime(String dateStr, String formatStr){
        SimpleDateFormat sdf = new SimpleDateFormat(formatStr);
        try {
            java.util.Date date  = sdf.parse(dateStr);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            return calendar;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}

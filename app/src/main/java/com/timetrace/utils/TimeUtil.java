package com.timetrace.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Atomu on 2014/11/30.
 */
public class TimeUtil {

    public static boolean sameDay(long current, long another) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date curDay = new Date(current);
        String strCur = format.format(curDay);
        Date anoDay = new Date(another);
        String strAno = format.format(anoDay);

        return strCur.equals(strAno);
    }

    public static long lastDayStart(long day) {
        return getDayStart(day) - 24 * 60 * 60 * 1000;
    }

    public static long nextDayStart(long day) {
        return getDayStart(day) + 24 * 60 * 60 * 1000;
    }

    public static long getDayStart(long time) {
        Date date = new Date(time);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String timeStr = format.format(date);
        try {
            date = format.parse(timeStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return date.getTime();
    }

    public static String getRelTimeStr(long time) {
        time /= 1000;
        long second = time % 60;
        time /= 60;
        long minute = time % 60;
        time /= 60;
        long hour = time;
        return hour + ":" + minute + ":" + second;
    }

    public static String getRelTimeStr1(long time) {
        time /= 1000;
        long second = time % 60;
        time /= 60;
        long minute = time % 60;
        time /= 60;
        long hour = time;
        return hour + " H " + minute + " M ";
    }

    public static String getAbsTimeStr(long time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd, HH:mm:ss");
        Date curDay = new Date(time);
        return format.format(curDay);
    }

}

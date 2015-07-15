package com.timetrace.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.timetrace.monitor.Monitor;

/**
 * Created by Atomu on 2014/11/22.
 */
public class DataUtil extends BaseUtil {
    private final static String FILE = "user";
    private static SharedPreferences spf;

    public static void init() {
        DataUtil.monitor = Monitor.getApplication();
        spf = monitor.getSharedPreferences(FILE, Context.MODE_PRIVATE);
    }

    public static DataUtil getInstance() {
        return new DataUtil();
    }

    public static SharedPreferences getSpf() {
        return spf;
    }

    public static String getString(String key, String defValue) {
        return spf.getString(key, defValue);
    }

    public static String getString(String key) {
        return spf.getString(key, "");
    }

    public static void putString(String key, String value) {
        spf.edit().putString(key, value).apply();
    }

    public static long getLong(String key, long defValue) {
        return spf.getLong(key, defValue);
    }

    public static long getLong(String key) {
        return spf.getLong(key, 0);
    }

    public static void putLong(String key, long value) {
        spf.edit().putLong(key, value);
    }

    public static void clear() {
        spf.edit().clear().apply();
    }
}

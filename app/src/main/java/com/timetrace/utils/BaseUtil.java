package com.timetrace.utils;

import com.timetrace.monitor.Monitor;

/**
 * Created by Atomu on 2014/11/22.
 */
public class BaseUtil {
    protected static Monitor monitor;
//    private static Resources res;

    public static void init() {
        BaseUtil.monitor = Monitor.getApplication();
//        res = monitor.getResources();
    }

    public static String getString(int resId) {
        return monitor.getString(resId);
    }

//    public static Resources getResources(){
//        return res;
//    }
}

package com.timetrace.utils;

import com.timetrace.monitor.Monitor;

/**
 * Created by Atomu on 2014/11/22.
 */
public class HttpUtil extends BaseUtil {

    public static void init() {
        HttpUtil.monitor = Monitor.getApplication();
    }
}

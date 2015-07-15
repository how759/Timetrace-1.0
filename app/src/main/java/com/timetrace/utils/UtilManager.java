package com.timetrace.utils;

/**
 * Created by Atomu on 2014/10/24.
 */
public class UtilManager {
    public static void init() {
        DataUtil.init();
        DatabaseUtil.init();
        HttpUtil.init();
        ImageUtil.init();
        ViewUtil.init();
    }

}

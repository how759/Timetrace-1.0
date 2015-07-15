package com.timetrace.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.telephony.TelephonyManager;

import com.timetrace.monitor.Monitor;

import java.io.File;

/**
 * Created by Atomu on 2014/11/22.
 * 获取应用信息
 */
public class AppUtil extends BaseUtil {
    public static void init() {
        AppUtil.monitor = Monitor.getApplication();
    }

    public static String getCacheDir() {
        try {
            File cacheDir = monitor.getExternalCacheDir();
            if (cacheDir != null) {
                return cacheDir.getPath();
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return monitor.getCacheDir().getPath();
    }

    public static String getDeviceId() {
        TelephonyManager tm = (TelephonyManager) monitor.getSystemService(Context.TELEPHONY_SERVICE);
        return tm.getDeviceId();
    }

    public static String getVersionName() {
        String versionName = "";
        PackageManager pm = monitor.getPackageManager();
        try {
            PackageInfo info = pm.getPackageInfo(monitor.getPackageName(), 0);
            versionName = info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionName;
    }

}

package com.timetrace.monitor;

import android.app.Application;

import com.timetrace.objects.ApplicationInfo;
import com.timetrace.objects.MotionInfo;
import com.timetrace.objects.ProcessInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by Atomu on 2014/11/26.
 * 保存全局变量, 提供context
 */
public class Monitor extends Application {
    private static Application application;
    public MotionInfo motionInfo = new MotionInfo();
    public ConcurrentLinkedQueue<MotionInfo> motionInfoQueue = new ConcurrentLinkedQueue<MotionInfo>();

    public ConcurrentLinkedQueue<ProcessInfo[]> processInfoQueue = new ConcurrentLinkedQueue<ProcessInfo[]>();
    public ConcurrentLinkedQueue<long[]> timeFlags = new ConcurrentLinkedQueue<long[]>();

    public List<ProcessInfo> processInfoList = new ArrayList<ProcessInfo>();

    public Map<String, ApplicationInfo> appInfoMap = new HashMap<String, ApplicationInfo>();
    public long appMapLastRef = -1;

    public static void init(Application application) {
        Monitor.application = application;
    }

    public static Monitor getApplication() {
        return (Monitor) Monitor.application;
    }

}

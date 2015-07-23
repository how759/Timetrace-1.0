package com.timetrace.monitor;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import com.timetrace.objects.ApplicationInfo;
import com.timetrace.objects.ProcessInfo;
import com.timetrace.utils.TimeUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Atomu on 2014/11/26.
 * 获取进程信息等, 包含四个线程, 分别进行process的检测和统计
 */
public class AppInfoProvider {
    private static AppInfoProvider instance;
    private final String TAG = AppInfoProvider.class.getSimpleName();
    // 频率
    private final int REF_PRO_FREQ = 1000 * 10;
    private final int REF_APP_MAP_FREQ = 1000 * 60;
    private final int START_WAITE = 1000 * 3;
    private final ActivityManager activityManager;
    private final PackageManager packageManager;
    private Monitor monitor = Monitor.getApplication();
    private DetectProcessThread detectProcessThread;
    private WriteProcessThread writeProcessThread;
    private StatisticAppThread statisticAppThread;
    private RefreshAppMapThread refreshAppMapThread;
    /**
     * -1 represents not start yet, until this value becomes positive, that means the thread has started,
     * and then timeSlice should be positive, which should be calculated by System.currentTime - start
     * and of course start value should change precisely
     */
    private long startTime = -1;
    private long timeSlice = 0;

    private AppInfoProvider() {
        activityManager = (ActivityManager) monitor.getSystemService(Context.ACTIVITY_SERVICE);
        packageManager = monitor.getPackageManager();
    }

    public static AppInfoProvider getInstance() {
        if (instance == null) {
            instance = new AppInfoProvider();
        }
        return instance;
    }

    /**
     * 将一个process转化成对应的app加入到某个队列
     *
     * @param info                进程信息
     * @param applicationInfoList 需要加入的列表
     */
    public void addApp(ProcessInfo info, List<ApplicationInfo> applicationInfoList) {
        ApplicationInfo applicationInfo = new ApplicationInfo();
        applicationInfo.setImportance(info.getImportance());
        applicationInfo.setPackName(info.getPacName());

        if (info.getPacName().isEmpty())
            //TODO: 已经按功能过滤过了所以就不再按照importance过滤了
//        || info.getImportance() > ActivityManager.RunningAppProcessInfo.IMPORTANCE_BACKGROUND)
            return;

        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(info.getPacName(), 0);
            applicationInfo.setIcon(packageInfo.applicationInfo.loadIcon(packageManager));

            String appName = packageInfo.applicationInfo.loadLabel(packageManager).toString();
            if (appName.isEmpty())
                appName = info.getPacName();
            applicationInfo.setAppName(appName);

            boolean isUserApp = true;
            int flags = packageInfo.applicationInfo.flags;
            if ((flags & android.content.pm.ApplicationInfo.FLAG_SYSTEM) != 0) {
                isUserApp = false;
            } else if ((flags & android.content.pm.ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0) {
                isUserApp = false;
            }

            applicationInfo.setUserProcess(isUserApp);

            long active;
            if (monitor.appInfoMap.containsKey(info.getPacName())) {
                active = monitor.appInfoMap.get(info.getPacName()).getActiveTime();
            } else {
                active = 0;
            }
            applicationInfo.setActiveTime(active);

            /**
             * 筛选功能性系统app
             */
            if (applicationInfo.isUserProcess()) {
                applicationInfoList.add(applicationInfo);
            } else if (info.getPacName().contains("browser") || info.getPacName().contains("music") ||
                    info.getPacName().contains("video") || info.getPacName().contains("player") || info.getPacName().contains("news")) {
                applicationInfoList.add(applicationInfo);
            }
        } catch (PackageManager.NameNotFoundException e) {
//            e.printStackTrace();
        }
    }

    public List<ApplicationInfo> getInstalledApps() {
        List<PackageInfo> packageInfoList = packageManager.getInstalledPackages(0);
        List<ProcessInfo> packageInstalled = new ArrayList<ProcessInfo>();

        for (PackageInfo info : packageInfoList) {
            ProcessInfo processInfo = new ProcessInfo();
            processInfo.setPackName(info.packageName);
            processInfo.setImportance(ActivityManager.RunningAppProcessInfo.IMPORTANCE_EMPTY);

            packageInstalled.add(processInfo);
        }

        List<ApplicationInfo> applicationInstalled = new ArrayList<ApplicationInfo>();
        Log.d(TAG, "installed apps are " + packageInstalled.size());
        for (ProcessInfo info : packageInstalled) {
            addApp(info, applicationInstalled);
        }

        Log.d(TAG, "installed apps are " + applicationInstalled.size());

        return applicationInstalled;
    }

    public List<ProcessInfo> getRunningProcessList() {
        List<ActivityManager.RunningAppProcessInfo> runningAppProcessInfos = activityManager.getRunningAppProcesses();

        monitor.processInfoList.clear();
        if (runningAppProcessInfos == null) {
            return monitor.processInfoList;
        }
        Log.d(TAG, "running process are " + runningAppProcessInfos.size());

        for (ActivityManager.RunningAppProcessInfo info : runningAppProcessInfos) {
            ProcessInfo processInfo = new ProcessInfo();
            processInfo.setImportance(info.importance);
            processInfo.setPackName(info.processName);

            if (info.processName == null || info.processName.isEmpty())
                continue;

            monitor.processInfoList.add(processInfo);
        }

        return monitor.processInfoList;
    }

    public List<ApplicationInfo> getRunningAppList() {
        while (monitor.processInfoList.isEmpty()) {
            try {
                Thread.sleep(1000 * 3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        List<ApplicationInfo> applicationInfoList = new ArrayList<ApplicationInfo>();
        for (ProcessInfo info : monitor.processInfoList) {
            addApp(info, applicationInfoList);
        }

        Log.d(TAG, "running apps are hehe " + applicationInfoList.size());

        return applicationInfoList;
    }

    public void start() {
        Log.d(TAG, "start processthread");
        if (detectProcessThread == null) {
            Log.d(TAG, "start detect process thread");
            detectProcessThread = new DetectProcessThread();
            detectProcessThread.start();
        }

        if (writeProcessThread == null) {
            writeProcessThread = new WriteProcessThread();
            writeProcessThread.start();
        }

        if (statisticAppThread == null) {
            statisticAppThread = new StatisticAppThread();
            statisticAppThread.start();
        }

        if (refreshAppMapThread == null) {
            refreshAppMapThread = new RefreshAppMapThread();
            refreshAppMapThread.start();
        }
    }

    //TODO: 允许打断?
    public void interrupt() {

    }

    private class DetectProcessThread extends Thread {
        private ProcessInfo[] processInfoArray;
        private long[] timeFlag;

        @Override
        public void run() {
            super.run();
            Log.d(TAG, "detect process thread");
            startTime = System.currentTimeMillis();
            timeSlice = 0;
            while (!isInterrupted()) {
                try {
                    sleep(REF_PRO_FREQ);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                processInfoArray = new ProcessInfo[getRunningProcessList().size()];
                Log.d(TAG, "num of process is " + getRunningProcessList().size());
                monitor.processInfoList.toArray(processInfoArray);
                monitor.processInfoQueue.offer(processInfoArray);

                timeFlag = new long[2];
                timeFlag[0] = startTime;
                timeFlag[1] = timeSlice;
                monitor.timeFlags.offer(timeFlag);

                long current = System.currentTimeMillis();
                timeSlice = current - startTime;
                startTime = current;
            }

        }
    }

    private class WriteProcessThread extends Thread {
        @Override
        public void run() {
            super.run();

            while (!isInterrupted()) {

                try {
                    sleep(REF_PRO_FREQ);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if (!monitor.processInfoQueue.isEmpty()) {
                    long[] timeFlag = monitor.timeFlags.poll();
                    AppDBHelper.writeProcess(monitor.processInfoQueue.poll(), timeFlag[0], timeFlag[1]);
//                    Log.d(TAG, "--------------------------------------write once-----------------------------------------");
                }
            }
        }
    }

    public class StatisticAppThread extends Thread {
        @Override
        public void run() {
            super.run();

            // 刚启动的时候先不要统计
            try {
                sleep(START_WAITE);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            Log.d(TAG, "start write here");
            AppDBHelper.writeProcessStatistic();
        }
    }

    public class RefreshAppMapThread extends Thread {
        @Override
        public void run() {
            // 刚启动的时候先不要统计
            try {
                sleep(START_WAITE);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            while (!isInterrupted()) {
                long current = System.currentTimeMillis(), lastRefresh;
                if (monitor.appMapLastRef == -1) {
                    lastRefresh = AppDBHelper.readProcess(monitor.appInfoMap, TimeUtil.getDayStart(current), current);
                    if (lastRefresh != -1)
                        monitor.appMapLastRef = lastRefresh;
                } else {
                    lastRefresh = AppDBHelper.readProcess(monitor.appInfoMap, monitor.appMapLastRef, current);
                    if (lastRefresh != -1)
                        monitor.appMapLastRef = lastRefresh;
                }

                try {
                    sleep(REF_APP_MAP_FREQ);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

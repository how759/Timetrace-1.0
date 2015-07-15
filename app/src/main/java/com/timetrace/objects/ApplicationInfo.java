package com.timetrace.objects;

import android.graphics.drawable.Drawable;

/**
 * Created by Atomu on 2014/11/26.
 */
public class ApplicationInfo extends ProcessInfo {

    /**
     * less important, just to present
     */
    private Drawable icon;
    private String appName;
    private long activeTime;
    private boolean userProcess;

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public long getActiveTime() {
        return activeTime;
    }

    public void setActiveTime(long activeTime) {
        this.activeTime = activeTime;
    }

    public boolean isUserProcess() {
        return userProcess;
    }

    public void setUserProcess(boolean userProcess) {
        this.userProcess = userProcess;
    }
}

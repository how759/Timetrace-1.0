package com.timetrace.objects;

/**
 * Created by Atomu on 2014/12/2.
 */
public class MotionInfo {
    private long time;
    private float[] acc;
    private float[] pre;
    private float[] gyr;
    private double[] gps;

    public float[] getAcc() {
        return acc;
    }

    public void setAcc(float[] acc) {
        this.acc = acc;
    }

    public float[] getPre() {
        return pre;
    }

    public void setPre(float[] pre) {
        this.pre = pre;
    }

    public float[] getGyr() {
        return gyr;
    }

    public void setGyr(float[] gyr) {
        this.gyr = gyr;
    }

    public double[] getGps() {
        return gps;
    }

    public void setGps(double[] gps) {
        this.gps = gps;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    @Override
    public MotionInfo clone() {
        MotionInfo copy = new MotionInfo();
        if (acc != null)
            copy.setAcc(this.acc.clone());
        if (gps != null)
            copy.setGps(this.gps.clone());
        if (gyr != null)
            copy.setGyr(this.gyr.clone());
        if (pre != null)
            copy.setPre(this.pre.clone());

        return copy;
    }
}

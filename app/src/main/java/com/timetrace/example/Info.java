package com.timetrace.example;

import java.util.concurrent.ConcurrentLinkedQueue;

public class Info {
    public static final int DOWNELEVATOR = 10;
    public static final int DOWNSTAIR = 8;
    public static final int INPOCKET = 2;
    public static final int JUMPING = 4;
    public static final int LYING = 3;
    public static final int RUNNING = 6;
    public static final int SITTING = 11;
    public static final int STANDING = 1;
    public static final int TV = 12;
    public static final int UPELEVATOR = 9;
    public static final int UPSTAIR = 7;
    public static final int WALKING = 5;
    public static ConcurrentLinkedQueue<String> altitude;
    public static ConcurrentLinkedQueue<String[]> raw_data = new ConcurrentLinkedQueue();

    static {
        altitude = new ConcurrentLinkedQueue();
    }
}

/* Location:           C:\Users\Atomu\Desktop\MonitoringActivity-dex2jar.jar
 * Qualified Name:     com.example.monitoringactivity.Info
 * JD-Core Version:    0.6.0
 */
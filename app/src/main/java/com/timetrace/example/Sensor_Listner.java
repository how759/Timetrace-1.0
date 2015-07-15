package com.timetrace.example;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class Sensor_Listner implements SensorEventListener {
    private static final String ratData = "raw";
    private static String[] delma = {"E", "N", "D"};
    private long current;
    private long current2;
    private float currentBarometerValue;
    private Write_Thread highpass_write;
    private Write_Thread lowpass_write;
    private String phone_state = null;
    private Write_Thread raw_write;
    private long startCurrent;
    private long startCurrent2;
    private String state;
    private String strCutTime;
    private String sub_name = "SubjectA";

    public Sensor_Listner(String paramString1, String paramString2, String paramString3, String paramString4) {
        this.strCutTime = paramString1;
        this.state = paramString2;
        this.phone_state = paramString3;
        this.sub_name = paramString4;
        this.startCurrent = System.currentTimeMillis();
        this.startCurrent2 = System.currentTimeMillis();
        init();
    }

    public void init() {
        this.raw_write = new Write_Thread(this.strCutTime, this.state, "raw", this.phone_state, this.sub_name);
        this.raw_write.start();
    }

    public void onAccuracyChanged(Sensor paramSensor, int paramInt) {
    }

    public void onSensorChanged(SensorEvent paramSensorEvent) {
        switch (paramSensorEvent.sensor.getType()) {
            default:
                return;
            case 1:
                this.current = System.currentTimeMillis();
                float[] arrayOfFloat = paramSensorEvent.values;
                String[] arrayOfString = new String[arrayOfFloat.length];
                for (int i = 0; ; i++) {
                    if (i >= arrayOfFloat.length) {
                        Info.raw_data.offer(arrayOfString);
                        return;
                    }
                    arrayOfString[i] = String.valueOf(arrayOfFloat[i]);
                    arrayOfString[i] = (arrayOfString[i] + " ");
                }
            case 6:
        }
        this.current2 = System.currentTimeMillis();
        this.currentBarometerValue = paramSensorEvent.values[0];
        SensorManager.getAltitude(1013.25F, this.currentBarometerValue);
        Info.altitude.offer(String.valueOf(this.currentBarometerValue));
    }

    public void stop() {
        this.raw_write.stopThread();
        this.raw_write = null;
    }
}

/* Location:           C:\Users\Atomu\Desktop\MonitoringActivity-dex2jar.jar
 * Qualified Name:     com.example.monitoringactivity.Sensor_Listner
 * JD-Core Version:    0.6.0
 */
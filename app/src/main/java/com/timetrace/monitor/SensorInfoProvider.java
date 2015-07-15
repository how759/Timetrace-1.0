package com.timetrace.monitor;

import android.app.KeyguardManager;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.timetrace.objects.MotionInfo;

/**
 * Created by Atomu on 2014/11/26.
 * TODO: 锁屏时要不要保持传感器active
 */
public class SensorInfoProvider implements SensorEventListener, AMapLocationListener {
    private final String TAG = "sensorUtil";

    private final SensorManager sensorManager;
    private final Sensor accelerometer;
    private final Sensor pressure;
    private final Sensor gyroscope;

    // 100 Hz, 20 Hz, 100 Hz
    private final int ACC_FREQ = 100 * 1000;
    private final int PRE_FREQ = 500 * 1000;
    private final int GYR_FREQ = 100 * 1000;
    private final int GPS_FREQ = 500;
    private final int REF_FREQ = 100;
    private final KeyguardManager keyguardManager;
    private LocationManagerProxy locationManagerProxy;
    private long curTime = System.currentTimeMillis();
    private boolean hasPreSensor = true;
    private DetectThread detectThread;
    private WriteThread writeThread;
    private WriteFeatureThread writeFeatureThread;
    private Monitor monitor = Monitor.getApplication();

    public SensorInfoProvider() {
        sensorManager = (SensorManager) monitor.getSystemService(Context.SENSOR_SERVICE);
        keyguardManager = (KeyguardManager) monitor.getSystemService(Context.KEYGUARD_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        pressure = sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE);
        if (pressure == null) {
            Toast.makeText(monitor, "木有气压传感器", Toast.LENGTH_LONG).show();
            hasPreSensor = false;
        }
        gyroscope = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);

        if (detectThread == null) {
            detectThread = new DetectThread();
            detectThread.start();
        }
        if (writeThread == null) {
            writeThread = new WriteThread();
            writeThread.start();
        }
        if (writeFeatureThread == null) {
            writeFeatureThread = new WriteFeatureThread();
            writeFeatureThread.start();
        }
    }

    public void registerListener() {
        sensorManager.registerListener(this, accelerometer, ACC_FREQ);
        sensorManager.registerListener(this, pressure, PRE_FREQ);
        sensorManager.registerListener(this, gyroscope, GYR_FREQ);

        /**
         * 高德定位初始化
         */
        locationManagerProxy = LocationManagerProxy.getInstance(monitor);
        //此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
        //注意设置合适的定位时间的间隔，并且在合适时间调用removeUpdates()方法来取消定位请求
        //在定位结束后，在合适的生命周期调用destroy()方法
        //其中如果间隔时间为-1，则定位只定一次
        locationManagerProxy.requestLocationData(LocationProviderProxy.AMapNetwork, GPS_FREQ, 15, this);
        locationManagerProxy.setGpsEnable(false);
    }

    /**
     * 高德定位回收, "建议在 onPause 中调用"
     */
    public void removeAMapListener() {
        if (locationManagerProxy != null) {
            locationManagerProxy.removeUpdates(this);
            locationManagerProxy.destroy();
        }
        locationManagerProxy = null;
    }

    public void unregisterListener() {
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float[] values = event.values;
        int length = values.length;
        long current = System.currentTimeMillis();

        switch (event.sensor.getType()) {
            case Sensor.TYPE_ACCELEROMETER:
//                Log.d(TAG, "ACC " + Arrays.toString(values));
                float[] acc = new float[length];
                System.arraycopy(values, 0, acc, 0, length);
                monitor.motionInfo.setAcc(acc);
                monitor.motionInfo.setTime(current);
                break;
            case Sensor.TYPE_PRESSURE:
//                Log.d(TAG, "PRE " + Arrays.toString(values));
                float[] pre = new float[length];
                System.arraycopy(values, 0, pre, 0, length);
                monitor.motionInfo.setPre(pre);
                monitor.motionInfo.setTime(current);
                break;
            case Sensor.TYPE_GYROSCOPE:
//                Log.d(TAG, "GYR " + Arrays.toString(values));
                float[] gyr = new float[length];
                System.arraycopy(values, 0, gyr, 0, length);
                monitor.motionInfo.setGyr(gyr);
                monitor.motionInfo.setTime(current);
                break;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation != null && aMapLocation.getAMapException().getErrorCode() == 0) {
            //获取位置信息
            double geoLat = aMapLocation.getLatitude();
            double geoLng = aMapLocation.getLongitude();
            float speed = aMapLocation.getSpeed();
            float altitude = (float) aMapLocation.getAltitude();
            Log.d(TAG, "time " + (System.currentTimeMillis() - curTime) + "gps height " + altitude
                    + " lat " + geoLat + " lng " + geoLng + " speed " + speed);
            curTime = System.currentTimeMillis();

            double[] gps = new double[3];
            gps[0] = geoLat;
            gps[1] = geoLng;
            gps[2] = speed;
            monitor.motionInfo.setGps(gps);

            if (!hasPreSensor) {
                monitor.motionInfo.setPre(new float[]{altitude});
            }
            monitor.motionInfo.setTime(System.currentTimeMillis());
        }
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    private class DetectThread extends Thread {
        @Override
        public void run() {
            while (!isInterrupted()) {
                boolean locked = keyguardManager.inKeyguardRestrictedInputMode();

                if (locked) {
                    try {
                        sleep(10 * 1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    Log.d(TAG, "add is locked");

                } else {
                    try {
                        sleep(REF_FREQ);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

//                    Log.d(TAG, "add time " + TimeUtil.getAbsTimeStr(monitor.motionInfo.getTime()));
//                    Log.d(TAG, "add acc " + Arrays.toString(monitor.motionInfo.getAcc()));
//                    Log.d(TAG, "add pre " + Arrays.toString(monitor.motionInfo.getPre()));
//                    Log.d(TAG, "add gyr " + Arrays.toString(monitor.motionInfo.getGyr()));
//                    Log.d(TAG, "add gps " + Arrays.toString(monitor.motionInfo.getGps()));

                    monitor.motionInfoQueue.offer(monitor.motionInfo);
                    monitor.motionInfo = monitor.motionInfo.clone();
                }
            }
        }
    }

    private class WriteThread extends Thread {
        @Override
        public void run() {
            try {
                sleep(3 * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            while (!isInterrupted()) {
                if (monitor.motionInfoQueue.isEmpty())
                    continue;

                MotionInfo motionInfo = monitor.motionInfoQueue.poll();
                MotionDBHelper.writeMotionInfo(motionInfo);
//                Log.d(TAG, "write once");

                try {
                    sleep(REF_FREQ);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private class WriteFeatureThread extends Thread {
        @Override
        public void run() {
            try {
                sleep(5 * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            while (!isInterrupted()) {
                MotionDBHelper.writeMotionFeature();

                try {
                    sleep(MotionDBHelper.WINDOW_TIME);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

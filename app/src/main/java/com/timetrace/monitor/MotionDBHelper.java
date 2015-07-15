package com.timetrace.monitor;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.timetrace.api.API_DB;
import com.timetrace.objects.MotionFeatureInfo;
import com.timetrace.objects.MotionInfo;
import com.timetrace.utils.BaseUtil;
import com.timetrace.utils.DatabaseUtil;
import com.timetrace.utils.TimeUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Atomu on 2014/12/2.
 * 封装了传感器, feature的数据库操作
 */
public class MotionDBHelper extends BaseUtil {
    public static final long WINDOW_TIME = 1000 * 60 * 2;
    public static final int WINDOW_SIZE = 250;

    private static final String TAG = MotionDBHelper.class.getSimpleName();
    private static SQLiteDatabase wdb = DatabaseUtil.getWritableDatabase();
    private static SQLiteDatabase rdb = DatabaseUtil.getReadableDatabase();

    public static void writeMotionInfo(MotionInfo motionInfo) {
        ContentValues values = new ContentValues();

        if (motionInfo.getTime() <= 0 || motionInfo.getAcc() == null || motionInfo.getGyr() == null || motionInfo.getPre() == null || motionInfo.getGps() == null)
            return;

        values.put(API_DB.COL_TIME, motionInfo.getTime());
        values.put(API_DB.COL_ACC_X, motionInfo.getAcc()[0]);
        values.put(API_DB.COL_ACC_Y, motionInfo.getAcc()[1]);
        values.put(API_DB.COL_ACC_Z, motionInfo.getAcc()[2]);
        values.put(API_DB.COL_PRE_HEIGHT, motionInfo.getPre()[0]);
        values.put(API_DB.COL_GYR_X, motionInfo.getGyr()[0]);
        values.put(API_DB.COL_GYR_Y, motionInfo.getGyr()[1]);
        values.put(API_DB.COL_GYR_Z, motionInfo.getGyr()[2]);
        values.put(API_DB.COL_GPS_LAT, motionInfo.getGps()[0]);
        values.put(API_DB.COL_GPS_LNG, motionInfo.getGps()[1]);
        values.put(API_DB.COL_GPS_SPEED, (float) motionInfo.getGps()[2]);

        wdb.insert(API_DB.TABLE_MOTION, null, values);
    }

    /**
     * gps 还有 gyr 先不统计, 暂时没用到
     */
    public static long readMotionInfo(List<MotionInfo> motionInfoList, long start, long end) {
        Cursor cursor = rdb.rawQuery("SELECT * FROM " + API_DB.TABLE_MOTION + " WHERE "
                + API_DB.COL_TIME + " >= ? AND " + API_DB.COL_TIME + " <?", new String[]{"" + start, "" + end});

//        Log.d(TAG, "check time " + TimeUtil.getAbsTimeStr(start) + " to " + TimeUtil.getAbsTimeStr(end));

        int indexTime = cursor.getColumnIndex(API_DB.COL_TIME);
        int indexAccX = cursor.getColumnIndex(API_DB.COL_ACC_X);
        int indexAccY = cursor.getColumnIndex(API_DB.COL_ACC_Y);
        int indexAccZ = cursor.getColumnIndex(API_DB.COL_ACC_Z);
        int indexGyrX = cursor.getColumnIndex(API_DB.COL_GYR_X);
        int indexGyrY = cursor.getColumnIndex(API_DB.COL_GYR_Y);
        int indexGyrZ = cursor.getColumnIndex(API_DB.COL_GYR_Z);
        int indexPre = cursor.getColumnIndex(API_DB.COL_PRE_HEIGHT);
        int indexGpsLat = cursor.getColumnIndex(API_DB.COL_GPS_LAT);
        int indexGpsLng = cursor.getColumnIndex(API_DB.COL_GPS_LNG);
        int indexSpeed = cursor.getColumnIndex(API_DB.COL_GPS_SPEED);
        long time = -1;
        while (cursor.moveToNext()) {
            MotionInfo motionInfo = new MotionInfo();
            time = cursor.getLong(indexTime);
            motionInfo.setTime(time);
            float[] acc = new float[3];
            acc[0] = cursor.getFloat(indexAccX);
            acc[1] = cursor.getFloat(indexAccY);
            acc[2] = cursor.getFloat(indexAccZ);
            motionInfo.setAcc(acc);
//            float[] gyr = new float[3];
//            gyr[0] = cursor.getFloat(indexGyrX);
//            gyr[1] = cursor.getFloat(indexGyrY);
//            gyr[2] = cursor.getFloat(indexGyrZ);
//            motionInfo.setGyr(gyr);
            float[] pre = new float[1];
            pre[0] = cursor.getFloat(indexPre);
            motionInfo.setPre(pre);
//            double[] gps = new double[3];
//            gps[0] = cursor.getDouble(indexGpsLat);
//            gps[1] = cursor.getDouble(indexGpsLng);
//            gps[2] = cursor.getFloat(indexSpeed);
//            motionInfo.setGps(gps);

//            Log.d(TAG, "read motion, time: " + TimeUtil.getAbsTimeStr(time) + ", acc: " + Arrays.toString(acc) + ", pre: " + Arrays.toString(pre));

            motionInfoList.add(motionInfo);
        }
        cursor.close();

        return time;
    }

    public static long getLastFeature() {
        Cursor cursor = rdb.rawQuery("SELECT " + API_DB.COL_TIME + " FROM " + API_DB.TABLE_MOTION_FEATURE, null);
        long lastFeature = -1;
        if (cursor.moveToLast()) {
            lastFeature = cursor.getLong(cursor.getColumnIndex(API_DB.COL_TIME));
        }
        cursor.close();
        return lastFeature;
    }

    public static long getLastRecord() {
        Cursor cursor = rdb.rawQuery("SELECT " + API_DB.COL_TIME + " FROM " + API_DB.TABLE_MOTION, null);
        long lastRecord = -1;
        if (cursor.moveToLast()) {
            lastRecord = cursor.getLong(cursor.getColumnIndex(API_DB.COL_TIME));
        }
        cursor.close();
        return lastRecord;
    }

    public static long getMotionTime(long day) {
        Cursor cursor = rdb.rawQuery("SELECT " + API_DB.COL_TIME + " FROM " + API_DB.TABLE_MOTION + " WHERE "
                        + API_DB.COL_TIME + " >= ? AND " + API_DB.COL_TIME + " < ?",
                new String[]{TimeUtil.getDayStart(day) + "", TimeUtil.nextDayStart(day) + ""});
        long motion = 0;
        int indexTime = cursor.getColumnIndex(API_DB.COL_TIME);
        if (cursor.moveToFirst()) {
            motion = cursor.getLong(indexTime);
        }
        if (cursor.moveToLast()) {
            motion = cursor.getLong(indexTime) - motion;
        } else {
            motion = 0;
        }
        cursor.close();
        return motion;
    }

    public static long getFirstRecord() {
        Cursor cursor = rdb.rawQuery("SELECT " + API_DB.COL_TIME + " FROM " + API_DB.TABLE_MOTION, null);
        long firstRecord = -1;
        if (cursor.moveToFirst()) {
            firstRecord = cursor.getLong(cursor.getColumnIndex(API_DB.COL_TIME));
        }
        cursor.close();
        return firstRecord;
    }

    public static long readMotionFeature(List<MotionFeatureInfo> featureInfoList, long start, long end) {
        Cursor cursor = rdb.rawQuery("SELECT * FROM " + API_DB.TABLE_MOTION_FEATURE + " WHERE "
                + API_DB.COL_TIME + " >= ? AND " + API_DB.COL_TIME + " < ?", new String[]{"" + start, "" + end});

        long time = -1;
        int indexTime = cursor.getColumnIndex(API_DB.COL_TIME);
        int indexAccMX = cursor.getColumnIndex(API_DB.COL_ACC_MEAN_X);
        int indexAccMY = cursor.getColumnIndex(API_DB.COL_ACC_MEAN_Y);
        int indexAccMZ = cursor.getColumnIndex(API_DB.COL_ACC_MEAN_Z);
        int indexAccSDX = cursor.getColumnIndex(API_DB.COL_ACC_SD_X);
        int indexAccSDY = cursor.getColumnIndex(API_DB.COL_ACC_SD_Y);
        int indexAccSDZ = cursor.getColumnIndex(API_DB.COL_ACC_SD_Z);
        int indexAccCXY = cursor.getColumnIndex(API_DB.COL_ACC_CORR_XY);
        int indexAccCYZ = cursor.getColumnIndex(API_DB.COL_ACC_CORR_YZ);
        int indexAccCXZ = cursor.getColumnIndex(API_DB.COL_ACC_CORR_XZ);
        int indexAlF = cursor.getColumnIndex(API_DB.COL_AL_FIRST);
        int indexAlM = cursor.getColumnIndex(API_DB.COL_AL_MIDDLE);
        int indexAlL = cursor.getColumnIndex(API_DB.COL_AL_LAST);
        int indexAccSmaX = cursor.getColumnIndex(API_DB.COL_ACC_SMA_X);
        int indexAccSmaY = cursor.getColumnIndex(API_DB.COL_ACC_SMA_Y);
        int indexAccSmaZ = cursor.getColumnIndex(API_DB.COL_ACC_SMA_Z);
        int indexCoefX = cursor.getColumnIndex(API_DB.COL_COEF_X);
        int indexCoefY = cursor.getColumnIndex(API_DB.COL_COEF_Y);
        int indexCoefZ = cursor.getColumnIndex(API_DB.COL_COEF_Z);
        while (cursor.moveToNext()) {
            MotionFeatureInfo featureInfo = new MotionFeatureInfo();
            float[] accMean = new float[3];
            accMean[0] = cursor.getFloat(indexAccMX);
            accMean[1] = cursor.getFloat(indexAccMY);
            accMean[2] = cursor.getFloat(indexAccMZ);
            featureInfo.setAccMean(accMean);
            float[] accSD = new float[3];
            accSD[0] = cursor.getFloat(indexAccSDX);
            accSD[1] = cursor.getFloat(indexAccSDY);
            accSD[2] = cursor.getFloat(indexAccSDZ);
            featureInfo.setAccSD(accSD);
            float[] accCorr = new float[3];
            accCorr[0] = cursor.getFloat(indexAccCXY);
            accCorr[1] = cursor.getFloat(indexAccCYZ);
            accCorr[2] = cursor.getFloat(indexAccCXZ);
            featureInfo.setAccCorr(accCorr);
            float[] accSma = new float[3];
            accSma[0] = cursor.getFloat(indexAccSmaX);
            accSma[1] = cursor.getFloat(indexAccSmaY);
            accSma[2] = cursor.getFloat(indexAccSmaZ);
            featureInfo.setAccSma(accSma);
            float[] al = new float[3];
            al[0] = cursor.getFloat(indexAlF);
            al[1] = cursor.getFloat(indexAlM);
            al[2] = cursor.getFloat(indexAlL);
            featureInfo.setAl(al);
            String coefXStr = cursor.getString(indexCoefX);
            String coefYStr = cursor.getString(indexCoefY);
            String coefZStr = cursor.getString(indexCoefZ);
            float[] coefX = new float[3];
            float[] coefY = new float[3];
            float[] coefZ = new float[3];
            int i = 0;
            for (String coef : coefXStr.split(",")) {
                coefX[i] = Float.parseFloat(coef);
                i++;
            }
            i = 0;
            for (String coef : coefYStr.split(",")) {
                coefY[i] = Float.parseFloat(coef);
                i++;
            }
            i = 0;
            for (String coef : coefZStr.split(",")) {
                coefZ[i] = Float.parseFloat(coef);
                i++;
            }
            featureInfo.setCoefX(coefX);
            featureInfo.setCoefY(coefY);
            featureInfo.setCoefZ(coefZ);

            featureInfoList.add(featureInfo);
        }
        cursor.close();

        return time;
    }

    /**
     * 从最后一次统计的时间节点开始统计到当前时刻, 也就是说这个时间段之内是没有统计过的, 只需要从Process表来统计就行了
     */
    public synchronized static void writeMotionFeature() {
        long lastFeature = getLastFeature();
        if (lastFeature == -1) {
            Log.d(TAG, "last record is " + TimeUtil.getAbsTimeStr(getFirstRecord()));
            long firstRecord = getFirstRecord();
            if (firstRecord == -1) {
                return;
            }
            lastFeature = firstRecord;
        }
        long current = System.currentTimeMillis();
        Log.d(TAG, "start is " + TimeUtil.getAbsTimeStr(lastFeature));
        Log.d(TAG, "current is " + TimeUtil.getAbsTimeStr(current));

        /**
         * 如果不满一次 window 则不统计
         */
        for (long time = lastFeature; time + WINDOW_TIME <= current; time += WINDOW_TIME) {
            try {
                Thread.sleep(3 * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            writeMotionFeatureSingleWindow(time);
        }
    }

    private synchronized static void writeMotionFeatureSingleWindow(long start) {
        List<MotionInfo> motionInfoList = new ArrayList<MotionInfo>();
        long end = readMotionInfo(motionInfoList, start, start + WINDOW_TIME);
        if (end == -1)
            return;

        int size = motionInfoList.size();
//        Log.d(TAG, "single widow size is " + size + " start from " + TimeUtil.getAbsTimeStr(start) + " to " + TimeUtil.getAbsTimeStr(end));

        if (size < WINDOW_SIZE)
            return;

        int left = (size - WINDOW_SIZE) / 2;
        for (int i = left; i >= 0; --i) {
            motionInfoList.remove(i);
        }
        size = motionInfoList.size();
        for (int i = size - 1; i >= WINDOW_SIZE; --i) {
            motionInfoList.remove(i);
        }
        size = WINDOW_SIZE;

        MotionFeatureInfo featureInfo = new MotionFeatureInfo();

        float[] accMean = new float[3], accSD = new float[3], accCorr = new float[3];
        float[] accSma = new float[3], al = new float[3];
        float[] coefX = new float[3], coefY = new float[3], coefZ = new float[3];

        float accSumX = 0, accSumY = 0, accSumZ = 0;
        for (MotionInfo info : motionInfoList) {
            float[] acc = info.getAcc();
            accSumX += acc[0];
            accSumY += acc[1];
            accSumZ += acc[2];
            accSma[0] += Math.abs(acc[0]);
            accSma[1] += Math.abs(acc[1]);
            accSma[2] += Math.abs(acc[2]);
        }
        accMean[0] = accSumX / size;
        accMean[1] = accSumY / size;
        accMean[2] = accSumZ / size;
        featureInfo.setAccMean(accMean);
        featureInfo.setAccSma(accSma);

        float accDiffX = 0, accDiffY = 0, accDiffZ = 0, accDiffXY = 0, accDiffYZ = 0, accDiffXZ = 0;
        for (MotionInfo info : motionInfoList) {
            float[] acc = info.getAcc();
            accDiffX += (acc[0] - accMean[0]) * (acc[0] - accMean[0]);
            accDiffY += (acc[1] - accMean[1]) * (acc[1] - accMean[1]);
            accDiffZ += (acc[2] - accMean[2]) * (acc[2] - accMean[2]);
            accDiffXY += (acc[0] - accMean[0]) * (acc[1] - accMean[1]);
            accDiffYZ += (acc[1] - accMean[1]) * (acc[2] - accMean[2]);
            accDiffXZ += (acc[0] - accMean[0]) * (acc[2] - accMean[2]);
        }
        accSD[0] = (float) Math.sqrt(accDiffX / size);
        accSD[1] = (float) Math.sqrt(accDiffY / size);
        accSD[2] = (float) Math.sqrt(accDiffZ / size);
        accCorr[0] = Math.abs(accDiffXY) / (size * accSD[0] * accSD[1]);
        accCorr[1] = Math.abs(accDiffYZ) / (size * accSD[1] * accSD[2]);
        accCorr[2] = Math.abs(accDiffXZ) / (size * accSD[0] * accSD[2]);
        featureInfo.setAccSD(accSD);
        featureInfo.setAccCorr(accCorr);

        al[0] = motionInfoList.get(0).getPre()[0];
        al[1] = motionInfoList.get(size / 2).getPre()[0];
        al[2] = motionInfoList.get(size - 1).getPre()[0];
        for (int i = 0; i < 3; ++i) {
            al[i] = 44330 * (float) (1 - Math.pow(al[i] / 1013.25, 1 / 5.255));
        }
        featureInfo.setAl(al);

//        final int order = 3;
//        float[] Rx = new float[WINDOW_SIZE - order];
//        float[] Ry = new float[WINDOW_SIZE - order];
//        float[] Rz = new float[WINDOW_SIZE - order];
//        for (int i = 0; i < WINDOW_SIZE - order; ++i) {
//            float[] acc = motionInfoList.get(order + i).getAcc();
//            Rx[i] = acc[0];
//            Ry[i] = acc[1];
//            Rz[i] = acc[2];
//        }
//
//        float[][] Xx = new float[WINDOW_SIZE - order][order];
//        float[][] Xy = new float[WINDOW_SIZE - order][order];
//        float[][] Xz = new float[WINDOW_SIZE - order][order];
//        for (int i = 0; i < WINDOW_SIZE - order; ++i) {
//            for (int j = 0; j < order; ++j) {
//                float[] acc = motionInfoList.get(order + i - j).getAcc();
//                Xx[i][j] = acc[0];
//                Xy[i][j] = acc[1];
//                Xz[i][j] = acc[2];
//            }
//        }
        for (int i = 0; i < 3; ++i) {
            coefX[i] = 0;
            coefY[i] = 0;
            coefZ[i] = 0;
        }
        featureInfo.setCoefX(coefX);
        featureInfo.setCoefY(coefY);
        featureInfo.setCoefZ(coefZ);

        ContentValues values = new ContentValues();
        values.put(API_DB.COL_TIME, end);
        values.put(API_DB.COL_ACC_MEAN_X, accMean[0]);
        values.put(API_DB.COL_ACC_MEAN_Y, accMean[1]);
        values.put(API_DB.COL_ACC_MEAN_Z, accMean[2]);
        values.put(API_DB.COL_ACC_SD_X, accSD[0]);
        values.put(API_DB.COL_ACC_SD_Y, accSD[1]);
        values.put(API_DB.COL_ACC_SD_Z, accSD[2]);
        values.put(API_DB.COL_ACC_CORR_XY, accCorr[0]);
        values.put(API_DB.COL_ACC_CORR_YZ, accCorr[1]);
        values.put(API_DB.COL_ACC_CORR_XZ, accCorr[2]);
        values.put(API_DB.COL_ACC_SMA_X, accSma[0]);
        values.put(API_DB.COL_ACC_SMA_Y, accSma[1]);
        values.put(API_DB.COL_ACC_SMA_Z, accSma[2]);
        values.put(API_DB.COL_AL_FIRST, al[0]);
        values.put(API_DB.COL_AL_MIDDLE, al[1]);
        values.put(API_DB.COL_AL_LAST, al[2]);
        values.put(API_DB.COL_COEF_X, "0,0,0");
        values.put(API_DB.COL_COEF_Y, "0,0,0");
        values.put(API_DB.COL_COEF_Z, "0,0,0");

        Log.d(TAG, "write feature " + values.toString());
        wdb.insert(API_DB.TABLE_MOTION_FEATURE, null, values);
    }

//    private float[][] multiply(float[][] A, float[][] B){
//        float[][] C = new float[A.length][B[0].length];
//
//
//    }
}

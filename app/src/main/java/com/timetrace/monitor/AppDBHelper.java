package com.timetrace.monitor;

import android.app.ActivityManager;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.timetrace.api.API_DB;
import com.timetrace.objects.ApplicationInfo;
import com.timetrace.objects.ProcessInfo;
import com.timetrace.utils.BaseUtil;
import com.timetrace.utils.DatabaseUtil;
import com.timetrace.utils.TimeUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Atomu on 2014/12/2.
 */
public class AppDBHelper extends BaseUtil {
    private static final String TAG = AppDBHelper.class.getSimpleName();

    private static SQLiteDatabase wdb = DatabaseUtil.getWritableDatabase();
    private static SQLiteDatabase rdb = DatabaseUtil.getReadableDatabase();

    /**
     * 将一组app存入数据库
     *
     * @param processInfos 当前监测到的app
     * @param current      当前时刻
     * @param slice        时间间隔
     */
    public static void writeProcess(ProcessInfo[] processInfos, long current, long slice) {
        if (wdb == null)
            return;

        for (ProcessInfo info : processInfos) {
            ContentValues values = new ContentValues();

            values.put(API_DB.COL_PACKAGE, info.getPacName());
            values.put(API_DB.COL_IMPORTANCE, info.getImportance());
            values.put(API_DB.COL_TIME, current);
            values.put(API_DB.COL_SLICE, slice);

            wdb.insert(API_DB.TABLE_PROCESS, null, values);
        }
    }

    /**
     * 从Process表统计 [lowerBound, higherBound) 时间段的 app
     *
     * @param map         查询统计结果存入map作为返回值
     * @param lowerBound  开始时刻, 这个时间不会被处理为一天的开始, 如果需要从某一天开始的时候进行查询, 需要在调用之前将这个时间设置为一天开始
     *
     * @param higherBound 结束时刻, 参照lowerBound, 函数内部不会自动处理为一天开始
     * @return process表中统计的最后一条记录的时间, 如果查询失败返回 -1
     */
    public static long readProcess(Map<String, ApplicationInfo> map, long lowerBound, long higherBound) {
        Cursor cursor = rdb.rawQuery("SELECT * FROM " + API_DB.TABLE_PROCESS + " WHERE " + API_DB.COL_TIME
                + " >= ? AND " + API_DB.COL_TIME + " < ? ", new String[]{"" + lowerBound, "" + higherBound});

        int indexPack = cursor.getColumnIndex(API_DB.COL_PACKAGE);
        int indexImp = cursor.getColumnIndex(API_DB.COL_IMPORTANCE);
        int indexTime = cursor.getColumnIndex(API_DB.COL_TIME);
        int indexSlice = cursor.getColumnIndex(API_DB.COL_SLICE);

        long time = -1;
        while (cursor.moveToNext()) {
            String packName = cursor.getString(indexPack);
            int importance = cursor.getInt(indexImp);
            long slice = cursor.getLong(indexSlice);
            time = cursor.getLong(indexTime);

            ApplicationInfo applicationInfo;
            if (map.containsKey(packName)) {
                // process importance小于SERVICE的会被忽略
                if (importance < ActivityManager.RunningAppProcessInfo.IMPORTANCE_SERVICE) {
                    applicationInfo = map.get(packName);
                    long lastActive = applicationInfo.getActiveTime();
                    applicationInfo.setActiveTime(lastActive + slice);
                }
            } else {
                applicationInfo = new ApplicationInfo();
                applicationInfo.setPackName(packName);
                if (importance < ActivityManager.RunningAppProcessInfo.IMPORTANCE_SERVICE) {
                    applicationInfo.setActiveTime(slice);
                }

                map.put(packName, applicationInfo);
            }
        }

        cursor.close();
        return time;
    }

    /**
     * 获取process表中第一条记录的时间, 不存在返回-1
     *
     * @return
     */
    public static long getFirstRecord() {
        Cursor cursor = rdb.rawQuery("SELECT " + API_DB.COL_TIME + " FROM " + API_DB.TABLE_PROCESS, null);
        long fisrtRecord = -1;
        if (cursor.moveToFirst()) {
            fisrtRecord = cursor.getLong(cursor.getColumnIndex(API_DB.COL_TIME));
        }
        cursor.close();
        return fisrtRecord;
    }

    /**
     * 参照firstRecord
     *
     * @return
     */
    public static long getLastRecord() {
        Cursor cursor = rdb.rawQuery("SELECT " + API_DB.COL_TIME + " FROM " + API_DB.TABLE_PROCESS, null);
        long lastRecord = -1;
        if (cursor.moveToLast()) {
            lastRecord = cursor.getLong(cursor.getColumnIndex(API_DB.COL_TIME));
        }
        cursor.close();
        return lastRecord;
    }

    /**
     * 最后一次统计的时刻
     *
     * @return
     */
    public static long getLastStatistic() {
        Cursor cursor = rdb.rawQuery("SELECT " + API_DB.COL_TIME + " FROM " + API_DB.TABLE_PROCESS_STATISTIC, null);
        long lastStatistic = -1;
        if (cursor.moveToLast()) {
            lastStatistic = cursor.getLong(cursor.getColumnIndex(API_DB.COL_TIME));
        }
        cursor.close();
        return lastStatistic;
    }


    /**
     * 获取某一天的TImeTrace总运行时间
     *
     * @param day 一天中的某一时刻
     * @return 从一天开始到下一天开始之间TImeTrace运行总时间
     */
    public static long getRunningTime(long day) {
        long running = 0;
        Cursor cursor = rdb.rawQuery("SELECT " + API_DB.COL_SLICE + " FROM " + API_DB.TABLE_PROCESS + " WHERE "
                        + API_DB.COL_PACKAGE + " =? AND " + API_DB.COL_TIME + " >= ? AND " + API_DB.COL_TIME + " <?",
                new String[]{monitor.getPackageName(), TimeUtil.getDayStart(day) + "", TimeUtil.nextDayStart(day) + ""});

        int indexSlice = cursor.getColumnIndex(API_DB.COL_SLICE);
        while (cursor.moveToNext()) {
            long slice = cursor.getLong(indexSlice);
            running += slice;
        }
        return running;
    }

    /**
     * 从 ProSta 查询 [lowerBound, higherBound) 时间段的 app
     *
     * @param map         查询结果处理成map作为返回值
     * @param lowerBound  参照readProcess
     * @param higherBound 参照readProcess
     * @return ProSta 表中查询的最后一条记录的时间, 查询失败返回 -1
     */
    public static long readProcessStatistic(Map<String, ApplicationInfo> map, long lowerBound, long higherBound) {
        map.clear();

        Cursor cursor = wdb.rawQuery("SELECT * FROM " + API_DB.TABLE_PROCESS_STATISTIC + " WHERE "
                + API_DB.COL_TIME + " >= ? AND " + API_DB.COL_TIME + " < ?", new String[]{"" + lowerBound, "" + higherBound});

        int indexPack = cursor.getColumnIndex(API_DB.COL_PACKAGE);
        int indexTime = cursor.getColumnIndex(API_DB.COL_TIME);
        int indexActive = cursor.getColumnIndex(API_DB.COL_ACTIVE);
        long time = -1;
        while (cursor.moveToNext()) {
            String packName = cursor.getString(indexPack);
            time = cursor.getLong(indexTime);
            long active = cursor.getLong(indexActive);

//            Log.d(TAG, packName + " -- " + TimeUtil.getAbsTimeStr(time) + " -- " + TimeUtil.getRelTimeStr(active));

            ApplicationInfo info;
            if (map.containsKey(packName)) {
                info = map.get(packName);
                info.setActiveTime(info.getActiveTime() + active);
            } else {
                info = new ApplicationInfo();
                info.setPackName(packName);
                info.setActiveTime(active);

                map.put(packName, info);
            }
        }

        cursor.close();
        return time;
    }


    /**
     * @param start 从 process 表统计从 start 之前一个凌晨开始到下一个凌晨的 slice, 如果这个凌晨对应的一天与最近一次统计不是同一天则插入,
     *              否则 update, 由于统计是从 start 当天凌晨开始的, 因此 update 是可以直接覆盖的
     *              必须满足, start 小于 lastStatistic,
     *              同时 这个函数的调用必须是单例的
     */
    private synchronized static void writeProcessStatisticSingleDay(long start) {
        Map<String, ApplicationInfo> map = new HashMap<>();

        start = TimeUtil.getDayStart(start);
        long end = TimeUtil.nextDayStart(start);
        long currentStatistic = readProcess(map, start, end);
        if (currentStatistic == -1)
            return;

        /**
         * start <= currentStatistic < end
         * start < end
         * lastStatistic <= start
         * lastStatistic <= currentStatistic
         */

        Log.d(TAG, "write pro sta single day from " + start + " to " + end);

        long lastStatistic = getLastStatistic();
        if (TimeUtil.sameDay(currentStatistic, lastStatistic)) {
            // TODO: 更新 ProSta 中 Time == lastStatistic 的记录

            Log.d(TAG, "it is the same day, so update");
            for (String key : map.keySet()) {
                ApplicationInfo info = map.get(key);
                ContentValues values = new ContentValues();
                values.put(API_DB.COL_TIME, currentStatistic);
                values.put(API_DB.COL_PACKAGE, info.getPacName());
                values.put(API_DB.COL_ACTIVE, info.getActiveTime());

                Cursor cursor = rdb.rawQuery("SELECT " + API_DB.COL_TIME + " FROM " + API_DB.TABLE_PROCESS_STATISTIC + " WHERE "
                        + API_DB.COL_TIME + " =? AND " + API_DB.COL_PACKAGE + " =?", new String[]{lastStatistic + "", info.getPacName() + ""});

                //TODO: update 或 insert !!!
                if (cursor.getColumnCount() > 0) {
                    wdb.update(API_DB.TABLE_PROCESS_STATISTIC, values, API_DB.COL_TIME + " =? AND " + API_DB.COL_PACKAGE + " =?",
                            new String[]{"" + lastStatistic, info.getPacName()});
                } else {
                    wdb.insert(API_DB.TABLE_PROCESS_STATISTIC, null, values);
                }
            }
        } else {
            // TODO: 根据注释中的关系, 如果这两个 statistic 不属于同一天, 那么必然 curSta 是 lastSta 之后的某一天

            Log.d(TAG, "not the same day, so insert");
            for (String key : map.keySet()) {
                ApplicationInfo info = map.get(key);
                ContentValues values = new ContentValues();
                values.put(API_DB.COL_TIME, currentStatistic);
                values.put(API_DB.COL_PACKAGE, info.getPacName());
                values.put(API_DB.COL_ACTIVE, info.getActiveTime());

                wdb.insert(API_DB.TABLE_PROCESS_STATISTIC, null, values);
            }
        }

    }

    /**
     * 从最后一次统计的时间节点开始统计到当前时刻, 也就是说这个时间段之内是没有统计过的, 只需要从Process表来统计就行了
     */
    public synchronized static void writeProcessStatistic() {
        long lastStatistic = getLastStatistic();
        if (lastStatistic == -1) {
            // 此时 ProSta 没有记录
            long firstRecord = getFirstRecord();
            if (firstRecord == -1)
                return;
            lastStatistic = firstRecord;
        }
        long current = System.currentTimeMillis();

        Log.d(TAG, "start is " + TimeUtil.getAbsTimeStr(lastStatistic));
        Log.d(TAG, "current is " + TimeUtil.getAbsTimeStr(current));

        /**
         * 从最后一次统计 ( 实际上是最后一次统计当天凌晨开始 )
         * 当 time == current 的凌晨时, 最后一次统计 current 这一天
         */
        for (long time = TimeUtil.getDayStart(lastStatistic); time <= TimeUtil.getDayStart(current); time = TimeUtil.nextDayStart(time)) {
            Log.d(TAG, "write pro sta from " + time);
            writeProcessStatisticSingleDay(time);
        }
    }

}

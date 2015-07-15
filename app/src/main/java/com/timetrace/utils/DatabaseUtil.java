package com.timetrace.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.timetrace.api.API_DB;

/**
 * Created by Atomu on 2014/11/22.
 * TODO: 先做成db不close的单例, 随后改成db连接的单例吧
 */
public class DatabaseUtil extends BaseUtil {
    private static final String CREATE_TABLE_PROCESS = "CREATE TABLE IF NOT EXISTS "
            + API_DB.TABLE_PROCESS + " ("
            + API_DB.COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + API_DB.COL_TIME + " LONG, "
            + API_DB.COL_PACKAGE + " STRING, "
            + API_DB.COL_IMPORTANCE + " INTEGER, "
            + API_DB.COL_SLICE + " LONG)";
    private static final String DROP_TABLE_PROCESS = "DROP TABLE IF EXISTS " + API_DB.TABLE_PROCESS;
    private static final String CREATE_TABLE_PROCESS_STATISTIC = "CREATE TABLE IF NOT EXISTS "
            + API_DB.TABLE_PROCESS_STATISTIC + " ("
            + API_DB.COL_TIME + " LONG, "
            + API_DB.COL_PACKAGE + " STRING, "
            + API_DB.COL_ACTIVE + " LONG)";
    private static final String DROP_TABLE_PROCESS_STATISTIC = "DROP TABLE IF EXISTS "
            + API_DB.TABLE_PROCESS_STATISTIC;
    private static final String CREATE_TABLE_MOTION = "CREATE TABLE IF NOT EXISTS "
            + API_DB.TABLE_MOTION + " ("
            + API_DB.COL_TIME + " LONG, "
            + API_DB.COL_ACC_X + " FLOAT, "
            + API_DB.COL_ACC_Y + " FLOAT, "
            + API_DB.COL_ACC_Z + " FLOAT, "
            + API_DB.COL_PRE_HEIGHT + " FLOAT, "
            + API_DB.COL_GYR_X + " FLOAT, "
            + API_DB.COL_GYR_Y + " FLOAT, "
            + API_DB.COL_GYR_Z + " FLOAT, "
            + API_DB.COL_GPS_SPEED + " FLOAT, "
            + API_DB.COL_GPS_LAT + " DOUBLE, "
            + API_DB.COL_GPS_LNG + " DOUBLE)";
    private static final String DROP_TABLE_MOTION = "DROP TABLE IF EXISTS " + API_DB.TABLE_MOTION;
    private static final String CREATE_TABLE_MOTION_FEATURE = "CREATE TABLE IF NOT EXISTS "
            + API_DB.TABLE_MOTION_FEATURE + " ("
            + API_DB.COL_TIME + " LONG, "
            + API_DB.COL_ACC_MEAN_X + " FLOAT, "
            + API_DB.COL_ACC_MEAN_Y + " FLOAT, "
            + API_DB.COL_ACC_MEAN_Z + " FLOAT, "
            + API_DB.COL_ACC_SD_X + " FLOAT, "
            + API_DB.COL_ACC_SD_Y + " FLOAT, "
            + API_DB.COL_ACC_SD_Z + " FLOAT, "
            + API_DB.COL_ACC_CORR_XY + " FLOAT, "
            + API_DB.COL_ACC_CORR_YZ + " FLOAT, "
            + API_DB.COL_ACC_CORR_XZ + " FLOAT, "
            + API_DB.COL_ACC_SMA_X + " FLOAT, "
            + API_DB.COL_ACC_SMA_Y + " FLOAT, "
            + API_DB.COL_ACC_SMA_Z + " FLOAT, "
            + API_DB.COL_AL_FIRST + " FLOAT, "
            + API_DB.COL_AL_MIDDLE + " FLOAT, "
            + API_DB.COL_AL_LAST + " FLOAT, "
            + API_DB.COL_COEF_X + " STRING, "
            + API_DB.COL_COEF_Y + " STRING, "
            + API_DB.COL_COEF_Z + " STRING)";
    private static final String DROP_TABLE_MOTION_FEATURE = "DROP TABLE IF EXISTS " + API_DB.TABLE_MOTION_FEATURE;

    private static final String TAG = "databaseUtil";

    private static DBHelper helper;
    private static SQLiteDatabase rdb;
    private static SQLiteDatabase wdb;

    public static void init() {
        if (helper == null)
            helper = new DBHelper(monitor, API_DB.DB_NAME, null, 1);

        if (rdb == null)
            rdb = helper.getReadableDatabase();

        if (wdb == null)
            wdb = helper.getWritableDatabase();

        //TODO: create table here
        if (wdb != null) {
            wdb.execSQL(CREATE_TABLE_PROCESS);
            wdb.execSQL(CREATE_TABLE_PROCESS_STATISTIC);
            wdb.execSQL(CREATE_TABLE_MOTION);
            wdb.execSQL(CREATE_TABLE_MOTION_FEATURE);
        }
    }

    public static SQLiteDatabase getWritableDatabase() {
        return wdb;
    }

    public static SQLiteDatabase getReadableDatabase() {
        return rdb;
    }

    private static class DBHelper extends SQLiteOpenHelper {

        public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }
}

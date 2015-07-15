package com.timetrace.api;

/**
 * Created by Atomu on 2014/11/27.
 * 保存数据库字段名等
 */
public interface API_DB {
    public final String DB_NAME = "USER";

    /**
     * process表, 存app使用统计
     * API_DB.TABLE_PROCESS + " ("
     + API_DB.COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
     + API_DB.COL_TIME + " LONG, "
     + API_DB.COL_PACKAGE + " STRING, "
     + API_DB.COL_IMPORTANCE + " INTEGER, "
     + API_DB.COL_SLICE + " LONG)"
     */
    public final String TABLE_PROCESS = "PROCESS";
    public final String COL_ID = "PID";
    public final String COL_TIME = "TIME";
    public final String COL_IMPORTANCE = "IMPORTANCE";
    public final String COL_PACKAGE = "PACKAGE";
    public final String COL_SLICE = "SLICE";

    /**
     * process的统计
     * + API_DB.TABLE_PROCESS_STATISTIC + " ("
     + API_DB.COL_TIME + " LONG, "
     + API_DB.COL_PACKAGE + " STRING, "
     + API_DB.COL_ACTIVE + " LONG)"
     */
    public final String TABLE_PROCESS_STATISTIC = "PROCESS_STATISTIC";
    // 从上一次统计到TIME时刻, app处于active的总时间
    public final String COL_ACTIVE = "ACTIVE";

    public final String TABLE_MOTION = "MOTION";
    public final String COL_ACC_X = "ACC_X";      // float f = 100 Hz
    public final String COL_ACC_Y = "ACC_Y";
    public final String COL_ACC_Z = "ACC_Z";
    public final String COL_GYR_X = "GYR_X";    // float f = 100Hz
    public final String COL_GYR_Y = "GYR_Y";
    public final String COL_GYR_Z = "GYR_Z";
    public final String COL_PRE_HEIGHT = "PRE_HEIGHT";      // float f = 20Hz
    public final String COL_GPS_LAT = "GPS_LAT";    // double f = 20Hz
    public final String COL_GPS_LNG = "GPS_LNG";
    public final String COL_GPS_SPEED = "GPS_SPEED";

    public final String TABLE_MOTION_FEATURE = "MOTION_FEATURE";
    public final String COL_ACC_MEAN_X = "ACC_MEAN_X";
    public final String COL_ACC_MEAN_Y = "ACC_MEAN_Y";
    public final String COL_ACC_MEAN_Z = "ACC_MEAN_Z";
    public final String COL_ACC_SD_X = "ACC_SD_X";
    public final String COL_ACC_SD_Y = "ACC_SD_Y";
    public final String COL_ACC_SD_Z = "ACC_SD_Z";
    public final String COL_ACC_CORR_XY = "ACC_CORR_XY";
    public final String COL_ACC_CORR_YZ = "ACC_CORR_YZ";
    public final String COL_ACC_CORR_XZ = "ACC_CORR_XZ";
    public final String COL_ACC_SMA_X = "ACC_SMA_X";
    public final String COL_ACC_SMA_Y = "ACC_SMA_Y";
    public final String COL_ACC_SMA_Z = "ACC_SMA_Z";
    public final String COL_AL_FIRST = "AL_FIRST";
    public final String COL_AL_MIDDLE = "AL_MIDDLE";
    public final String COL_AL_LAST = "AL_LAST";
    public final String COL_COEF_X = "COEF_X";
    public final String COL_COEF_Y = "COEF_Y";
    public final String COL_COEF_Z = "COEF_Z";
}

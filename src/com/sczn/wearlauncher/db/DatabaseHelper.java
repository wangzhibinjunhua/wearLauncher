package com.sczn.wearlauncher.db;

import com.sczn.wearlauncher.LauncherApp;
import com.sczn.wearlauncher.db.provider.Provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


/**
 * Created by wuzhiyi on 2016/1/7.
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "sports_step_count.db";
    private static final int DATABASE_VERSION = 4;
    
    private static class Hold{
    	//private static DatabaseHelper instance = new DatabaseHelper();
    }
    
    public static DatabaseHelper getInstance(Context context){
    	//return Hold.instance;
    	return new DatabaseHelper(context);
    }

    public DatabaseHelper(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL("CREATE TABLE "+ Provider.SportsUserInfoColumns.TABLE_NAME + " ("
                + Provider.SportsUserInfoColumns._ID + " INTEGER PRIMARY KEY,"
                + Provider.SportsUserInfoColumns.COLUMNS_USER_SEX + " INTEGER DEFAULT 0,"
                + Provider.SportsUserInfoColumns.COLUMNS_USER_AGE + " INTEGER DEFAULT 28,"
                + Provider.SportsUserInfoColumns.COLUMNS_USER_LENGTH + " INTEGER DEFAULT 172,"
                + Provider.SportsUserInfoColumns.COLUMNS_USER_WEIGHT + " INTEGER DEFAULT 68,"
                + Provider.SportsUserInfoColumns.COLUMNS_START_TIME + " DATETIME DEFAULT CURRENT_TIMESTAMP,"
                + Provider.SportsUserInfoColumns.COLUMNS_TOTAL_COUNT + " INTEGER DEFAULT 0"
                + ");");

        db.execSQL("CREATE TABLE " + Provider.StepCountDetailColumns.TABLE_NAME + " ("
                + Provider.StepCountDetailColumns._ID + " INTEGER PRIMARY KEY,"
                + Provider.StepCountDetailColumns.COLUMNS_GET_TIME + " LONG NOT NULL,"
                + Provider.StepCountDetailColumns.COLUMNS_SETP_COUNT + " INTEGER,"
                + Provider.StepCountDetailColumns.COLUMNS_DISTANCE + " DOUBLE,"
                + Provider.StepCountDetailColumns.COLUMNS_KCAL + " DOUBLE"
                + ");");

        db.execSQL("CREATE TABLE " + Provider.SleepTimeDetailColumns.TABLE_NAME + " ("
                + Provider.SleepTimeDetailColumns._ID + " INTEGER PRIMARY KEY,"
                + Provider.SleepTimeDetailColumns.COLUMNS_START_TIME + " LONG NOT NULL,"
                + Provider.SleepTimeDetailColumns.COLUMNS_END_TIME + " LONG NOT NULL,"
                + Provider.SleepTimeDetailColumns.COLUMNS_SLEEP_STATE + " INTEGER,"
                + Provider.SleepTimeDetailColumns.COLUMNS_SLEEP_STATE_STEP + " INTEGER"
                + ");");
        db.execSQL("CREATE TABLE " + Provider.HeartRateDetailColumns.TABLE_NAME + " ("
                + Provider.HeartRateDetailColumns._ID + " INTEGER PRIMARY KEY,"
                + Provider.HeartRateDetailColumns.COLUMNS_TIME + " LONG NOT NULL,"
                + Provider.HeartRateDetailColumns.COLUMNS_HEART_RATE + " LONG"
                + ");");
        db.execSQL("CREATE TABLE " + Provider.PressureDetailColumns.TABLE_NAME + " ("
                + Provider.PressureDetailColumns._ID + " INTEGER PRIMARY KEY,"
                + Provider.PressureDetailColumns.COLUMNS_TIME + " LONG NOT NULL,"
                + Provider.PressureDetailColumns.COLUMNS_PRESSURE + " DOUBLE"
                + ");");
        
        //健康提醒闹钟数据表
        db.execSQL("CREATE TABLE " + Provider.HealthAlarmcolumns.TABLE_NAME + " ("
                + Provider.HealthAlarmcolumns._ID + " INTEGER PRIMARY KEY,"
                + Provider.HealthAlarmcolumns.COLUMNS_TIME + " LONG NOT NULL,"
                + Provider.HealthAlarmcolumns.COLUMNS_TYPE + " INTEGER,"
                + Provider.HealthAlarmcolumns.COLUMNS_REPEAT + " INTEGER,"
                + Provider.HealthAlarmcolumns.COLUMNS_EBABLE + " INTEGER"
                + ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db,int oldVersion,int newVersion){
        db.execSQL("DROP TABLE IF EXISTS " + Provider.SportsUserInfoColumns.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + Provider.StepCountDetailColumns.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + Provider.SleepTimeDetailColumns.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + Provider.HeartRateDetailColumns.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + Provider.PressureDetailColumns.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + Provider.HealthAlarmcolumns.TABLE_NAME);
        onCreate(db);
    }

}

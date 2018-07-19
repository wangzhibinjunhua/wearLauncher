package com.sczn.wearlauncher.db.provider;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by mxy on 2017/1/7.
 */
public class Provider {
	
	public static final String AUTHORITY_COMMON = "com.sczn.wearlauncher.db.provider";

    public static final class SportsUserInfoColumns implements BaseColumns{
        public static final String AUTHORITY = AUTHORITY_COMMON + ".StepcountProvider";

        public static final String TABLE_NAME = "user_info";
        public static final String COLUMNS_USER_SEX = "user_sex";
        public static final String COLUMNS_USER_AGE = "user_age";
        public static final String COLUMNS_USER_LENGTH = "user_length";
        public static final String COLUMNS_USER_WEIGHT = "user_weight";
        public static final String COLUMNS_START_TIME = "start_time";
        public static final String COLUMNS_TOTAL_COUNT = "total_count";
        public static final String DEFAULT_SORT_ORDER = COLUMNS_TOTAL_COUNT +" desc";

        public static final Uri CONTENT_URI = Uri.parse("content://"+ AUTHORITY +"/"+TABLE_NAME);
    }

    public static final class StepCountDetailColumns implements BaseColumns{
        public static final String AUTHORITY = AUTHORITY_COMMON + ".StepcountProvider";

        public static final String TABLE_NAME = "step_count_detail";
        public static final String COLUMNS_GET_TIME = "get_time";
        public static final String COLUMNS_SETP_COUNT = "step_count";
        public static final String COLUMNS_DISTANCE = "distance";
        public static final String COLUMNS_KCAL = "kcal";
        public static final String DEFAULT_SORT_ORDER = COLUMNS_GET_TIME;

        public static final Uri CONTENT_URI = Uri.parse("content://"+ AUTHORITY +"/"+TABLE_NAME);
    }

    public static final class SleepTimeDetailColumns implements BaseColumns{
        public static final String AUTHORITY = AUTHORITY_COMMON + ".SleepTimeProvider";

        public static final String TABLE_NAME = "sleep_time_detail";
        public static final String COLUMNS_START_TIME = "start_time";
        public static final String COLUMNS_END_TIME = "end_time";
        public static final String COLUMNS_SLEEP_STATE = "sleep_state";
        public static final String COLUMNS_SLEEP_STATE_STEP = "step";
        public static final String DEFAULT_SORT_ORDER = COLUMNS_START_TIME;

        public static final Uri CONTENT_URI = Uri.parse("content://"+ AUTHORITY +"/"+TABLE_NAME);
    }
    
    public static final class HeartRateDetailColumns implements BaseColumns{
    	public static final String AUTHORITY = AUTHORITY_COMMON + ".HeartRateProvider";
    	
    	public static final String TABLE_NAME = "heart_rate_detail";
        public static final String COLUMNS_TIME = "start_time";
        public static final String COLUMNS_HEART_RATE = "heart_rate";
        public static final String DEFAULT_SORT_ORDER = COLUMNS_TIME;

        public static final Uri CONTENT_URI = Uri.parse("content://"+ AUTHORITY +"/"+TABLE_NAME); 
    }
    
    public static final class PressureDetailColumns implements BaseColumns{
    	public static final String AUTHORITY = AUTHORITY_COMMON + ".PressureProvider";
    	
    	public static final String TABLE_NAME = "pressure_detail";
        public static final String COLUMNS_TIME = "time";
        public static final String COLUMNS_PRESSURE = "pressure";
        public static final String DEFAULT_SORT_ORDER = COLUMNS_TIME;

        public static final Uri CONTENT_URI = Uri.parse("content://"+ AUTHORITY +"/"+TABLE_NAME); 
    }
    
    public static final class HealthAlarmcolumns implements BaseColumns{
    	public static final String AUTHORITY = AUTHORITY_COMMON + ".HealthAlarmProvider";
    	
    	public static final String TABLE_NAME = "healthalarm";
        public static final String COLUMNS_TIME = "time";
        public static final String COLUMNS_TYPE = "type";
        public static final String COLUMNS_REPEAT = "repeat";
        public static final String COLUMNS_EBABLE = "enable";
        public static final String DEFAULT_SORT_ORDER = COLUMNS_TIME;

        public static final Uri CONTENT_URI = Uri.parse("content://"+ AUTHORITY +"/"+TABLE_NAME); 
    }
}

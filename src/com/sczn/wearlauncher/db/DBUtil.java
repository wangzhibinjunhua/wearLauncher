package com.sczn.wearlauncher.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import com.sczn.wearlauncher.db.bean.SportsStepCountInfo;
import com.sczn.wearlauncher.db.provider.Provider;
import com.sczn.wearlauncher.db.provider.Provider.StepCountDetailColumns;
import com.sczn.wearlauncher.db.util.SportsUtil;
import com.sczn.wearlauncher.util.MxyLog;
import com.sczn.wearlauncher.util.SharedPreferencesUtils;

public class DBUtil {
	private static final String TAG = DBUtil.class.getSimpleName();
	private DatabaseHelper dbHelper;
	private Context mContext;

	public DBUtil(Context context) {
		if (dbHelper == null) {
			dbHelper = DatabaseHelper.getInstance(context);
		}
		this.mContext = context;
	}

	// 新步数产生
	public long saveStepCount(int stepCount, long recordHour) {

		long insertResult = -1L;
		int dbStep = 0;
		int saveStep = 0;
		double saveCal = 0.0;
		double saveDistance = 0.0;
		ContentValues values = new ContentValues();
		if (dbHelper != null) {
			try {
				SQLiteDatabase db = dbHelper.getWritableDatabase();
				Cursor cursor = db
						.query(Provider.StepCountDetailColumns.TABLE_NAME,
								new String[] { Provider.StepCountDetailColumns.COLUMNS_SETP_COUNT },
								Provider.StepCountDetailColumns.COLUMNS_GET_TIME
										+ "==" + String.valueOf(recordHour),
								null, null, null, null);
				if (cursor != null && cursor.getCount() != 0
						&& cursor.moveToFirst()) {
					dbStep = cursor
							.getInt(cursor
									.getColumnIndex(Provider.StepCountDetailColumns.COLUMNS_SETP_COUNT));
				} else {
					dbStep = 0;
				}
				saveStep = dbStep + stepCount;
				saveDistance = SportsUtil.getDistance(mContext, saveStep);
				saveCal = SportsUtil.getCal(mContext, saveDistance);
				if (dbStep == 0) {
					// 插入
					values.put(
							Provider.StepCountDetailColumns.COLUMNS_GET_TIME,
							recordHour);
					values.put(
							Provider.StepCountDetailColumns.COLUMNS_SETP_COUNT,
							saveStep);
					values.put(
							Provider.StepCountDetailColumns.COLUMNS_DISTANCE,
							saveDistance);
					values.put(
							Provider.StepCountDetailColumns.COLUMNS_KCAL,
							saveCal);
					insertResult = db.insert(
							Provider.StepCountDetailColumns.TABLE_NAME,
							null, values);
				} else {
					// 更新
					values.put(
							Provider.StepCountDetailColumns.COLUMNS_SETP_COUNT,
							saveStep);
					values.put(
							Provider.StepCountDetailColumns.COLUMNS_DISTANCE,
							saveDistance);
					values.put(
							Provider.StepCountDetailColumns.COLUMNS_KCAL,
							saveCal);
					insertResult = db
							.update(Provider.StepCountDetailColumns.TABLE_NAME,
									values,
									Provider.StepCountDetailColumns.COLUMNS_GET_TIME
											+ "=?", new String[] { String
											.valueOf(recordHour) });
				}
				cursor.close();
				db.close();
				long time = Calendar.getInstance().getTimeInMillis();
				SimpleDateFormat sdf = new SimpleDateFormat(
						"yyyy-MM-dd HH:mm:ss");
				SharedPreferencesUtils.setParam(mContext,
						"step:" + sdf.format(time),
						String.valueOf(stepCount));
			} catch (Exception e) {
				MxyLog.e("dmm", e.getMessage());
				long time = Calendar.getInstance().getTimeInMillis();
				SimpleDateFormat sdf = new SimpleDateFormat(
						"yyyy-MM-dd HH:mm:ss");
				SharedPreferencesUtils.setParam(mContext, sdf.format(time),
						"db error:" + e.getMessage());
				return -1;
			}
		}
		if (insertResult != -1) {
			mContext.getContentResolver().notifyChange(
					StepCountDetailColumns.CONTENT_URI, null);
		}
		return insertResult;
	
	}

	// 查询一段时间内的记步数据
	public ArrayList<SportsStepCountInfo> getStepCountBetweenTimes(
			long beginTime, long endTime) {
		SportsStepCountInfo sportsStepCountInfo = null;
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery(
				"SELECT * FROM " + Provider.StepCountDetailColumns.TABLE_NAME
						+ " where "
						+ Provider.StepCountDetailColumns.COLUMNS_GET_TIME
						+ " >= ? and "
						+ Provider.StepCountDetailColumns.COLUMNS_GET_TIME
						+ " < ? " + "order by "
						+ Provider.StepCountDetailColumns.DEFAULT_SORT_ORDER,
				new String[] { String.valueOf(beginTime),
						String.valueOf(endTime) });
		ArrayList<SportsStepCountInfo> sportsStepCountInfos = new ArrayList<SportsStepCountInfo>();
		while (cursor != null && !cursor.isClosed() && cursor.moveToNext()) {
			try {
				sportsStepCountInfo = new SportsStepCountInfo();
				sportsStepCountInfo
						.setGetTime(cursor.getLong(cursor
								.getColumnIndex(Provider.StepCountDetailColumns.COLUMNS_GET_TIME)));
				sportsStepCountInfo
						.setStepCount(cursor.getLong(cursor
								.getColumnIndex(Provider.StepCountDetailColumns.COLUMNS_SETP_COUNT)));
				sportsStepCountInfo
						.setDistance(cursor.getDouble(cursor
								.getColumnIndex(Provider.StepCountDetailColumns.COLUMNS_DISTANCE)));
				sportsStepCountInfo
						.setKcal(cursor.getDouble(cursor
								.getColumnIndex(Provider.StepCountDetailColumns.COLUMNS_KCAL)));
				sportsStepCountInfos.add(sportsStepCountInfo);
			} catch (Exception e) {

			}
		}
		cursor.close();
		db.close();
		return sportsStepCountInfos;
	}

	// 插入用户信息
	public long insertUserInfo(int sex, int age, int length, int weight,
			long startTime, long stepCount) {
		long insertResult = -1L;
		if (dbHelper != null) {
			SQLiteDatabase db = dbHelper.getWritableDatabase();
			ContentValues values = new ContentValues();
			values.put(Provider.SportsUserInfoColumns.COLUMNS_USER_SEX, sex);
			values.put(Provider.SportsUserInfoColumns.COLUMNS_USER_AGE, age);
			values.put(Provider.SportsUserInfoColumns.COLUMNS_USER_LENGTH,
					length);
			values.put(Provider.SportsUserInfoColumns.COLUMNS_USER_WEIGHT,
					weight);
			values.put(Provider.SportsUserInfoColumns.COLUMNS_START_TIME,
					startTime);
			values.put(Provider.SportsUserInfoColumns.COLUMNS_TOTAL_COUNT,
					stepCount);
			insertResult = db.insert(Provider.SportsUserInfoColumns.TABLE_NAME,
					null, values);
			db.close();
		}
		return insertResult;
	}

	// 更新用户信息
	public long updateUserInfo(int userId, int sex, int age, int length,
			int weight) {
		long insertResult = -1L;
		if (dbHelper != null) {
			SQLiteDatabase db = dbHelper.getWritableDatabase();
			ContentValues values = new ContentValues();
			values.put(Provider.SportsUserInfoColumns.COLUMNS_USER_SEX, sex);
			values.put(Provider.SportsUserInfoColumns.COLUMNS_USER_AGE, age);
			values.put(Provider.SportsUserInfoColumns.COLUMNS_USER_LENGTH,
					length);
			values.put(Provider.SportsUserInfoColumns.COLUMNS_USER_WEIGHT,
					weight);
			insertResult = db.update(Provider.SportsUserInfoColumns.TABLE_NAME,
					values, Provider.SportsUserInfoColumns._ID,
					new String[] { String.valueOf(userId) });
			db.close();
		}
		return insertResult;
	}

	// 插入睡眠数据
	public long insertSleepTime(long startTime, long endTime, int sleepState,
			int step) {

		long insertResult = -1L;
		if (dbHelper != null) {
			SQLiteDatabase db = dbHelper.getWritableDatabase();
			ContentValues values = new ContentValues();
			values.put(Provider.SleepTimeDetailColumns.COLUMNS_START_TIME,
					startTime);
			values.put(Provider.SleepTimeDetailColumns.COLUMNS_END_TIME,
					endTime);
			values.put(Provider.SleepTimeDetailColumns.COLUMNS_SLEEP_STATE,
					sleepState);
			values.put(
					Provider.SleepTimeDetailColumns.COLUMNS_SLEEP_STATE_STEP,
					step);
			insertResult = db.insert(
					Provider.SleepTimeDetailColumns.TABLE_NAME, null,
					values);
			db.close();
		}
		return insertResult;
	
	}

	public DatabaseHelper getDbHelper() {
		return dbHelper;
	}

	// 插入心率数据
	public long insertHeartRate(long time, int heartrate) {

		long insertResult = -1L;
		if (dbHelper != null) {
			SQLiteDatabase db = dbHelper.getWritableDatabase();
			ContentValues values = new ContentValues();
			values.put(Provider.HeartRateDetailColumns.COLUMNS_TIME, time);
			values.put(Provider.HeartRateDetailColumns.COLUMNS_HEART_RATE,
					heartrate);
			insertResult = db.insert(
					Provider.HeartRateDetailColumns.TABLE_NAME, null,
					values);
			db.close();
		}
		return insertResult;
	
	}

	// 插入气压数据
	public long insertPressure(long time, float pressure) {

		long insertResult = -1L;
		if (dbHelper != null) {
			SQLiteDatabase db = dbHelper.getWritableDatabase();
			ContentValues values = new ContentValues();
			values.put(Provider.PressureDetailColumns.COLUMNS_TIME, time);
			values.put(Provider.PressureDetailColumns.COLUMNS_PRESSURE,
					pressure);
			insertResult = db
					.insert(Provider.PressureDetailColumns.TABLE_NAME,
							null, values);
			db.close();
		}
		return insertResult;
	
	}

	/********************************** 健康提醒 *******************************/

	//	新建闹钟
	public long insertAlarm(long time, int type, int repeatType, boolean enable) {

		//MxyLog.d(this, "insertAlarm" + "dbHelper=" + dbHelper + "--time=" + time + "--type=" + type + "--repeatType=" + repeatType + "--enable=" + enable);
		long result = -1L;
		if (dbHelper != null) {
			SQLiteDatabase db = dbHelper.getWritableDatabase();
			ContentValues values = new ContentValues();
			values.put(Provider.HealthAlarmcolumns.COLUMNS_TIME, time);
			values.put(Provider.HealthAlarmcolumns.COLUMNS_TYPE, type);
			values.put(Provider.HealthAlarmcolumns.COLUMNS_REPEAT,
					repeatType);
			values.put(Provider.HealthAlarmcolumns.COLUMNS_EBABLE, enable ? 1 : 0);
			result = db.insert(
					Provider.HealthAlarmcolumns.TABLE_NAME, null, values);
			db.close();
		}
		return result;
	
	}
	
	//	删除闹钟
	public long deleteAlarm(int recordId){

		long result = -1L;
		if (dbHelper != null) {
			SQLiteDatabase db = dbHelper.getWritableDatabase();
			ContentValues values = new ContentValues();
			result = db.delete(Provider.HealthAlarmcolumns.TABLE_NAME,
					Provider.SportsUserInfoColumns._ID + "=",
					new String[] { String.valueOf(recordId) });
			db.close();
		}
		return result;
	
	}
	
	//修改闹钟
	public long updateAlarm(int recordId, long time, int type, int repeatType, boolean enable) {

		long result = -1L;
		if (dbHelper != null) {
			SQLiteDatabase db = dbHelper.getWritableDatabase();
			ContentValues values = new ContentValues();
			values.put(Provider.HealthAlarmcolumns.COLUMNS_TIME, time);
			values.put(Provider.HealthAlarmcolumns.COLUMNS_TYPE, type);
			values.put(Provider.HealthAlarmcolumns.COLUMNS_REPEAT,
					repeatType);
			values.put(Provider.HealthAlarmcolumns.COLUMNS_EBABLE, enable);
			result = db.update(Provider.HealthAlarmcolumns.TABLE_NAME, values,
					Provider.SportsUserInfoColumns._ID + "=",
					new String[] { String.valueOf(recordId) });
			db.close();
		}
		return result;
	
	}

	/********************************** 健康提醒 *******************************/

}

package com.sczn.wearlauncher.db.util;


import java.util.HashMap;

import com.sczn.wearlauncher.receivers.AbsBroadcastReceiver;
import com.sczn.wearlauncher.services.MainService;
import com.sczn.wearlauncher.util.DateFormatUtil;
import com.sczn.wearlauncher.util.MxyLog;
import com.sczn.wearlauncher.util.LogFile;
import com.sczn.wearlauncher.util.SysServices;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.os.SystemClock;
import android.provider.Settings;

public class SensorManagerUtil {
	private static final String TAG = SensorManagerUtil.class.getSimpleName();
	
	public static final String ACTION_START_SPORT = "com.sczn.sport.start.action";
	public static final String ACTION_STOP_SPORT = "com.sczn.sport.start.action";
	public static final String ACTION_ALARM_INIT = "broadcast.alarm.init";
	public static final String ACTION_ALARM_STEP = "broadcast.alarm.step";
	public static final String ACTION_ALARM_SLEEP = "broadcast.alarm.sleep";
	public static final String ACTION_ALARM_PRESSURE = "broadcast.alarm.pressure";
	
	public static final long ALARM_STEP_SLEEP_SHORT = 10 * 1000;
	public static final long ALARM_STEP_SLEEP = 1 * 60 * 1000;
	public static final long ALARM_PRESSURE = 15 * 60 * 1000;
	
	public static final long SENSOR_LISTEN_UNREGISTER_DELAY = 3*1000;
	
	public static final String SETTING_KEY_STEP_CURR = "step_curr";
	public static final String SETTING_KEY_STEP_SLEEP = "step_sleep";
	public static final String SETTING_KEY_SPORT_STATE = "sportsEnable";
	
	public static final String SENSOR_OBSERVER_STEP = "step_observer";
	public static final String SENSOR_OBSERVER_SLEEP = "sleep_observer";
	public static final String SENSOR_OBSERVER_PRESSURE= "pressure_observer";
	
	private static final int WAKE_LOCK_FLAG_STEP = 1;
	private static final int WAKE_LOCK_FLAG_SLEEP= 2;
	private static final int WAKE_LOCK_FLAG_PRESSURE = 4;

	private HashMap<String, ISensorChanger> mObserversMap;
	
	private Context mContext;
	private AlarmManager mAlarmManager;
	private PowerManager mPowerManager;
	private WakeLock mWakeLock;
	
	private PendingIntent mStepAlarm;
	private PendingIntent mSleepAlarm;
	private PendingIntent mPressureAlarm;
	
	private SensorManagerReceiver mSensorManagerReceiver;
	private SensorManager mSensorManager;
	private StepSensorListen mStepSensorListen;
	private SleepSensorListen mSleepSensorListen;
	private PressureSensorListen mPressureSensorListen;
	private ISensorChanger mSensorObserver;
	
	private int mWakeLockFlag = 0;
	private SensorHandler mSensorHandler;
	
	private boolean presseureGetFlag = false;		//if get failure in 3sec,then get after 1min,other after 15min
	
	public SensorManagerUtil(Context context, ISensorChanger observer) {
		this.mContext = context;
		this.mSensorObserver = observer;
		mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
		mPowerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
		mAlarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		
		mSensorManagerReceiver = new SensorManagerReceiver();
		mObserversMap = new HashMap<String, SensorManagerUtil.ISensorChanger>();
		
		Intent firsrAlarm = new Intent(ACTION_ALARM_INIT);
        PendingIntent firstIntent = PendingIntent.getBroadcast(context, 0, firsrAlarm, 0);
        mAlarmManager.set(AlarmManager.RTC_WAKEUP,
        		System.currentTimeMillis() + ALARM_STEP_SLEEP_SHORT, firstIntent);
        
        mStepAlarm = PendingIntent.getBroadcast(mContext, 0, new Intent(ACTION_ALARM_STEP), 0);
        mSleepAlarm = PendingIntent.getBroadcast(mContext, 0, new Intent(ACTION_ALARM_SLEEP), 0);
        mPressureAlarm = PendingIntent.getBroadcast(mContext, 0, new Intent(ACTION_ALARM_PRESSURE), 0);
	}
	
	public void startObserver(){
		mSensorHandler = new SensorHandler();
		
		mSensorManagerReceiver.register(mContext);
		
		Intent firsrAlarm = new Intent(ACTION_ALARM_INIT);
        PendingIntent firstIntent = PendingIntent.getBroadcast(mContext, 0, firsrAlarm, 0);
        mAlarmManager.set(AlarmManager.RTC_WAKEUP,  System.currentTimeMillis() + ALARM_STEP_SLEEP_SHORT, firstIntent);
        LogFile.logCatWithTime("======================================startObserver===");
	}
	public void stopObserver(){
		mSensorManagerReceiver.unRegister(mContext);
		LogFile.logCatWithTime("========================================stopObserver===");
		mObserversMap.clear();
		if(mWakeLock != null && mWakeLock.isHeld()){
			mWakeLock.release();
		}
		stopStepListen(mContext, mSensorObserver);
		stopSleepListen(mContext, mSensorObserver);
		stopPressureListen(mContext, mSensorObserver);
		mSensorHandler.removeCallbacksAndMessages(null);
	}
	
	private void setAlarm(int flagbit){

		switch (flagbit) {
			case WAKE_LOCK_FLAG_STEP:
				if(0 == Settings.System.getInt(mContext.getContentResolver(),
						SETTING_KEY_SPORT_STATE, 0)){
					mAlarmManager.set(AlarmManager.RTC_WAKEUP,
			        		System.currentTimeMillis() + ALARM_STEP_SLEEP, mStepAlarm);
				}else{
					mAlarmManager.set(AlarmManager.RTC_WAKEUP,
			        		System.currentTimeMillis() + ALARM_STEP_SLEEP_SHORT, mStepAlarm);
				}
				break;
			case WAKE_LOCK_FLAG_SLEEP:
				mAlarmManager.set(AlarmManager.RTC_WAKEUP,
		        		System.currentTimeMillis() + ALARM_STEP_SLEEP, mSleepAlarm);			
				break;
			case WAKE_LOCK_FLAG_PRESSURE:
				if(presseureGetFlag){
					mAlarmManager.set(AlarmManager.RTC_WAKEUP,
			        		System.currentTimeMillis() + ALARM_PRESSURE, mPressureAlarm);
				}else{
					mAlarmManager.set(AlarmManager.RTC_WAKEUP,
			        		System.currentTimeMillis() + ALARM_STEP_SLEEP, mPressureAlarm);
				}
				break;
			default:
				break;
		}
	}
	private void setSportAlarm(){
		mAlarmManager.cancel(mStepAlarm);
		mAlarmManager.set(AlarmManager.RTC_WAKEUP,
        		System.currentTimeMillis() + ALARM_STEP_SLEEP_SHORT, mStepAlarm);
	}
	
	public void changeLockFlag(int flagbit, boolean isLock){

		if(isLock){
			mWakeLockFlag |= flagbit;
		}else{
			setAlarm(flagbit);
			mWakeLockFlag = mWakeLockFlag & (~flagbit & Integer.MAX_VALUE);
		}
		//MxyLog.i(TAG, "changeLockFlag22222" + "--flagbit=" + flagbit + "--isLock=" + isLock + "--mWakeLockFlag=" + mWakeLockFlag);
		
		if(mWakeLockFlag > 0){
			if(mWakeLock != null && mWakeLock.isHeld()){
				return;
			}
			//LogFile.logCatWithTime("######## lock awake acquire");
			mWakeLock = mPowerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
					TAG);
			//mWakeLock.acquire();
		}else if(mWakeLockFlag == 0){
			
			if(mWakeLock != null && mWakeLock.isHeld()){
				mWakeLock.release();
				//LogFile.logCatWithTime("######## lock awake release");
			}
			mWakeLock = null;
		}
	}
	
	/**
	 *   step
	 */
	public void startStepListen(Context context, ISensorChanger observer){
		if(mStepSensorListen == null){
			mStepSensorListen = new StepSensorListen();
			mStepSensorListen.startListen(context);
			mObserversMap.put(SENSOR_OBSERVER_STEP, observer);
		}else{
			MxyLog.e(TAG, "startStepListen -- already exits");
		}
	}
	public void stopStepListen(Context context, ISensorChanger observer){
		if(mStepSensorListen != null){
			mStepSensorListen.stopListen();
			mStepSensorListen = null;
			mObserversMap.remove(SENSOR_OBSERVER_STEP);
		}else{
			MxyLog.e(TAG, "stopStepListen -- listen == null");
		}
	}
	/**
	 *   sleep
	 */
	public void startSleepListen(Context context, ISensorChanger observer){
		if(mSleepSensorListen == null){
			mSleepSensorListen = new SleepSensorListen();
			mSleepSensorListen.startListen(context);
			mObserversMap.put(SENSOR_OBSERVER_SLEEP, observer);
		}else{
			MxyLog.e(TAG, "startSleepListen -- already exits");
		}

	}
	public void stopSleepListen(Context context, ISensorChanger observer){
		if(mSleepSensorListen != null){
			mSleepSensorListen.stopListen();
			mSleepSensorListen = null;
			mObserversMap.remove(SENSOR_OBSERVER_SLEEP);
		}else{
			MxyLog.e(TAG, "stopStepListen -- listen == null");
		}
	}
	
	/**
	 *	pressure
	 */
	public void startPressureListen(Context context, ISensorChanger observer){
		
		if(mPressureSensorListen == null){
			mPressureSensorListen = new PressureSensorListen();
			mPressureSensorListen.startListen(context);
			mObserversMap.put(SENSOR_OBSERVER_PRESSURE, observer);
			presseureGetFlag = false;
		}else{
			MxyLog.e(TAG, "startPressureListen -- already exits");
		}
		
	}
	public void stopPressureListen(Context context, ISensorChanger observer){
		if(mPressureSensorListen != null){
			mPressureSensorListen.stopListen();
			mPressureSensorListen = null;
			mObserversMap.remove(SENSOR_OBSERVER_PRESSURE);
		}else{
			MxyLog.e(TAG, "stopStepListen -- listen == null");
		}
	}
	
	private void onStepChanged(int step){
		final ISensorChanger observer = mObserversMap.get(SENSOR_OBSERVER_STEP);
		if(observer != null){
			observer.stepChanged(step);
		}
		MxyLog.i(TAG, "onStepChanged=" + step);
		mSensorHandler.removeMessages(SensorHandler.MSG_CANCLE_STEP);
		stopStepListen(mContext, mSensorObserver);
	}
	
	private void onSleepStateChange(int state){
		final ISensorChanger observer = mObserversMap.get(SENSOR_OBSERVER_SLEEP);
		if(observer != null){
			observer.sleepStateChanged(state);
		}
		MxyLog.i(TAG, "onSleepStateChange=" + state);
		mSensorHandler.removeMessages(SensorHandler.MSG_CANCLE_SLEEP);
		stopSleepListen(mContext, mSensorObserver);
	}
	
	private void onPressureChange(float pressure){
		
		final ISensorChanger observer = mObserversMap.get(SENSOR_OBSERVER_SLEEP);
		if(observer != null){
			observer.pressureChanged(pressure);
		}
		MxyLog.i(TAG, "onPressureChange=" + pressure);
		mSensorHandler.removeMessages(SensorHandler.MSG_CANCLE_PRESSURE);
		presseureGetFlag = true;
		stopPressureListen(mContext, mSensorObserver);
	}
	
	private abstract class absSensorListen implements SensorEventListener{
		public static final int STATE_UNRIGISTER = 0;
		public static final int STATE_REGISTERED = 1;
		
		private int mState = STATE_UNRIGISTER;
		public void startListen(Context context){
			if(getSensor() == null){
				return;
			}
			if(STATE_REGISTERED == mState){
				return;
			}
			mSensorManager.registerListener(this,getSensor(), SensorManager.SENSOR_DELAY_NORMAL);
			mState = STATE_REGISTERED;
			changeLockFlag(getWakeLockFlag(), true);
			mSensorHandler.sendEmptyMessageDelayed(getMsgWhat(), SENSOR_LISTEN_UNREGISTER_DELAY);
			LogFile.logCatWithTime("======startListen===" + getSensor().getType());
			MxyLog.i(TAG, "======startListen===" + getSensor().getType());
		}
		public void stopListen(){
			if(getSensor() == null){
				return;
			}
			if(STATE_UNRIGISTER == mState){
				return;
			}
			mSensorManager.unregisterListener(this);
			mState = STATE_UNRIGISTER;
			changeLockFlag(getWakeLockFlag(), false);
			LogFile.logCatWithTime("======stopListen===" + getSensor().getType());
			MxyLog.i(TAG, "======stopListen===" + getSensor().getType());
		}
		public abstract Sensor getSensor();
		public abstract int getWakeLockFlag();
		public abstract int getMsgWhat();
	}
	
	private class StepSensorListen extends absSensorListen{

		@Override
		public void onSensorChanged(SensorEvent event) {
			// TODO Auto-generated method stub
			switch (event.sensor.getType()) {
				case Sensor.TYPE_STEP_COUNTER:
					final float[] values = event.values;
					onStepChanged((int) values[0]);
					break;
				default:
					break;
			}
		}
		@Override
		public void onAccuracyChanged(Sensor sensor, int accuracy) {
			// TODO Auto-generated method stub
			
		}
		@Override
		public Sensor getSensor() {
			// TODO Auto-generated method stub
			return mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
		}
		@Override
		public int getWakeLockFlag() {
			// TODO Auto-generated method stub
			return WAKE_LOCK_FLAG_STEP;
		}
		@Override
		public int getMsgWhat() {
			// TODO Auto-generated method stub
			return SensorHandler.MSG_CANCLE_STEP;
		}
	}
	
	private class SleepSensorListen extends absSensorListen{

		@Override
		public void onSensorChanged(SensorEvent event) {
			// TODO Auto-generated method stub
			switch (event.sensor.getType()) {
			case 22:
				final float[] values = event.values;
				onSleepStateChange((int) values[1]);
				break;
			default:
				break;
			}
			
		}
		@Override
		public void onAccuracyChanged(Sensor arg0, int arg1) {
			// TODO Auto-generated method stub
			
		}
		@Override
		public Sensor getSensor() {
			// TODO Auto-generated method stub
			return mSensorManager.getDefaultSensor(22);
		}
		@Override
		public int getWakeLockFlag() {
			// TODO Auto-generated method stub
			return WAKE_LOCK_FLAG_SLEEP;
		}
		@Override
		public int getMsgWhat() {
			// TODO Auto-generated method stub
			return SensorHandler.MSG_CANCLE_SLEEP;
		}
	}
	
	private class PressureSensorListen extends absSensorListen{
		
		@Override
		public void onAccuracyChanged(Sensor arg0, int arg1) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onSensorChanged(SensorEvent event) {
			// TODO Auto-generated method stub
			switch (event.sensor.getType()) {
			case Sensor.TYPE_PRESSURE:
				final float[] values = event.values;
				onPressureChange(values[0]);
				break;
			default:
				break;
			}
		}
		@Override
		public Sensor getSensor() {
			// TODO Auto-generated method stub
			return mSensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE);
		}

		@Override
		public int getWakeLockFlag() {
			// TODO Auto-generated method stub
			return WAKE_LOCK_FLAG_PRESSURE;
		}

		@Override
		public int getMsgWhat() {
			// TODO Auto-generated method stub
			return SensorHandler.MSG_CANCLE_PRESSURE;
		}
		
	}
	
	private class SensorHandler extends Handler{
		public static final int MSG_CANCLE_STEP = 0;
		public static final int MSG_CANCLE_SLEEP = 1;
		public static final int MSG_CANCLE_PRESSURE = 2;
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
				case MSG_CANCLE_STEP:
					stopStepListen(mContext, mSensorObserver);
					break;
				case MSG_CANCLE_SLEEP:
					stopSleepListen(mContext, mSensorObserver);
					break;
				case MSG_CANCLE_PRESSURE:
					stopPressureListen(mContext, mSensorObserver);
					break;
				default:
					break;
			}
		}
	}
	
	private class SensorManagerReceiver extends AbsBroadcastReceiver{
		
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			// step and sleep
			final String action = intent.getAction();
			if(ACTION_ALARM_INIT.equals(action)){
				startSleepListen(mContext, mSensorObserver);
				startStepListen(mContext, mSensorObserver);
				startPressureListen(mContext, mSensorObserver);
				return;
			}
			if(ACTION_ALARM_STEP.equals(action)){
				startStepListen(mContext, mSensorObserver);
				return;
			}
			if(ACTION_ALARM_SLEEP.equals(action)){
				startSleepListen(mContext, mSensorObserver);
				return;
			}
			if(ACTION_ALARM_PRESSURE.equals(intent.getAction())){
				startPressureListen(mContext, mSensorObserver);
				return;
			}
			if(ACTION_START_SPORT.equals(intent.getAction())){
				setSportAlarm();
				LogFile.logCat("---start sport----" +"\n");
				return;
			}
			if(ACTION_STOP_SPORT.equals(intent.getAction())){
				LogFile.logCat("---stop sport----" +"\n");
				return;
			}
		}

		@Override
		public IntentFilter getIntentFilter() {
			// TODO Auto-generated method stub
			IntentFilter filter = new IntentFilter();
			filter.addAction(ACTION_ALARM_INIT);
			filter.addAction(ACTION_ALARM_STEP);
			filter.addAction(ACTION_ALARM_SLEEP);
			filter.addAction(ACTION_ALARM_PRESSURE);
			
			filter.addAction(ACTION_START_SPORT);
			filter.addAction(ACTION_STOP_SPORT);
			return filter;
		}
	}
	
	public interface ISensorChanger{
		public void stepChanged(int allStep);
		public void sleepStateChanged(int state);
		public void pressureChanged(float pressure);
	}

}

package com.sczn.wearlauncher.services;

import java.io.FileDescriptor;
import java.util.Calendar;

import com.sczn.wearlauncher.db.DBUtil;
import com.sczn.wearlauncher.db.util.SensorManagerUtil;
import com.sczn.wearlauncher.db.util.SensorManagerUtil.ISensorChanger;
import com.sczn.wearlauncher.util.MxyLog;
import com.sczn.wearlauncher.util.LogFile;
import com.sczn.wearlauncher.util.NotificationUtil;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Message;
import android.os.Parcel;
import android.os.RemoteException;
import android.provider.Settings;
import android.service.notification.NotificationListenerService;
import android.util.Log;

public class SensorService extends Service implements ISensorChanger{
	private static final String TAG = SensorService.class.getSimpleName();
	
	public static final String STEP_CHANGE_ACTION = "broadcast.kct.step";
	public static final String MAIN_ACTION = "com.sczn.action.service.SensorService";
	
	public volatile static DBUtil dataBaseUtil = null;
	private SensorManagerUtil mSensorManagerUtil;
	
	private int lastReadStep = 0;
	private int lastState = -1;
	private long startTime = 0L;
	private int lastStateStep = 0;
	
	private SensorServiceBinder mBinder = new SensorServiceBinder();
	
	public static void startInstance(Context context){
		Intent i = new Intent(MAIN_ACTION); 
		i.setPackage(context.getPackageName());
		context.startService(i);
		//MxyLog.d(TAG, "startInstance");
		//LogFile.logCat("###################################startSensorService" + "\n");
	}
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		
		MxyLog.d(TAG, "onCreate");
		if (null == dataBaseUtil) {
            dataBaseUtil = new DBUtil(this);
        }
		
		mSensorManagerUtil = new SensorManagerUtil(this,this);
		mSensorManagerUtil.startObserver();
		
	}
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		mSensorManagerUtil.stopObserver();
		MxyLog.d(TAG, "onDestroy");
		super.onDestroy();
	}
	
	@Override
	public void onTaskRemoved(Intent rootIntent) {
		// TODO Auto-generated method stub
		MxyLog.d(TAG, "onTaskRemoved");
		super.onTaskRemoved(rootIntent);
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		//return mBinder;
		return null;
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		//return START_STICKY;
		return super.onStartCommand(intent, flags, startId);
	}
	
	private void restoreStepData(int step) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.MILLISECOND,0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MINUTE, 0);
        long currentHour = c.getTimeInMillis();
        if(dataBaseUtil!=null){
        	dataBaseUtil.saveStepCount(step, currentHour);
        }else {
            MxyLog.e(TAG,"restoreStepData  dataBaseUtil = null");
        }
    }

	@Override
	public void stepChanged(int allStep) {
		// TODO Auto-generated method stub

		Log.e("sensor", "allStep:"+allStep);
		
		final int stepSaved = Settings.System.getInt(getContentResolver(), 
				SensorManagerUtil.SETTING_KEY_STEP_CURR, lastReadStep);
		Settings.System.putInt(getContentResolver(), 
				SensorManagerUtil.SETTING_KEY_STEP_CURR, allStep);
		
		int oneTimeStep = allStep - stepSaved;
		lastReadStep = allStep;
		
		boolean stepChanged = false;
		if(oneTimeStep < 0){
			oneTimeStep = allStep;
			Settings.System.putInt(getContentResolver(), 
					SensorManagerUtil.SETTING_KEY_STEP_CURR, allStep);
			Settings.System.putInt(getContentResolver(), 
					SensorManagerUtil.SETTING_KEY_STEP_SLEEP, allStep);
			LogFile.logCat("--------------------------------------------maybe clear" + "\n");
			stepChanged = true;
		}else if(oneTimeStep == 0){
		}else{
			stepChanged = true;
		}
		
		if(stepChanged){
			restoreStepData(oneTimeStep);
			
			Intent stepChangeIntent = new Intent();
			stepChangeIntent.putExtra("step", oneTimeStep);
			stepChangeIntent.setAction(STEP_CHANGE_ACTION);
			sendBroadcast(stepChangeIntent);
		}
		
		LogFile.logCatWithTime("--------------------------------------------getStep="  + allStep);

		//mSensorManagerUtil.stopStepListen(this, this);
	
	}

	/**
	 * state
	 * 		0:deep sleep
	 * 		1:sleep
	 * 		2:rest
	 * 		3:walk
	 * 		4:run
	 * 		5:stop
	 */
	@Override
	public void sleepStateChanged(int state) {
		// TODO Auto-generated method stub
		Calendar calendar = Calendar.getInstance();
		long time = calendar.getTimeInMillis();
		//first in
		if(lastState == -1){
			lastState = state;
			startTime = time;
			lastStateStep = lastReadStep;
			//return;
		}
		calendar.set(Calendar.MILLISECOND, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.add(Calendar.HOUR, 9);
		long amNine = calendar.getTimeInMillis();
		calendar.add(Calendar.HOUR, 12);
		long pmNine = calendar.getTimeInMillis();
		
		if(time > amNine && time < pmNine){
			 if(state == 0 || state == 1){
				 state = 2;
			 }
		}
		if(lastState != state){
			long endTime = time;
			
			if(lastState == 3 || lastState == 4){
				
				final int stepSaved = Settings.System.getInt(getContentResolver(), 
						SensorManagerUtil.SETTING_KEY_STEP_CURR, lastReadStep);
				final int sleepStepSaved = Settings.System.getInt(getContentResolver(), 
						SensorManagerUtil.SETTING_KEY_STEP_CURR, lastStateStep);
				
				int saveStep = stepSaved - sleepStepSaved;
				
				dataBaseUtil.insertSleepTime(startTime, endTime, lastState,saveStep);
			}else{
				dataBaseUtil.insertSleepTime(startTime, endTime, lastState,0);
			}
			startTime = time;
			lastState = state;
			lastStateStep = lastReadStep;
			Settings.System.putInt(getContentResolver(), 
					SensorManagerUtil.SETTING_KEY_STEP_CURR, lastStateStep);
		}
		
		LogFile.logCatWithTime("--------------------------------------------getSleep:state=" + state);
		
		//mSensorManagerUtil.stopSleepListen(this, this);
		Log.e("sensor", "sleepState:"+state);
	}
	
	@Override
	public void pressureChanged(float pressure) {
		// TODO Auto-generated method stub
		if(pressure > 0){
			long time = Calendar.getInstance().getTimeInMillis();
			dataBaseUtil.insertPressure(time, pressure);
			LogFile.logCatWithTime("--------------------------------------------getPressure:pressure=" + pressure);
			//mSensorManagerUtil.stopPressureListen(this, this);
		}
	}
	
	public class SensorServiceBinder extends Binder{
		public SensorService getService(){
			return SensorService.this;
		}
	}

}

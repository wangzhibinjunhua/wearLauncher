package com.sczn.wearlauncher.services;


import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.sczn.wearlauncher.card.healthalarm.UtilHealthAlarm;
import com.sczn.wearlauncher.db.DBUtil;
import com.sczn.wearlauncher.db.util.SensorManagerUtil;
import com.sczn.wearlauncher.db.util.SportsUtil;
import com.sczn.wearlauncher.db.util.SensorManagerUtil.ISensorChanger;
import com.sczn.wearlauncher.receivers.AbsBroadcastReceiver;
import com.sczn.wearlauncher.services.SensorService.SensorServiceBinder;
import com.sczn.wearlauncher.util.MxyLog;
import com.sczn.wearlauncher.util.MxyToast;
import com.sczn.wearlauncher.util.LogFile;
import com.sczn.wearlauncher.util.NotificationUtil;
import com.sczn.wearlauncher.util.RaiseScreenUtil;
import com.sczn.wearlauncher.util.SharedPreferencesUtils;
import com.sczn.wearlauncher.util.SysServices;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.database.ContentObserver;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.provider.Settings;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

public class MainService extends NotificationListenerService{

	private final static String TAG = MainService.class.getSimpleName();
	
	public static final String EXTRA_NAME_NOTIFICATION = "notification";
	
	public static void startInstance(Context context){
		Intent i = new Intent(NotificationListenerService.SERVICE_INTERFACE); 
		i.setPackage(context.getPackageName());
		i.putExtra(EXTRA_NAME_NOTIFICATION, NotificationUtil.getInstance());
		context.startService(i);
	}
	
	private NotificationUtil mNotificationUtil;
	private LauncherServiceReceiver mLauncherServiceReceiver;
	private UtilHealthAlarm mUtilHealthAlarm;
	private RaiseScreenUtil mRaiseScreenUtil;
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		MxyLog.d(TAG, "onCreate--" + Calendar.getInstance().getTime());
		super.onCreate();
		
		SensorService.startInstance(getApplicationContext());

		mLauncherServiceReceiver = new LauncherServiceReceiver();
		mLauncherServiceReceiver.register(this);
		
		mRaiseScreenUtil = RaiseScreenUtil.getInstance();
		mRaiseScreenUtil.startMgr();
		
		mUtilHealthAlarm = UtilHealthAlarm.getInstance();
		mUtilHealthAlarm.startMgr(this);
		
		mNotificationUtil = NotificationUtil.getInstance();
		mNotificationUtil.registerReceiver(this);
	}
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		//MxyLog.d(TAG, "onDestroy");
		mLauncherServiceReceiver.unRegister(this);
		
		mRaiseScreenUtil.stopMgr();
		
		mUtilHealthAlarm.stopMgr(this);
		
		mNotificationUtil.unRegisterReceiver(this);
		
		MxyLog.d(TAG, "onDestroy");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SharedPreferencesUtils.setParam(this, sdf.format(Calendar.getInstance().getTime()), "service destroy");
		super.onDestroy();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		MxyLog.d(TAG, "onStartCommand");
		return START_STICKY;
	}
	
	@Override
	public void onNotificationPosted(StatusBarNotification sbn) {
		// TODO Auto-generated method stub
		//showToast("onNotificationPosted" + "--getPackageName=" + sbn.getPackageName() + "--tickerText=" + sbn.getNotification().tickerText);
		if(sbn != null){
			//NotificationUtil.getInstance().addWatchNotification(sbn);
		}
	}

	@Override
	public void onNotificationRemoved(StatusBarNotification sbn) {
		// TODO Auto-generated method stub
		//showToast("onNotificationRemoved" + "--getPackageName=" + sbn.getPackageName() + "--tickerText=" + sbn.getNotification().tickerText);
		if(sbn != null){
			//NotificationUtil.getInstance().removeWatchNotification(sbn);
		}
	}

	private void showToast(final String message){
		new Handler(Looper.getMainLooper()).post(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				MxyToast.showShort(getApplicationContext(), message);
			}
		});
	}
	
	private void checkSensorServices(){
		
		/*
		ActivityManager manager = (ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);   
	    for (RunningServiceInfo service :manager.getRunningServices(Integer.MAX_VALUE)){ 
	    	MxyLog.i(TAG, "service.service.getClassName()=" + service.service.getClassName() + "--service.started=" + service.started);
		    if("com.sczn.wearlauncher.services.SensorService".equals(service.service.getClassName())){   
		    	return; 
		    }   
	    }*/
	    SensorService.startInstance(getApplicationContext());
	}
	
	private class LauncherServiceReceiver extends AbsBroadcastReceiver {
		
		public static final String SETTING_KEY_WIFI_STATE = "sczn_wifi_state";
		public static final String SETTING_KEY_WIFI_OFF_BY_SCREEN = "destroy_screen_without_internet";
		
		@Override
		public IntentFilter getIntentFilter() {
			// TODO Auto-generated method stub
			IntentFilter filter = new IntentFilter();
			filter.addAction(Intent.ACTION_SCREEN_ON);
			filter.addAction(Intent.ACTION_SCREEN_OFF);
			
			filter.addAction(Intent.ACTION_TIME_TICK);
			return filter;
		}
		
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			String action = intent.getAction();
			//MxyLog.d(TAG, "ScreenStateReceiver--" + action + "--" + Calendar.getInstance().getTime());
			
			if(action.equals(Intent.ACTION_TIME_TICK)){
				checkSensorServices();
				return;
			}
			
	        if(action.equals(Intent.ACTION_SCREEN_OFF)){
	        	//stopSensorListener();
	            storeWifiState(context);
	        }else if(action.equals(Intent.ACTION_SCREEN_ON)){
	        	//openSensorHandle.sendEmptyMessageDelayed(0, 5 * 1000);
	            restoreWifiState(context);
	        }
		}

		private void storeWifiState(Context context){
	        if(0 == (Settings.System.getInt(context.getContentResolver(), SETTING_KEY_WIFI_OFF_BY_SCREEN ,0))){
	            return;
	        }
	        final WifiManager wm = SysServices.getWfMgr(context);
	       // MxyLog.d(this, "storeWifiState--" + wm.getWifiState());
	        switch (wm.getWifiState()) {
	            case WifiManager.WIFI_STATE_ENABLED:
	            case WifiManager.WIFI_STATE_ENABLING:
	                Settings.System.putInt(context.getContentResolver(),SETTING_KEY_WIFI_STATE,WifiManager.WIFI_STATE_ENABLED);
	                wm.setWifiEnabled(false);
	                break;
	            case WifiManager.WIFI_STATE_DISABLING:
	            case WifiManager.WIFI_STATE_DISABLED:
	            default:
	                Settings.System.putInt(context.getContentResolver(),SETTING_KEY_WIFI_STATE,WifiManager.WIFI_STATE_DISABLED);
	                break;
	        }
	       // MxyLog.d(TAG, "storeWifiState=" + Settings.System.getInt(context.getContentResolver(), SETTING_KEY_WIFI_OFF_BY_SCREEN ,0) );
	    }
	    private void restoreWifiState(Context context){
	    	//MxyLog.d(TAG, "restoreWifiState=" + Settings.System.getInt(context.getContentResolver(), SETTING_KEY_WIFI_OFF_BY_SCREEN ,0) );
	        if(0 == (Settings.System.getInt(context.getContentResolver(), SETTING_KEY_WIFI_OFF_BY_SCREEN ,0))){
	            return;
	        }
	        if( WifiManager.WIFI_STATE_ENABLED == Settings.System.getInt(context.getContentResolver()
	                ,SETTING_KEY_WIFI_STATE,WifiManager.WIFI_STATE_DISABLED)){
	        	SysServices.getWfMgr(context).setWifiEnabled(true);
	        }
	    }
	}	
	

	private class SensorServiceConn implements ServiceConnection{
		
		private SensorService.SensorServiceBinder mBinder;
		private SensorService mSensorService;
		
		@Override
		public void onServiceDisconnected(ComponentName name) {
			// TODO Auto-generated method stub
			if(mSensorService != null){
				mSensorService.stopSelf();
			}
			SensorService.startInstance(MainService.this);
		}
		
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			// TODO Auto-generated method stub
			mBinder = (SensorServiceBinder) service;
			if(mBinder != null){
				mSensorService = mBinder.getService();
			}
		}
	}
}

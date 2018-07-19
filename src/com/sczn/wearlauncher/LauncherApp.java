package com.sczn.wearlauncher;

import com.sczn.wearlauncher.services.MainService;
import com.sczn.wearlauncher.services.SensorService;
import com.sczn.wearlauncher.util.ClockSkinUtil;
import com.sczn.wearlauncher.util.MxyLog;
import com.sczn.wearlauncher.util.SysServices;

import android.app.Application;
import android.content.Context;
import android.graphics.Typeface;

public class LauncherApp extends Application {
	private static final String TAG = LauncherApp.class.getSimpleName();
	
	public static Context appContext;
	public static Typeface lanTingBoldBlackTypeface = null;
	public static Typeface cardFragmentTypeface = null;

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		appContext = getApplicationContext();
		
		appInit();
		
		//LauncherCrashHandler.getInstance().init(appContext);
		
		lanTingBoldBlackTypeface = Typeface.createFromAsset(getAssets(),"fonts/LanTingBoldBlack.TTF");
		cardFragmentTypeface = Typeface.createFromAsset(getAssets(),"fonts/DINCond-Medium.otf");
		
		MainService.startInstance(appContext);

		MxyLog.i(TAG, "density=" + getResources().getDisplayMetrics().density);
		MxyLog.i(TAG, "scaledDensity=" + getResources().getDisplayMetrics().scaledDensity);
		MxyLog.i(TAG, "widthPixels=" + getResources().getDisplayMetrics().widthPixels);
		MxyLog.i(TAG, "densityDpi=" + getResources().getDisplayMetrics().densityDpi);
	}


	private void appInit() {
		// TODO Auto-generated method stub
		// for remount connect
		SysServices.setSystemSettingString(appContext, "type_dmm", "Y");
		SysServices.setSystemSettingString(appContext, "type_uri", "com.sczn.wearlauncher.db.provider");
		ClockSkinUtil.initAllClockIndex();
	}
	
}

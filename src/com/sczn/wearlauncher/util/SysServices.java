package com.sczn.wearlauncher.util;

import android.app.NotificationManager;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Environment;
import android.provider.Settings;
import android.provider.Settings.SettingNotFoundException;
import android.telephony.TelephonyManager;

public class SysServices {
	
	public static final String TAG = "SysServices";
	
	/**************PackageManager****************/
	public static PackageManager getPkMgr(Context context){
		return context.getPackageManager();
	}
	
	public static Drawable getAppIcon(Context context,String packageName){
		return context.getApplicationInfo().loadIcon(getPkMgr(context));
	}
	
	
	/**************TelephonyManager****************/
	public static TelephonyManager getTlMgr(Context context){
		return (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
	}
	
	public static int getSimState(Context context){
		return getTlMgr(context).getSimState();
	}
	
	
	/******************WifiManager**************************/
	public static WifiManager getWfMgr(Context context){
		return (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
	}
	
	public static WifiInfo getWifiInfo(Context context){
		return getWfMgr(context).getConnectionInfo();
	}
	
	
	/******************Bluetooth**************************/
	public static BluetoothAdapter getBtAdapter(Context context){
		final BluetoothManager bm = (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
		if(bm == null) return null;
		return bm.getAdapter();
	}
	
	
	/***********************Storage************************/
	public static String getStoragePath(){
		return Environment.getExternalStorageDirectory().toString();
	}
	
	/***********************AudioManager************************/
	public static AudioManager getaudioMgr(Context context){
		return (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
	}
	
	public static int getRingMode(Context context){
		if(getaudioMgr(context) != null){
			return getaudioMgr(context).getRingerMode();
		}
		return -1;
	}
	
	/***********************AudioManager************************/
	public static NotificationManager getNtfMgr(Context context){
		return (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
	}
	
	/***********************Settings.System************************/
	public static int getSystemSettingInt(Context context, String name){

		try {
			return Settings.System.getInt(context.getContentResolver(),name);
		} catch (SettingNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			MxyLog.e(TAG, "context=" + context.toString() + "--e=" + e.toString());
		}	
		return Integer.MIN_VALUE;
	}
	public static int getSystemSettingInt(Context context, String name,int defalutValue){
		return Settings.System.getInt(context.getContentResolver(),name,defalutValue);
	}
	public static void setSystemSettingInt(Context context, String name,int value){
		Settings.System.putInt(context.getContentResolver(), name, value);
	}
	
	public static void setSystemSettingString(Context context, String name, String value){
		Settings.System.putString(context.getContentResolver(), name, value);
	}
	public static String getSystemSettingString(Context context, String name){
		return Settings.System.getString(context.getContentResolver(), name);
	}
	public static String getSystemSettingString(Context context, String name,String defaultValue){
		if(Settings.System.getString(context.getContentResolver(), name) == null){
			return defaultValue;
		}
		return Settings.System.getString(context.getContentResolver(), name);
	}
}

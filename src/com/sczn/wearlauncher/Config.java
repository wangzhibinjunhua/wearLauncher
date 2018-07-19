package com.sczn.wearlauncher;

import com.sczn.wearlauncher.R;

import android.content.Context;

public class Config {
	public static final boolean IS_DEBUG = true;
	
	public static boolean isBleVersion(Context context){
		return context.getResources().getBoolean(R.bool.ble_version);
	}
	
	public static final String DEAFULT_CITY = "shenzhen";
}

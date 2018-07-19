package com.sczn.wearlauncher.util;

import android.util.Log;

public class MxyLog {

	public static final boolean DEBUG = true;
	
	public static final String TAG = "mxy";
	
	public static void i(Object object,String string){
		i(object.getClass().getSimpleName(), string);
	}
	public static void e(Object object,String string){
		e(object.getClass().getSimpleName(), string);
	}
	public static void d(Object object,String string){
		d(object.getClass().getSimpleName(), string);
	}
	
	public static void d(String tag,String string){
		if(isDebug(tag)){
			d(tag + "--" + string);
		}
	}
	
	public static void i(String tag,String string){
		if(isDebug(tag)){
			i(tag + "--" + string);
		}
	}
	
	public static void e(String tag,String string){
		//if(isDebug(tag)){
			e(tag + "--" + string);
		//}
	}
	
	private static void i(String info){
		Log.i(TAG, info);
	}
	private static void d(String info){
		Log.d(TAG, info);
	}
	private static void e(String info){
		Log.e(TAG, info);
	}
	
	private static boolean isDebug(String tag){
		if(!DEBUG){
			return false;
		}
		
		if("dmm".equals(tag)){
			return false;
		}

		return true;
	}
	
}

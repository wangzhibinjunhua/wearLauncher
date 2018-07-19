package com.sczn.wearlauncher.util;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

public class MxyToast {
	
	private static final int DUARING_SHORT = 1000;

	private static Toast mToast;
	private static Toast mViewToast;

	public static void showShort(Context context,String string){
		show(context,string,DUARING_SHORT);
	}
	
	public static void show(Context context,String string,int duaring){
		if(context == null || string == null){
			return;
		}
		if(mToast == null){
			mToast = Toast.makeText(context.getApplicationContext(), string, duaring);
		}else{
			mToast.setText(string);
			mToast.setDuration(duaring);
		}
		mToast.show();
	}
	
	public static void showShort(Context context,int stringId){
		show(context,stringId,DUARING_SHORT);
	}
	
	public static void show(Context context,int stringId,int duaring){
		if(context == null){
			return;
		}
		if(mToast == null){
			mToast = Toast.makeText(context.getApplicationContext(), stringId, duaring);
		}else{
			mToast.setText(stringId);
			mToast.setDuration(duaring);
		}
		mToast.show();
	}
	
	public static void showView(Context context, View view){
		if(context == null){
			return;
		}
		if(mViewToast == null){
			mViewToast = new Toast(context);
		}
		mViewToast.setView(view);
		mViewToast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
		mViewToast.setDuration(DUARING_SHORT);
		mViewToast.show();
	}
}

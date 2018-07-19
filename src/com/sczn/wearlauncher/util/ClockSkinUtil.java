package com.sczn.wearlauncher.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import com.sczn.wearlauncher.LauncherApp;
import com.sczn.wearlauncher.R;
import com.sczn.wearlauncher.model.ClockSkin;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.provider.Settings;

public class ClockSkinUtil {
	private static final String TAG = ClockSkinUtil.class.getSimpleName();
	
	private static final String CLOCK_ROOT = "ClockSkin";
	private static final String CLOCK_XML_NAME = "clock_skin.xml";
	private static final String CLOCK_MODEL_NAME = "clock_skin_model.png";
	
	public static final int CLOCK_SKIN_STATE_UNREADY = 0;
	public static final int CLOCK_SKIN_STATE_READYING = 1;
	public static final int CLOCK_SKIN_STATE_READY = 2;
	
	public static String[] getAllClockSkins(Context context){
		try {
			return context.getAssets().list(CLOCK_ROOT);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			MxyLog.e(TAG, "getClockSkins error=" + e.toString());
		}
		return null;
	}
	
	public static void initAllClockIndex(){
		new ClockIndexLoader().execute();
	}
	
	public static String getClockSkinByPosition(Context context, int position){
		try {
			String[] files = context.getAssets().list(CLOCK_ROOT);
			if(files != null && files.length > position){
				return files[position];
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			MxyLog.e(TAG, "getClockSkins error=" + e.toString());
		}
		return null;
	}
	
	public static Drawable getClockSkinModelByName(Context context, String skinName){

		InputStream in;
		Bitmap bitmap = null;

			try {
				in = context.getAssets().open(getClockSkinModelFile(skinName));
				bitmap = BitmapFactory.decodeStream(in);
				in.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				MxyLog.e(TAG, "getDrawable" + "--IOException=" + e.toString());
			}
		if(bitmap == null){
			return context.getResources().getDrawable(R.drawable.alt_bg);
		}
		return new BitmapDrawable(context.getResources(),bitmap);
	
	}
	
	public static String getClockSkinXmlFile(String skinName){
    	return CLOCK_ROOT + File.separator + skinName + File.separator + CLOCK_XML_NAME;
    }
	public static String getClockSkinDetail(String skinName, String pngName){
		return CLOCK_ROOT + File.separator + skinName + File.separator + pngName;
	}
	private static String getClockSkinModelFile(String skinName){
    	return CLOCK_ROOT + File.separator + skinName + File.separator + CLOCK_MODEL_NAME;
    }
	
	private static class ClockIndexLoader extends AsyncTask<Void, Void, String>{

		@Override
		protected String doInBackground(Void... params) {
			// TODO Auto-generated method stub
			if(LauncherApp.appContext != null){
				try {
					final String[] clocks = getAllClockSkins(LauncherApp.appContext);
					final StringBuilder indexString = new StringBuilder();
					for(String clock: clocks){
						final String[] indexs = clock.split("_");
						if(indexs.length == 2){
							indexString.append(indexs[1] + "#");
						}else{
							indexString.append(indexs[0] + "#");
						}
					}
					if(indexString.length() > 1){
						return indexString.substring(0, indexString.length() - 1);
					}
				} catch (Exception e) {
					// TODO: handle exception
					cancel(true);
				}
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			MxyLog.d(TAG, "ClockIndexLoader--result=" + result);
			super.onPostExecute(result);
			if(!isCancelled() && result != null && LauncherApp.appContext != null){
				Settings.System.putString(LauncherApp.appContext.getContentResolver(),
						"clock_index", result);
			}
		}

		
	}
	
}

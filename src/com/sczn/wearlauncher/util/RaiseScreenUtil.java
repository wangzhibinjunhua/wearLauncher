package com.sczn.wearlauncher.util;

import java.util.ArrayList;

import com.sczn.wearlauncher.LauncherApp;

import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.provider.Settings;

public class RaiseScreenUtil {
	public static final String SETTING_KEY_RAISE_SCREEN = "double_hand_bright_screen";
	
	private static RaiseScreenUtil instance;
	
	public static RaiseScreenUtil getInstance(){
		if(instance == null){
			synchronized (RaiseScreenUtil.class) {
				instance = new RaiseScreenUtil();
			}
		}
		return instance;
	}
	private RaiseScreenUtil(){
		mRaiseScreenObserver = new RaiseScreenObserver(null);
	}
	
	private RaiseScreenObserver mRaiseScreenObserver;
	private ArrayList<IRaiseScreenState> mListens;
	
	public void startMgr(){
		freshState();
		mRaiseScreenObserver.register();
	}
	public void stopMgr(){
		mRaiseScreenObserver.unRegister();
	}
	
	public void addListen(IRaiseScreenState listen){
		if(mListens == null){
			mListens = new ArrayList<RaiseScreenUtil.IRaiseScreenState>();
		}
		//MxyLog.d(this, "addListen" + "--listen=" + listen);
		if(!mListens.contains(listen)){
			mListens.add(listen);
		}
	}
	
	public void deldteListen(IRaiseScreenState listen){
		if(mListens == null){
			return;
		}
		//MxyLog.d(this, "deldteListen" + "--listen=" + listen);
		if(mListens.contains(listen)){
			mListens.remove(listen);
		}
	}
	
	public boolean getState(){
		return 1 == Settings.System.getInt(LauncherApp.appContext.getContentResolver(),
				SETTING_KEY_RAISE_SCREEN,0);
	}
	
	public void switchState(){
		if(0 == Settings.System.getInt(LauncherApp.appContext.getContentResolver(),
				SETTING_KEY_RAISE_SCREEN,0)){
			Settings.System.putInt(LauncherApp.appContext.getContentResolver(),
					SETTING_KEY_RAISE_SCREEN,1);
		}else{
			Settings.System.putInt(LauncherApp.appContext.getContentResolver(),
					SETTING_KEY_RAISE_SCREEN,0);
		}
	}
	
	public void freshState(){
		if(0 == Settings.System.getInt(LauncherApp.appContext.getContentResolver(),
				SETTING_KEY_RAISE_SCREEN,0)){
			notifiListen(false);
		}else{
			notifiListen(true);
		}
	}

	
	private void notifiListen(boolean isOpen){
		if(mListens == null){
			return;
		}
		for(IRaiseScreenState listen : mListens){
			listen.onRaiseScreenState(isOpen);
		}
	}
	
	private class  RaiseScreenObserver extends ContentObserver {
        private final Uri ScreenOnByHand;

        public  RaiseScreenObserver(Handler handler) {
            super(handler);
            // TODO Auto-generated constructor stub
            ScreenOnByHand = Settings.System.getUriFor(SETTING_KEY_RAISE_SCREEN);
        }

        public void register() {
        	LauncherApp.appContext.getContentResolver().registerContentObserver(ScreenOnByHand, true, this);
        }

        public void unRegister() {
        	LauncherApp.appContext.getContentResolver().unregisterContentObserver(this);
        }

        @Override
        public void onChange(boolean selfChange, Uri uri) {
            // TODO Auto-generated method stub

            //MxyLog.d(TAG, "onChange uri=" + uri);
            if (ScreenOnByHand.equals(uri)) {
            	freshState();
                return;
            }
        }
    }
	
	public interface IRaiseScreenState{
		public void onRaiseScreenState(boolean isopen);
	}
}

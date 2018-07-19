package com.sczn.wearlauncher.util;

import android.app.Notification.Action;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.sczn.wearlauncher.receivers.AbsBroadcastReceiver;

public class StatusUtil {
	private static class Hold{
		private static StatusUtil instance;
	}
	
	public static StatusUtil getInstance(){
		return Hold.instance;
	}
	
	private IModeListen mModeListen;
	
	private class StatusReceiver extends AbsBroadcastReceiver{
		public static final String RINGER_MODE_CHANGED_ACTION = "android.media.RINGER_MODE_CHANGED";
		@Override
		public IntentFilter getIntentFilter() {
			// TODO Auto-generated method stub
			final IntentFilter filter = new IntentFilter();
			
			filter.addAction(RINGER_MODE_CHANGED_ACTION);
			
			return null;
		}

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			final String action = intent.getAction();
			if(RINGER_MODE_CHANGED_ACTION.equals(action)){
				if(mModeListen != null){
					mModeListen.onModeChange();
				}
			}
		}
		
	}
	
	public interface IModeListen{ public void onModeChange(); }
}

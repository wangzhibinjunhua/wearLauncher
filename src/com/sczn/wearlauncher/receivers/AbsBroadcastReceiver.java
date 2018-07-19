package com.sczn.wearlauncher.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;

public abstract class AbsBroadcastReceiver extends BroadcastReceiver {

	private boolean registerFlag = false;
	public void register(Context context){
		if(!registerFlag){
			context.registerReceiver(this, getIntentFilter());
			registerFlag = true;
		}
	}
	public void unRegister(Context context){
		if(registerFlag){
			context.unregisterReceiver(this);
			registerFlag = false;
			
		}
	}

	public abstract IntentFilter getIntentFilter();
}

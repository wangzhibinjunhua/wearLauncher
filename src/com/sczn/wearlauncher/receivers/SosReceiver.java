package com.sczn.wearlauncher.receivers;

import com.sczn.wearlauncher.R;
import com.sczn.wearlauncher.util.MxyToast;
import com.sczn.wearlauncher.util.SharedPreferencesUtils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class SosReceiver extends BroadcastReceiver {
    public static final String SOS_ACTION_SETNUM = "com.action.broadcast.setsos";
    public static final String SOS_KEY = "sosc";
    public static final String SOS_NUM_KEY_ONE = "phoneNum1";
    public static final String SOS_NUM_KEY_TOW = "phoneNum2";
    public static final String SOS_NUM_KEY_THREE = "phoneNum3";
    
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if(action.equals(SOS_ACTION_SETNUM)){
        	saveSosNumber(context, intent.getStringExtra(SOS_KEY));
        	return;
        }
    }
    
    private void saveSosNumber(Context context, String number){
    	if(number == null){
    		return;
    	}
    	String strs[];
		try {
			strs = number.split("w50253d");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
		
		if(strs.length >= 1){
			SharedPreferencesUtils.setParam(context,SOS_NUM_KEY_ONE,strs[0]);
			if(strs.length >= 2){
				SharedPreferencesUtils.setParam(context,SOS_NUM_KEY_TOW,strs[1]);
				if(strs.length >= 3){
					SharedPreferencesUtils.setParam(context,SOS_NUM_KEY_THREE,strs[2]);
				}
			}
			MxyToast.showShort(context, R.string.sos_number_set_success);
		}
    }
}

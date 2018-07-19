package com.sczn.wearlauncher.sensor;

import com.sczn.wearlauncher.util.MxyLog;
import com.sczn.wearlauncher.util.LogFile;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public abstract class LooperSensorListen implements SensorEventListener {

	public static final int STATE_UNRIGISTER = 0;
	public static final int STATE_REGISTERED = 1;
	
	private int mState = STATE_UNRIGISTER;
	public void startListen(SensorManager sm){
		if(STATE_REGISTERED == mState){
			return;
		}
		sm.registerListener(this,getSensor(), getRateUs());
		mState = STATE_REGISTERED;
	}
	public void stopListen(SensorManager sm){
		if(STATE_UNRIGISTER == mState){
			return;
		}
		sm.unregisterListener(this);
		mState = STATE_UNRIGISTER;
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub

	}
	
	public int getRateUs(){
		return SensorManager.SENSOR_DELAY_NORMAL;
	}

	public abstract Sensor getSensor();
	public abstract int getWakeLockFlag();
	public abstract int getMsgWhat();
}

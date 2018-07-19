package com.sczn.wearlauncher.receivers;

import com.sczn.wearlauncher.util.MxyLog;
import com.sczn.wearlauncher.util.MxyToast;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BtReceiver extends BroadcastReceiver {
	private static final String TAG = BtReceiver.class.getSimpleName();
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		if(BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED.equals(intent.getAction())){
			//MxyLog.d(TAG, "ACTION_CONNECTION_STATE_CHANGED");
			onConnectionStateChanged(context, intent);
			return;
		}
		if(BluetoothAdapter.ACTION_STATE_CHANGED.equals(intent.getAction())){
			//MxyLog.d(TAG, "ACTION_STATE_CHANGED");
		}
	}

	private void onConnectionStateChanged(Context context, Intent intent) {
		// TODO Auto-generated method stub
		final int currState = intent.getIntExtra(BluetoothAdapter.EXTRA_CONNECTION_STATE, 
				BluetoothAdapter.STATE_DISCONNECTED);
		final int previousState = intent.getIntExtra(BluetoothAdapter.EXTRA_PREVIOUS_CONNECTION_STATE, 
				BluetoothAdapter.STATE_DISCONNECTED);
		final String address = intent.getStringExtra(BluetoothDevice.EXTRA_DEVICE);
		
		MxyLog.d(TAG, "currState=" + currState + "--previousState=" + previousState + "--");
		if(BluetoothAdapter.STATE_CONNECTED == currState){

		}else if(BluetoothAdapter.STATE_DISCONNECTED== currState){

		}
	}

}

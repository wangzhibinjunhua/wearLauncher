package com.sczn.wearlauncher.view.status;

import com.sczn.wearlauncher.R;
import com.sczn.wearlauncher.receivers.AbsBroadcastReceiver;
import com.sczn.wearlauncher.util.SysServices;
import com.sczn.wearlauncher.view.ClickIcon;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.View;

public class WifiIcon extends StatusIconWithText {
	private static final String TAG = WifiIcon.class.getSimpleName();
	
	public static final int WIFI_STATE_OFF = 0;
	public static final int WIFI_STATE_NO_CONNECTION = 1;
	public static final int WIFI_STATE_CONNECTED = 2;
	
	public static final int WIFI_SIGNAL_UNKNOW = -1;
	public static final int WIFI_SIGNAL_0 = 0;
	public static final int WIFI_SIGNAL_1 = 1;
	public static final int WIFI_SIGNAL_2 = 2;
	public static final int WIFI_SIGNAL_3 = 3;
	
	//public static final int IMAGE_OFF = R.drawable.statu_icon_wifi_off;
	//public static final int IMAGE_0 = R.drawable.statu_icon_wifi_0;
	//public static final int IMAGE_1 = R.drawable.statu_icon_wifi_1;
	//public static final int IMAGE_2 = R.drawable.statu_icon_wifi_2;
	//public static final int IMAGE_3 = R.drawable.statu_icon_wifi_3;

	private WifiReceiver mWifiReceiver;
	private int wifiState = WIFI_STATE_OFF;
	private int wifiSignal = WIFI_SIGNAL_UNKNOW;
	
	public WifiIcon(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		mWifiReceiver = new WifiReceiver();
		mIcon.setOnClickListener(onWifiClick);
	}
	
	@Override
	public void startFresh() {
		// TODO Auto-generated method stub

		super.startFresh();
		mWifiReceiver.register(getContext());
		initView();
	}
	@Override
	public void stopFresh() {
		// TODO Auto-generated method stub
		mWifiReceiver.unRegister(getContext());
		super.stopFresh();
	}

	private void initView() {
		// TODO Auto-generated method stub
		final WifiManager manager = SysServices.getWfMgr(getContext());
		if(manager == null){
			setWifiState(WIFI_STATE_OFF);
			return;
		}
		switch (manager.getWifiState()) {
			case WifiManager.WIFI_STATE_ENABLED:
				setWifiState(WIFI_STATE_CONNECTED);
				setWifiSignal();
				setEnabled(true);
				break;
			case WifiManager.WIFI_STATE_DISABLING:
			case WifiManager.WIFI_STATE_ENABLING:
				setWifiState(WIFI_STATE_NO_CONNECTION);
				setEnabled(false);
				break;
			case WifiManager.WIFI_STATE_DISABLED:
			default:
				setWifiState(WIFI_STATE_OFF);
				setEnabled(true);
				break;
		}
		
	}
	
	@Override
	protected void onAttachedToWindow() {
		// TODO Auto-generated method stub
		super.onAttachedToWindow();
		//mWifiReceiver.register(getContext());
		initView();
	}
	
	@Override
	protected void onDetachedFromWindow() {
		// TODO Auto-generated method stub
		//mWifiReceiver.unRegister(getContext());
		super.onDetachedFromWindow();
	}
	
	private void setWifiState(int state){
		wifiState = state;
		switch (wifiState) {
			case WIFI_STATE_OFF:
				mIcon.setImageResource(R.drawable.statu_icon_wifi_off);
				break;
			case WIFI_STATE_NO_CONNECTION:
				mIcon.setImageResource(R.drawable.statu_icon_wifi_0);
				break;
			default:
				break;
		}
	}
	
	private void setWifiSignal(){
		final WifiInfo info = SysServices.getWifiInfo(getContext());
		
		if(info == null){
			setWifiState(WIFI_STATE_NO_CONNECTION);
			return;
		}

		if(info.getBSSID() != null){
			wifiSignal = WifiManager.calculateSignalLevel(info.getRssi(), 4);
			switch (wifiSignal) {
				case WIFI_SIGNAL_3:
					mIcon.setImageResource(R.drawable.statu_icon_wifi_4);
					break;
				case WIFI_SIGNAL_2:
					mIcon.setImageResource(R.drawable.statu_icon_wifi_3);
					break;
				case WIFI_SIGNAL_1:
					mIcon.setImageResource(R.drawable.statu_icon_wifi_2);
					break;
				case WIFI_SIGNAL_0:
					mIcon.setImageResource(R.drawable.statu_icon_wifi_1);
				default:
					//wifiSignal = WIFI_SIGNAL_0;
					mIcon.setImageResource(R.drawable.statu_icon_wifi_0);
					break;
			}
		}
	}
	
	private OnClickListener onWifiClick = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			final WifiManager manager = SysServices.getWfMgr(getContext());
			//MxyLog.i(TAG, "onWifiClick manager.getWifiState()=" + manager.getWifiState());
			switch (manager.getWifiState()) {
				case WifiManager.WIFI_STATE_ENABLED:
					manager.setWifiEnabled(false);
					break;
				case WifiManager.WIFI_STATE_DISABLED:
					manager.setWifiEnabled(true);
				default:
					break;
			}
		
		}
	};

	private class WifiReceiver extends AbsBroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			if(WifiManager.WIFI_STATE_CHANGED_ACTION.equals(intent.getAction())){
				//MxyLog.i(TAG, "onReceive =" + intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, 0));
	            switch (intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, 0)) {   
		            case WifiManager.WIFI_STATE_DISABLED: 
		            	setWifiState(WIFI_STATE_OFF);
		            	setEnabled(true);
		            	break;
		            case WifiManager.WIFI_STATE_ENABLED:
		            	setWifiState(WIFI_STATE_NO_CONNECTION);
		            	setEnabled(true);
		                break;   
		            case WifiManager.WIFI_STATE_DISABLING:
		            case WifiManager.WIFI_STATE_ENABLING:
		            	setEnabled(false);
		                break;   

	            } 
				return;
			}
			if(WifiManager.NETWORK_STATE_CHANGED_ACTION.equals(intent.getAction())){
				 Parcelable parcelableExtra = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO); 
				 if(null != parcelableExtra){
					 NetworkInfo networkInfo = (NetworkInfo) parcelableExtra; 
		             if(networkInfo.isConnectedOrConnecting()){  
		            	 setWifiState(WIFI_STATE_CONNECTED);
		            	 setWifiSignal();
		             }
				 }
				return;
			}
			if(WifiManager.RSSI_CHANGED_ACTION.equals(intent.getAction())){
				setWifiSignal();
				return;
			}
		}
		@Override
		public IntentFilter getIntentFilter() {
			// TODO Auto-generated method stub
			IntentFilter filter = new IntentFilter(WifiManager.WIFI_STATE_CHANGED_ACTION);
			filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
			filter.addAction(WifiManager.RSSI_CHANGED_ACTION);
			return filter;
		}
		
	}
}

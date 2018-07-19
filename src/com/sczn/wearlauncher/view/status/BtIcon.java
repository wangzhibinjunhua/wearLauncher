package com.sczn.wearlauncher.view.status;

import com.sczn.wearlauncher.R;
import com.sczn.wearlauncher.receivers.AbsBroadcastReceiver;
import com.sczn.wearlauncher.util.SysServices;
import com.sczn.wearlauncher.view.ClickIcon;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class BtIcon extends StatusIconWithText {
	
	private static final String TAG = BtIcon.class.getSimpleName();
	
	//public static final int IMAGE_OFF = R.drawable.statu_icon_bt_off;
	//public static final int IMAGE_ON = R.drawable.statu_icon_bt_on;

	final BluetoothAdapter mAdapter;
	private BtReceiver mBtReceiver;
	private int btState = BluetoothAdapter.STATE_TURNING_OFF;
	public BtIcon(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		mBtReceiver = new BtReceiver();
		mAdapter = SysServices.getBtAdapter(getContext());
	}
	
	private void initView() {
		// TODO Auto-generated method stub
		if(mAdapter.isEnabled()){
			setBtState(BluetoothAdapter.STATE_ON);
		}else{
			setBtState(BluetoothAdapter.STATE_OFF);
		}
		mIcon.setOnClickListener(onBtClick);
	}

	@Override
	protected void onAttachedToWindow() {
		// TODO Auto-generated method stub
		super.onAttachedToWindow();
		mBtReceiver.register(getContext());
		initView();
	}
	
	@Override
	protected void onDetachedFromWindow() {
		// TODO Auto-generated method stub
		mBtReceiver.unRegister(getContext());
		super.onDetachedFromWindow();
	}
	
	@Override
	public void startFresh() {
		// TODO Auto-generated method stub
		super.startFresh();
		//mBtReceiver.register(getContext());
	}
	@Override
	public void stopFresh() {
		// TODO Auto-generated method stub
		//mBtReceiver.unRegister(getContext());
		super.stopFresh();
	}
	
	private void setBtState(int state){
		btState = state;
		switch (btState) {
		case BluetoothAdapter.STATE_OFF:
			mIcon.setImageResource(R.drawable.statu_icon_bt_off);
			setEnabled(true);
			break;
		case BluetoothAdapter.STATE_ON:
			mIcon.setImageResource(R.drawable.statu_icon_bt_on);
			setEnabled(true);
			break;
		case BluetoothAdapter.STATE_TURNING_OFF:
		case BluetoothAdapter.STATE_TURNING_ON:
			setEnabled(false);
			break;
		default:
			break;
		}
	
	}
	
	private class BtReceiver extends AbsBroadcastReceiver{
		
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			if(BluetoothAdapter.ACTION_STATE_CHANGED.equals(intent.getAction())){
				setBtState(intent.getIntExtra(BluetoothAdapter.EXTRA_STATE,
						BluetoothAdapter.STATE_OFF));
				return;
			}
		}
		@Override
		public IntentFilter getIntentFilter() {
			// TODO Auto-generated method stub
			IntentFilter filter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
			return filter;
		}
		
	}
	
	private OnClickListener onBtClick = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if(mAdapter == null){
				setBtState(BluetoothAdapter.STATE_OFF);
				return;
			}
			if(btState == BluetoothAdapter.STATE_ON){
				mAdapter.disable();
			}else if(btState == BluetoothAdapter.STATE_OFF){
				mAdapter.enable();
			}
		}
	};
}

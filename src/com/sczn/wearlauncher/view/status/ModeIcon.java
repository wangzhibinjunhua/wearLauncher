package com.sczn.wearlauncher.view.status;

import com.sczn.wearlauncher.R;
import com.sczn.wearlauncher.receivers.AbsBroadcastReceiver;
import com.sczn.wearlauncher.util.MxyToast;
import com.sczn.wearlauncher.util.SysServices;
import com.sczn.wearlauncher.view.ClickIcon;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.util.AttributeSet;
import android.view.View;

public class ModeIcon extends StatusIconWithText {
	
	//public static final int IMAGE_SILENT = R.drawable.statu_icon_mode_off;
	//public static final int IMAGE_VIBRATE = R.drawable.statu_icon_mode_vibrate;
	//public static final int IMAGE_NORMAL = R.drawable.statu_icon_mode_on;

	private ModeReceiver mModeReceiver;
	private int modeState = AudioManager.RINGER_MODE_SILENT;
	public ModeIcon(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		mModeReceiver = new ModeReceiver();
		setOnClickListener(onModeClick);
	}
	
	@Override
	public void startFresh() {
		// TODO Auto-generated method stub
		super.startFresh();
		//mModeReceiver.register(getContext());
		//setModeState();
	}
	@Override
	public void stopFresh() {
		// TODO Auto-generated method stub
		//mModeReceiver.unRegister(getContext());
		super.stopFresh();
	}
	
	private void setModeState(){
		modeState = SysServices.getRingMode(getContext());
		switch (modeState) {
			case AudioManager.RINGER_MODE_SILENT:
				mIcon.setImageResource(R.drawable.statu_icon_mode_off);
				break;
			case AudioManager.RINGER_MODE_VIBRATE:
				mIcon.setImageResource(R.drawable.statu_icon_mode_vibrate);
				break;
			case AudioManager.RINGER_MODE_NORMAL:
			default:
				modeState = AudioManager.RINGER_MODE_NORMAL;
				mIcon.setImageResource(R.drawable.statu_icon_mode_on);
				break;
		}
	}
	
	private void showToast(int modeState){
		final String state;
		switch (modeState) {
			case AudioManager.RINGER_MODE_SILENT:
				state = getContext().getString(R.string.mode_silent);
				break;
			case AudioManager.RINGER_MODE_VIBRATE:
				state = getContext().getString(R.string.mode_vibrate);
				break;
			case AudioManager.RINGER_MODE_NORMAL:
			default:
				state = getContext().getString(R.string.mode_normal);
				break;
		}
		MxyToast.showShort(getContext(), String.format(getContext().getString(R.string.mode_curr), state));
	}
	
	private void changeModeState(){
		switch (SysServices.getRingMode(getContext())) {
			case AudioManager.RINGER_MODE_SILENT:
				SysServices.getaudioMgr(getContext()).setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
				break;
			case AudioManager.RINGER_MODE_VIBRATE:
				SysServices.getaudioMgr(getContext()).setRingerMode(AudioManager.RINGER_MODE_NORMAL);
				break;
			case AudioManager.RINGER_MODE_NORMAL:
			default:
				SysServices.getaudioMgr(getContext()).setRingerMode(AudioManager.RINGER_MODE_SILENT);
				break;
		}
		showToast(SysServices.getRingMode(getContext()));
	}
	
	@Override
	protected void onAttachedToWindow() {
		// TODO Auto-generated method stub
		super.onAttachedToWindow();
		mModeReceiver.register(getContext());
		setModeState();
	}
	
	@Override
	protected void onDetachedFromWindow() {
		// TODO Auto-generated method stub
		mModeReceiver.unRegister(getContext());
		super.onDetachedFromWindow();
	}
	
	private OnClickListener onModeClick = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			changeModeState();
		}
	};
	
	private class ModeReceiver extends AbsBroadcastReceiver{
	
		public static final String RINGER_MODE_CHANGED_ACTION = "android.media.RINGER_MODE_CHANGED";

		public void register(Context context){
			IntentFilter filter = new IntentFilter();
			//filter.addAction(VOLUME_CHANGED_ACTION);
			//filter.addAction(Intent.ACTION_AIRPLANE_MODE_CHANGED);
			filter.addAction(RINGER_MODE_CHANGED_ACTION);
			context.registerReceiver(this, filter);
		}
		public void unRegister(Context context){
			context.unregisterReceiver(this);
		}
		
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			if(RINGER_MODE_CHANGED_ACTION.equals(intent.getAction())){
				setModeState();
			}
		}
		@Override
		public IntentFilter getIntentFilter() {
			// TODO Auto-generated method stub
			IntentFilter filter = new IntentFilter();
			//filter.addAction(VOLUME_CHANGED_ACTION);
			//filter.addAction(Intent.ACTION_AIRPLANE_MODE_CHANGED);
			filter.addAction(RINGER_MODE_CHANGED_ACTION);
			return filter;
		}
	}

}

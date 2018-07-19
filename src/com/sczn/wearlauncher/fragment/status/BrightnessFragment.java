package com.sczn.wearlauncher.fragment.status;

import java.lang.ref.WeakReference;

import android.app.Activity;
import android.content.Context;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.provider.Settings.SettingNotFoundException;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.SeekBar;
import android.widget.TextView;

import com.sczn.wearlauncher.R;
import com.sczn.wearlauncher.fragment.absDialogFragment;
import com.sczn.wearlauncher.util.MxyLog;
import com.sczn.wearlauncher.util.SysServices;
import com.sczn.wearlauncher.view.SeekBarView;
import com.sczn.wearlauncher.view.SeekBarView.ISeekBarStopTouch;

public class BrightnessFragment extends absDialogFragment implements OnClickListener,ISeekBarStopTouch{
	private static final String TAG = BrightnessFragment.class.getSimpleName();
	
	public static final int BRIGHTNESS_MAX = 255;
	
	private static BrightnessFragment instance;
	public static BrightnessFragment getInstance(){
		/*
		if(instance == null){
			synchronized (BrightnessFragment.class) {
				instance = new BrightnessFragment();
			}
		}
		return instance;*/
		return new BrightnessFragment();
	}
	public BrightnessFragment(){
		super();
		
		mBrightnessHandler = new BrightnessHandler(getActivity());
		mBrightnessObserver = new BrightnessObserver(mBrightnessHandler);
	}

	private TextView mBrightnessAuto;
	private TextView mBrightness_done;
	private SeekBarView mBrightnessSeekBar;
	
	private BrightnessObserver mBrightnessObserver;
	private BrightnessHandler mBrightnessHandler;
	@Override
	protected int getLayoutResouceId() {
		// TODO Auto-generated method stub
		return R.layout.fragment_status_brightness;
	}

	@Override
	protected void creatView() {
		// TODO Auto-generated method stub
		initView();
		initData();
	}
	
	private void initView(){
		mBrightnessAuto = findViewById(R.id.brightness_auto);
		mBrightness_done = findViewById(R.id.brightness_done);
		mBrightnessSeekBar = findViewById(R.id.brightness_seekbar);
	}
	
	private void initData(){
		mBrightnessAuto.setOnClickListener(this);
		mBrightness_done.setOnClickListener(this);
		
		mBrightnessSeekBar.setTitle(R.string.brightness_title);
		mBrightnessSeekBar.setMax(BRIGHTNESS_MAX);
		mBrightnessSeekBar.setListen(this);
		initSeekBar();
		
		mBrightnessObserver.register(getActivity());
	}
	
	private void initSeekBar() {
		// TODO Auto-generated method stub
		changeSeekBarEnable();
	}
	private void changeSeekBarEnable(){
		mBrightnessSeekBar.setEnabled(!isBrightnessAuto());
		mBrightnessSeekBar.setSeekBarProgress(getBrightnessLevel());
	}

	@Override
	protected void destorytView() {
		// TODO Auto-generated method stub
		mBrightnessObserver.unRegister(getActivity());
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
			case R.id.brightness_auto:
				setIsAuto(isBrightnessAuto());
				break;
			case R.id.brightness_done:
				dismiss();
				break;
	
			default:
				break;
		}
	}
	private void setIsAuto(boolean isAuto) {
		// TODO Auto-generated method stub
		if(isAuto){
			setSystemSettingInt(Settings.System.SCREEN_BRIGHTNESS_MODE,
					Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
		}else{
			setSystemSettingInt(Settings.System.SCREEN_BRIGHTNESS_MODE,
					Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC);
		}
		changeSeekBarEnable();
	}
	private boolean isBrightnessAuto(){
		return getSystemSettingInt(Settings.System.SCREEN_BRIGHTNESS_MODE) == 
				Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC;
	}
	private int getBrightnessLevel(){
		return getSystemSettingInt(Settings.System.SCREEN_BRIGHTNESS);
	}
	
	private void onBrightnessChange(){
		mBrightnessSeekBar.setSeekBarProgress(getBrightnessLevel());
		if(isBrightnessAuto()){
			mBrightnessSeekBar.setEnabled(false);
			MxyLog.i(TAG, "isBrightnessAuto()=" + "true"
					+ "--BrightnessLevel=" + getBrightnessLevel());
		}else{
			mBrightnessSeekBar.setEnabled(true);
			MxyLog.i(TAG, "isBrightnessAuto()=" + "false"
					+ "--BrightnessLevel=" + getBrightnessLevel());
		}
	}
	
	@Override
	public void onSeekBarStopTouch(int id, int progess) {
		// TODO Auto-generated method stub
		SysServices.setSystemSettingInt(getActivity(),Settings.System.SCREEN_BRIGHTNESS,progess);
	}
	
	private static class BrightnessHandler extends Handler{

		private WeakReference<Activity> mActivity;    
		public BrightnessHandler(Activity mActivity){    
			this.mActivity = new WeakReference<Activity>(mActivity);    
		} 

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			if(mActivity.get() == null){
				return;
			}
			super.handleMessage(msg);
		}
		
	}
	
	private class BrightnessObserver extends ContentObserver{
		
		public final Uri levelUri;
		public final Uri modeUri;
		
		public void register(Context context){
			context.getContentResolver().registerContentObserver(levelUri, true,this);
			context.getContentResolver().registerContentObserver(modeUri, true,this);
		}
		
		public void unRegister(Context context){
			context.getContentResolver().unregisterContentObserver(this);
		}

		public BrightnessObserver(Handler handler) {
			super(handler);
			// TODO Auto-generated constructor stub
			levelUri = Settings.System.getUriFor(Settings.System.SCREEN_BRIGHTNESS);
			modeUri = Settings.System.getUriFor(Settings.System.SCREEN_BRIGHTNESS_MODE);
		}
		
		@Override
		public void onChange(boolean selfChange) {
			// TODO Auto-generated method stub
			super.onChange(selfChange);
			onBrightnessChange();
		}

	}
}

package com.sczn.wearlauncher.fragment.status;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.sczn.wearlauncher.R;
import com.sczn.wearlauncher.fragment.absFragment;
import com.sczn.wearlauncher.fragment.card.AltitudeFragment;
import com.sczn.wearlauncher.services.SosService;
import com.sczn.wearlauncher.util.MxyLog;
import com.sczn.wearlauncher.view.ClickIcon;
import com.sczn.wearlauncher.view.status.BrightnessIcon;
import com.sczn.wearlauncher.view.status.BrightnessIcon.IBrightnessClick;
import com.sczn.wearlauncher.view.status.BtIcon;
import com.sczn.wearlauncher.view.status.StatusIconWithText;
import com.sczn.wearlauncher.view.status.VolumeIcon;
import com.sczn.wearlauncher.view.status.VolumeIcon.IVolumeClick;

public class ThirdStatusFragment extends absFragment implements IVolumeClick,IBrightnessClick{
	private static final String TAG = ThirdStatusFragment.class.getSimpleName();
	
	public static final String FRAGMENT_TAG_BRIGHTNESS = "status_tag_brightness";
	public static final String FRAGMENT_TAG_VOLUME = "status_tag_volume";
	
	public static final String ARG_IS_TMP = "is_tmp";
	public static ThirdStatusFragment newInstance(boolean isTmp){
		ThirdStatusFragment fragment = new ThirdStatusFragment();
		Bundle bdl = new Bundle();
		bdl.putBoolean(ARG_IS_TMP, isTmp);
		fragment.setArguments(bdl);
		return fragment;
		
	}
	
	private boolean isTmp = false;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Bundle bdl = getArguments();
		if(bdl != null){
			isTmp = bdl.getBoolean(ARG_IS_TMP, false);
		}
	}
	
	private VolumeIcon mVolumeIcon;
	private BrightnessIcon mBrightnessIcon;
	
	@Override
	protected int getLayoutResouceId() {
		// TODO Auto-generated method stub
		return R.layout.fragment_status_third;
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		mVolumeIcon = findViewById(R.id.status_volume);
		mBrightnessIcon = findViewById(R.id.status_brightness);
	}

	@Override
	protected void initData() {
		// TODO Auto-generated method stub
	}
	
	@Override
	protected void startFreshData() {
		// TODO Auto-generated method stub
		super.startFreshData();
		mVolumeIcon.setClickCb(this);
		mBrightnessIcon.setClickCb(this);
	}
	
	@Override
	protected void endFreshData() {
		// TODO Auto-generated method stub
		super.endFreshData();
		mVolumeIcon.setClickCb(null);
		mBrightnessIcon.setClickCb(null);
	}

	@Override
	public void onBrightnessClick() {
		// TODO Auto-generated method stub
		final BrightnessFragment fragmet = (BrightnessFragment) getChildFragmentManager().findFragmentByTag(FRAGMENT_TAG_BRIGHTNESS);
		if(fragmet != null){
			MxyLog.e(TAG, "showVolumeFragment--fragmet != null");
		}else{
			BrightnessFragment.getInstance().show(getChildFragmentManager(), FRAGMENT_TAG_BRIGHTNESS);
		}
	}

	@Override
	public void onVolumeClick() {
		// TODO Auto-generated method stub
		final VolumeFragment fragmet = (VolumeFragment) getChildFragmentManager().findFragmentByTag(FRAGMENT_TAG_VOLUME);
		if(fragmet != null){
			MxyLog.e(TAG, "showVolumeFragment--fragmet != null");
		}else{
			VolumeFragment.getInstance().show(getChildFragmentManager(), FRAGMENT_TAG_VOLUME);
		}
	}
	
}

package com.sczn.wearlauncher.fragment.card;

import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import com.sczn.wearlauncher.R;
import com.sczn.wearlauncher.fragment.absFragment;
import com.sczn.wearlauncher.util.DateFormatUtil;

public class GeographicFragment extends absFragment {
	
public static final String ARG_IS_TMP = "is_tmp";
	
	public static GeographicFragment newInstance(boolean isTmp) {
		GeographicFragment fragment = new GeographicFragment();
		Bundle bdl = new Bundle();
		bdl.putBoolean(ARG_IS_TMP, isTmp);
		fragment.setArguments(bdl);
		return fragment;

	}

	private boolean isTmp;
	private TextView mTime;
	private TextView mAtmosphere;
	private TextView mAltitude;
	private Handler mHandler = new Handler();
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Bundle bdl = getArguments();
		if (bdl != null) {
			isTmp = bdl.getBoolean(ARG_IS_TMP, false);
		}
	}
	
	@Override
	protected int getLayoutResouceId() {
		// TODO Auto-generated method stub
		return R.layout.fragment_card_geographic;
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		mTime = findViewById(R.id.geographic_time);
		mAtmosphere = findViewById(R.id.geographic_atmosphere);
		mAltitude = findViewById(R.id.geographic_aititude);
	}

	@Override
	protected void initData() {
		// TODO Auto-generated method stub
		mTime.setText(DateFormatUtil.getCurrTimeString(DateFormatUtil.YYYY_MM_DD_HM));
		mAtmosphere.setText(getString(R.string.geographic_atmosphere, 1000.0));
		mAltitude.setText(getString(R.string.geographic_altitude, 100.0));
	}

	@Override
	protected void startFreshData() {
		// TODO Auto-generated method stub
		if(isTmp){
			return;
		}
		super.startFreshData();
		mHandler.postDelayed(new Runnable(){   

		    public void run() {   
		    	mHandler.removeCallbacksAndMessages(null);
		    	mTime.setText(DateFormatUtil.getCurrTimeString(DateFormatUtil.YYYY_MM_DD_HM));
		    	mHandler.postDelayed(this,1000);
		    }   

		 }, 1000);
	}
	
	@Override
	protected void endFreshData() {
		// TODO Auto-generated method stub
		if(isTmp){
			return;
		}
		super.endFreshData();
		mHandler.removeCallbacksAndMessages(null);
	}
}

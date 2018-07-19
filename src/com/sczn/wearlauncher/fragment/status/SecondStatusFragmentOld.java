package com.sczn.wearlauncher.fragment.status;

import com.sczn.wearlauncher.R;
import com.sczn.wearlauncher.fragment.absFragment;
import com.sczn.wearlauncher.util.MxyLog;
import com.sczn.wearlauncher.view.ClickIcon;
import com.sczn.wearlauncher.view.status.BatteryIcon;
import com.sczn.wearlauncher.view.status.BtIcon;
import com.sczn.wearlauncher.view.status.DataIcon;
import com.sczn.wearlauncher.view.status.SimIcon;
import com.sczn.wearlauncher.view.status.WifiIcon;

public class SecondStatusFragmentOld extends absFragment {
	private final static String TAG = SecondStatusFragmentOld.class.getSimpleName();
	
	private BatteryIcon mBatteryIcon;
	private SimIcon mSimIcon;
	private WifiIcon mWifiIcon;
	private DataIcon mDataIcon;
	
	@Override
	protected int getLayoutResouceId() {
		// TODO Auto-generated method stub
		return R.layout.fragment_status_second;
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		mBatteryIcon = findViewById(R.id.status_battery);
		mSimIcon = findViewById(R.id.status_sim);
		mDataIcon = findViewById(R.id.status_data);
		mWifiIcon = findViewById(R.id.status_wifi);
	}

	@Override
	protected void initData() {
		// TODO Auto-generated method stub
	}

	@Override
	protected void startFreshData() {
		// TODO Auto-generated method stub
		super.startFreshData();
		mWifiIcon.startFresh();
		mDataIcon.startFresh();
		mSimIcon.startFresh();
		mBatteryIcon.startFresh();
	}
	
	@Override
	protected void endFreshData() {
		// TODO Auto-generated method stub
		mWifiIcon.stopFresh();
		mDataIcon.stopFresh();
		mSimIcon.stopFresh();
		mBatteryIcon.stopFresh();
		super.endFreshData();
	}
}

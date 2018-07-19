package com.sczn.wearlauncher.fragment.card;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.sczn.wearlauncher.R;
import com.sczn.wearlauncher.card.healthalarm.ActivityHealthAlarm;
import com.sczn.wearlauncher.fragment.absFragment;
import com.sczn.wearlauncher.util.DateFormatUtil;

public class HealthAlamFragment extends absFragment implements android.view.View.OnClickListener{
	
	public static final String ARG_IS_TMP = "is_tmp";
	
	public static HealthAlamFragment newInstance(boolean isTmp) {
		HealthAlamFragment fragment = new HealthAlamFragment();
		Bundle bdl = new Bundle();
		bdl.putBoolean(ARG_IS_TMP, isTmp);
		fragment.setArguments(bdl);
		return fragment;

	}

	private boolean isTmp;
	private TextView mSitAlarm;
	private TextView mDrinkAlarm;
	
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
		return R.layout.fragment_card_healthalarm;
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		mSitAlarm = findViewById(R.id.healthalarm_sit);
		mDrinkAlarm = findViewById(R.id.healthalarm_drink);
	}

	@Override
	protected void initData() {
		// TODO Auto-generated method stub
		mSitAlarm.setText(DateFormatUtil.getCurrTimeString(DateFormatUtil.HM));
		mDrinkAlarm.setText(DateFormatUtil.getCurrTimeString(DateFormatUtil.HM));
		
		mSitAlarm.setOnClickListener(this);
		mDrinkAlarm.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent i = new Intent(mActivity, ActivityHealthAlarm.class);
		switch (v.getId()) {
			case R.id.healthalarm_sit:
				i.putExtra(ActivityHealthAlarm.EXTRA_INTENT, ActivityHealthAlarm.FRAGMENT_SIT);
				break;
			case R.id.healthalarm_drink:
				i.putExtra(ActivityHealthAlarm.EXTRA_INTENT, ActivityHealthAlarm.FRAGMENT_DRINK);
				break;
			default:
				return;
		}
		mActivity.startActivity(i);
	}

}

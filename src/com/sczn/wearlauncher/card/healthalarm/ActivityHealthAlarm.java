package com.sczn.wearlauncher.card.healthalarm;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.sczn.wearlauncher.R;
import com.sczn.wearlauncher.card.CardActivity;
import com.sczn.wearlauncher.util.MxyLog;
import com.sczn.wearlauncher.util.MxyToast;

public class ActivityHealthAlarm extends CardActivity implements OnClickListener{

	public static final String EXTRA_INTENT = "witch_gragment";
	public static final String EXTRA_ALARM_TYPE = "alarm_type";
	
	public static final int FRAGMENT_SIT = 0;
	public static final int FRAGMENT_DRINK = 1;
	public static final int FRAGMENT_SETTING = 2;

	private ImageView mBackView;
	private TextView mTitle;
	private int contentId;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		MxyLog.d(this, "onCreate--fragmentIndex=" + getIntent().getIntExtra(EXTRA_INTENT, FRAGMENT_SIT));
		
	}
	
	@Override
	protected void onNewIntent(Intent intent) {
		// TODO Auto-generated method stub
		super.onNewIntent(intent);
		setIntent(intent);
		initFragment();
	}
	
	@Override
	protected int getLayoutId() {
		// TODO Auto-generated method stub
		return R.layout.activity_card_healthalarm;
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		mTitle = (TextView) findViewById(R.id.card_title_bar_title);
		mBackView = (ImageView) findViewById(R.id.card_title_bar_back);
		contentId = R.id.healthalarm_content;
	}

	@Override
	protected void initData() {
		// TODO Auto-generated method stub
		mBackView.setOnClickListener(this);
		initFragment();
	}

	private void initFragment() {
		// TODO Auto-generated method stub
		FragmentManager fm = getFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		
		ft.replace(contentId, getFragment());
		
		ft.commitAllowingStateLoss();
	}
	
	public Fragment getFragment(){
		final int fragmentIndex = getIntent().getIntExtra(EXTRA_INTENT, FRAGMENT_SIT);
		//MxyToast.showShort(getApplication(), "onNewIntent--fragmentIndex=" + fragmentIndex);
		switch (fragmentIndex) {
			case FRAGMENT_SETTING:
				mTitle.setText(R.string.healthalarm_setting);
				return getSettingFragment(getIntent().getIntExtra(EXTRA_ALARM_TYPE, ModelAlarm.ALARM_TYPE_SIT));
			case FRAGMENT_DRINK:
				mTitle.setText(R.string.healthalarm_drink);
				return getDrinkFragment();
			case FRAGMENT_SIT:
				mTitle.setText(R.string.healthalarm_sit);
				return getSitFragment();
			default:
				mTitle.setText(R.string.healthalarm_sit);
				return getSitFragment();
		}
	}
	
	private FragmentAlarmList getSettingFragment(int alarmType){
		return FragmentAlarmList.newInstance(alarmType);
	}
	private FragmentDrink getDrinkFragment(){
		return FragmentDrink.newInstance();
	}
	private FragmentSit getSitFragment(){
		return FragmentSit.newInstance();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
			case R.id.card_title_bar_back:
				onBackPressed();
				break;
	
			default:
				break;
		}
	}

}

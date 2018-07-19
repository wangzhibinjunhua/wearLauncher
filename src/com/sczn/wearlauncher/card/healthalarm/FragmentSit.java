package com.sczn.wearlauncher.card.healthalarm;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.sczn.wearlauncher.R;
import com.sczn.wearlauncher.fragment.absFragment;
import com.sczn.wearlauncher.fragment.status.FirstStatusFragment;

public class FragmentSit extends absFragment implements OnClickListener{

	public static FragmentSit newInstance(){
		FragmentSit fragment = new FragmentSit();
		Bundle bdl = new Bundle();
		fragment.setArguments(bdl);
		return fragment;
	}
	
	@Override
	protected int getLayoutResouceId() {
		// TODO Auto-generated method stub
		return R.layout.fragment_card_healthalarm_sit;
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void initData() {
		// TODO Auto-generated method stub
		findViewById(R.id.sit_setting).setOnClickListener(this);
	}
	
	private void gotoFragmentSetting(){
		Intent i = new Intent(getActivity(), ActivityHealthAlarm.class);
		i.putExtra(ActivityHealthAlarm.EXTRA_INTENT, ActivityHealthAlarm.FRAGMENT_SETTING);
		i.putExtra(ActivityHealthAlarm.EXTRA_ALARM_TYPE, ModelAlarm.ALARM_TYPE_SIT);
		getActivity().startActivity(i);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
			case R.id.sit_setting:
				gotoFragmentSetting();
				break;
	
			default:
				break;
		}
	}

}

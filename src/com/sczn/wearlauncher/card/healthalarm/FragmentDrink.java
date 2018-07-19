package com.sczn.wearlauncher.card.healthalarm;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.sczn.wearlauncher.R;
import com.sczn.wearlauncher.fragment.absFragment;
import com.sczn.wearlauncher.fragment.status.FirstStatusFragment;

public class FragmentDrink extends absFragment implements OnClickListener{
	
	public static FragmentDrink newInstance(){
		FragmentDrink fragment = new FragmentDrink();
		Bundle bdl = new Bundle();
		fragment.setArguments(bdl);
		return fragment;
	}

	private TextView mDrinkTarget;
	private TextView mDrinkCount;
	
	@Override
	protected int getLayoutResouceId() {
		// TODO Auto-generated method stub
		return R.layout.fragment_card_healthalarm_drink;
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		mDrinkTarget = findViewById(R.id.drink_target);
		mDrinkCount = findViewById(R.id.drink_count);
	}

	@Override
	protected void initData() {
		// TODO Auto-generated method stub
		mDrinkTarget.setText(getString(R.string.drink_target, "--"));
		mDrinkCount.setText(getString(R.string.drink_count, 0));
		findViewById(R.id.drink_much).setOnClickListener(this);
		findViewById(R.id.drink_little).setOnClickListener(this);
		findViewById(R.id.drink_setting).setOnClickListener(this);
	}

	private void gotoFragmentSetting(){
		Intent i = new Intent(getActivity(), ActivityHealthAlarm.class);
		i.putExtra(ActivityHealthAlarm.EXTRA_INTENT, ActivityHealthAlarm.FRAGMENT_SETTING);
		i.putExtra(ActivityHealthAlarm.EXTRA_ALARM_TYPE, ModelAlarm.ALARM_TYPE_DRINK);
		getActivity().startActivity(i);
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.drink_much:
			
			break;
		case R.id.drink_little:
			
			break;
		case R.id.drink_setting:
			gotoFragmentSetting();
			break;
		default:
			break;
		}
	}

}

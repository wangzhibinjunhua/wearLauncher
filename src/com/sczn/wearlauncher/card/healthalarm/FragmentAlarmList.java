package com.sczn.wearlauncher.card.healthalarm;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sczn.wearlauncher.R;
import com.sczn.wearlauncher.card.healthalarm.AdapterAlarm.IAlarmClickLiten;
import com.sczn.wearlauncher.card.healthalarm.UtilHealthAlarm.IHealthAlarmListen;
import com.sczn.wearlauncher.fragment.absFragment;
import com.sczn.wearlauncher.util.MxyToast;
import com.sczn.wearlauncher.view.MyRecyclerView;

public class FragmentAlarmList extends absFragment implements OnClickListener,IAlarmClickLiten,IHealthAlarmListen{

	public static final String ARG_ALARM_TYPE = "alarm_type";
	public static final String FRAGMENT_TAG_ALARM_EDIT = "alarm_edit";
	
	public static FragmentAlarmList newInstance(int type){
		FragmentAlarmList fragment = new FragmentAlarmList();
		final Bundle bdl = new Bundle();
		bdl.putInt(ARG_ALARM_TYPE, type);
		fragment.setArguments(bdl);
		return fragment;
	}
	
	private int alarmType = ModelAlarm.ALARM_TYPE_DRINK;
	private MyRecyclerView mRecyclerView;
	private TextView mAddButton;
	private AdapterAlarm mAdapterAlarm;
	
	public int getAlarmType(){
		return this.alarmType;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		final Bundle bdl = getArguments();
		if(bdl != null){
			alarmType = bdl.getInt(ARG_ALARM_TYPE);
		}
	}
	
	@Override
	protected int getLayoutResouceId() {
		// TODO Auto-generated method stub
		return R.layout.fragment_card_healthalarm_alarmlist;
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		mRecyclerView = findViewById(R.id.healthalarm_list);
		mAddButton = findViewById(R.id.healthalarm_add);
	}

	@Override
	protected void initData() {
		// TODO Auto-generated method stub
		mAdapterAlarm = new AdapterAlarm();
		mRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
		mRecyclerView.setAdapter(mAdapterAlarm);
		mRecyclerView.setEmpty(findViewById(R.id.healthalarm_empty));
		mAddButton.setOnClickListener(this);
	}
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		freshAlarm();
		UtilHealthAlarm.getInstance().addListen(this);
	}
	
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		UtilHealthAlarm.getInstance().removeListen(this);
		super.onPause();
	}
	
	private void freshAlarm(){
		switch (alarmType) {
			case ModelAlarm.ALARM_TYPE_DRINK:
				mAdapterAlarm.setData(UtilHealthAlarm.getInstance().getDrinkAlarms());
				break;
			case ModelAlarm.ALARM_TYPE_SIT:
				mAdapterAlarm.setData(UtilHealthAlarm.getInstance().getSitAlarms());
				break;
			default:
				return;
		}
		mAdapterAlarm.notifyDataSetChanged();
	}
	
	private void addAlarm(){
		if(mRecyclerView.getAdapter().getItemCount() >= UtilHealthAlarm.MAX_ALARM_COUNT){
			MxyToast.showShort(mActivity, R.string.alarm_count_limit);
			return;
		}
		gotoAlarmEdit(null);
	}
	
	private void gotoAlarmEdit(ModelAlarm alarm){
		FragmentAlarmEdit.newInstance(alarmType, alarm).show(getChildFragmentManager(), FRAGMENT_TAG_ALARM_EDIT);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
			case R.id.healthalarm_add:
				addAlarm();
				break;
			default:
				break;
		}
	}

	@Override
	public void onDeleteClick(int position, ModelAlarm alarm) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onAlarmClick(ModelAlarm alarm) {
		// TODO Auto-generated method stub
		gotoAlarmEdit(alarm);
	}

	@Override
	public void onHealthAlarmChanged() {
		// TODO Auto-generated method stub
		freshAlarm();
	}


}

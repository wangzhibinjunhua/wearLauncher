package com.sczn.wearlauncher.card.healthalarm;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sczn.wearlauncher.LauncherApp;
import com.sczn.wearlauncher.R;
import com.sczn.wearlauncher.card.healthalarm.AdapterAlarm.IAlarmClickLiten;
import com.sczn.wearlauncher.card.healthalarm.UtilHealthAlarm.IHealthAlarmListen;
import com.sczn.wearlauncher.db.DBUtil;
import com.sczn.wearlauncher.fragment.absFragment;
import com.sczn.wearlauncher.util.MxyToast;
import com.sczn.wearlauncher.view.MyRecyclerView;

public class FragmentAlarmList extends absFragment implements OnClickListener,IAlarmClickLiten,IHealthAlarmListen{

	public static final String ARG_ALARM_TYPE = "alarm_type";
	public static final String FRAGMENT_TAG_ALARM_EDIT = "alarm_edit";
	private UtilHealthAlarm mUtilHealthAlarm;
	private DBUtil mDbUtil;
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
	private Context mContext;
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
		mUtilHealthAlarm =UtilHealthAlarm.getInstance();
		mDbUtil = new DBUtil(LauncherApp.appContext);
		//if(alarmType ==ModelAlarm.ALARM_TYPE_DRINK)
			mContext =getActivity();
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
		mAdapterAlarm.setOnClickListen(this);
		mRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
		mRecyclerView.setAdapter(mAdapterAlarm);
		mRecyclerView.setEmpty(findViewById(R.id.healthalarm_empty));
		mAddButton.setOnClickListener(this);
		UtilHealthAlarm.getInstance().addListen(this);
	}
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		freshAlarm();
		Log.e("mxy","onResume alarm list");
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		freshAlarm();
		Log.e("mxy","onStart alarm list");
	}
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		Log.e("mxy","onPause alarm list");
		//freshAlarm();
		//UtilHealthAlarm.getInstance().removeListen(this);
	}

		@Override
	protected void startFreshData() {
		// TODO Auto-generated method stub
		super.startFreshData();
		freshAlarm();
		Log.e("mxy","startFreshData alarm list");
		UtilHealthAlarm.getInstance().addListen(this);
	}
	
	@Override
	protected void endFreshData() {
		// TODO Auto-generated method stub
		Log.e("mxy","endFreshData alarm list");
		super.endFreshData();
		//UtilHealthAlarm.getInstance().removeListen(this);
	}
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Log.e("mxy","onDestroy alarm list");
		UtilHealthAlarm.getInstance().removeListen(this);
	}
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		freshAlarm();
		}
	private void freshAlarm(){
		//mUtilHealthAlarm.InitAlarmIntent();//wbin add
		Log.e("mxy","freshAlarm");
		switch (alarmType) {
			case ModelAlarm.ALARM_TYPE_DRINK:
				mAdapterAlarm.setData(UtilHealthAlarm.getInstance().getDrinkAlarms());
				break;
			case ModelAlarm.ALARM_TYPE_SIT:
				Log.e("mxy","freshAlarm sit");
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
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		this.mContext = activity;
	}
	@Override
	public void onDeleteClick(int position, ModelAlarm alarm) {
		// TODO Auto-generated method stub
		Log.e("mxy","onDeleteClick onDeleteClick alarm"+alarm);
		Log.e("mxy","alarm edit mContext "+mContext);
		if(mDbUtil.deleteAlarm(alarm.getID())!=-1l)
			{
				mUtilHealthAlarm.InitAlarmIntent();
			}
		//mUtilHealthAlarm.showProgressDialog(getString(R.string.progress_title),getString(R.string.progress_message_delete),mContext);
	}

	@Override
	public void onAlarmClick(ModelAlarm alarm) {
		// TODO Auto-generated method stub
		Log.e("mxy","onAlarmClick "+alarm);
		gotoAlarmEdit(alarm);
	}
	@Override
	public void onAlarmSwitch(boolean enable,ModelAlarm alarm) {
		// TODO Auto-generated method stub
		Log.e("mxy","onAlarmSwitch "+alarm);
		alarm.setEnable(enable);
	}
	@Override
	public void onHealthAlarmChanged() {
		// TODO Auto-generated method stub
		Log.e("mxy","onHealthAlarmChanged ");
		freshAlarm();
		mUtilHealthAlarm.setHealthAlarm(mActivity);
	}
	
}

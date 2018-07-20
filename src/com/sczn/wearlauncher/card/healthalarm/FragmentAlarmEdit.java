package com.sczn.wearlauncher.card.healthalarm;

import com.sczn.wearlauncher.LauncherApp;
import com.sczn.wearlauncher.R;
import com.sczn.wearlauncher.db.DBUtil;
import com.sczn.wearlauncher.fragment.absDialogFragment;
import com.sczn.wearlauncher.util.MxyLog;
import com.sczn.wearlauncher.util.MxyToast;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class FragmentAlarmEdit extends absDialogFragment implements OnClickListener{

	public static final String ARG_ALARM = "alarm";
	public static final String ARG_ALARM_TYPE = "alarm_type";
	
	private DBUtil mDbUtil;
 	private ProgressDialog progressDialog;
	public static FragmentAlarmEdit newInstance(int alarmType, ModelAlarm alarm){
		final FragmentAlarmEdit fragment = new FragmentAlarmEdit();
		final Bundle bdl = new Bundle();
		bdl.putParcelable(ARG_ALARM, alarm);
		bdl.putInt(ARG_ALARM_TYPE, alarmType);//wbin add
		fragment.setArguments(bdl);
		return fragment;
	}
	private ViewTimePicker mTimePicker;
	private ViewWeekdayPick mViewWeekdayPick;
	private TextView mSure;
	private TextView mCancle;
	private ModelAlarm mAlarm;
	private int alarmType;
	private  Context mContext;
	private boolean isAdd = true;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		final Bundle bdl = getArguments();
		if(bdl != null){
			mAlarm = (ModelAlarm) bdl.get(ARG_ALARM);
			if(mAlarm != null){
				isAdd = false;
			}else{
				isAdd = true;
			}
			alarmType = bdl.getInt(ARG_ALARM_TYPE, ModelAlarm.ALARM_TYPE_DRINK);
		}
		mDbUtil = new DBUtil(LauncherApp.appContext);
		mContext=getActivity();
	}
	
	@Override
	protected int getLayoutResouceId() {
		// TODO Auto-generated method stub
		return R.layout.fragment_card_healthalarm_alarm_edit;
	}

	@Override
	protected void creatView() {
		// TODO Auto-generated method stub
		mTimePicker = findViewById(R.id.alarm_timer_pick);
		mViewWeekdayPick = findViewById(R.id.alarm_pick_weekday);
		mSure = findViewById(R.id.alarm_edit_sure);
		mCancle = findViewById(R.id.alarm_edit_cancle);
		
		findViewById(R.id.card_title_bar_back).setVisibility(View.GONE);
		((TextView)findViewById(R.id.card_title_bar_title)).setText(R.string.healthalarm_setting);
		
		initData();
	}

	private void initData() {
		// TODO Auto-generated method stub
		mSure.setOnClickListener(this);
		mCancle.setOnClickListener(this);
		
		if(mAlarm != null){
			mViewWeekdayPick.setInitValue(mAlarm.getRepeatDay());
		}
	}

	@Override
	protected void destorytView() {
		// TODO Auto-generated method stub
	}

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		this.mContext = activity;
	}
	private void alarmSave(){
		MxyLog.e("mxy", "alarmSave "+isAdd);
		if(isAdd){
			if(-1 == mDbUtil.insertAlarm(mTimePicker.getTimeInMill(), alarmType, mViewWeekdayPick.getValue(), true)){
				MxyToast.showShort(getActivity(), R.string.alarm_sava_failed);
			}else{
				UtilHealthAlarm.getInstance().InitAlarmIntent();
				MxyToast.showShort(getActivity(), R.string.alarm_sava_success);
			}
		}else{
			if(-1 == mDbUtil.updateAlarm(mAlarm.getID(), mTimePicker.getTimeInMill(), alarmType,
					mViewWeekdayPick.getValue(), mAlarm.isEnable())){
				MxyToast.showShort(getActivity(), R.string.alarm_sava_failed);
			}else{
				UtilHealthAlarm.getInstance().InitAlarmIntent();
				MxyToast.showShort(getActivity(), R.string.alarm_sava_success);
			}
		}
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
			case R.id.alarm_edit_sure:
				alarmSave();
				Log.e("mxy","alarm edit mContext "+mContext);
				//UtilHealthAlarm.getInstance().showProgressDialog(getString(R.string.progress_title),getString(R.string.progress_message),mContext);
				dismissAllowingStateLoss();
				break;
			case R.id.alarm_edit_cancle:
				dismissAllowingStateLoss();
				break;
			default:
				break;
		}
	}

}

package com.sczn.wearlauncher.fragment.card;

import java.util.Calendar;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sczn.wearlauncher.R;
import com.sczn.wearlauncher.activity.card.HeartRateActivity;
import com.sczn.wearlauncher.db.DBUtil;
import com.sczn.wearlauncher.fragment.absFragment;
import com.sczn.wearlauncher.util.MxyLog;

public class HealthFragment extends absFragment implements SensorEventListener {
	
	public static final String ARG_IS_TMP = "is_tmp";
	
	public static HealthFragment newInstance(boolean isTmp) {
		HealthFragment fragment = new HealthFragment();
		Bundle bdl = new Bundle();
		bdl.putBoolean(ARG_IS_TMP, isTmp);
		fragment.setArguments(bdl);
		return fragment;

	}
	
	private Context mContext;
	private ImageView heartRateImageView;
	private TextView mHeartRateTextView,mBeginTest;
	private LinearLayout mHeartRatePart;
	private SensorManager mSensorManager;
	private Sensor mHeartRateSensor,mSensorTest;
	private int mHeartRate = 0;
	private DBUtil dbUtil;
	private StringBuffer mRecords = new StringBuffer();
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				beginTest();
				break;
			case 1:
				if(mHeartRate == 0){
					Toast.makeText(mContext, R.string.heart_rate_error, Toast.LENGTH_SHORT).show();
					sendEmptyMessageDelayed(1, 30 * 1000);
				}
				break;
			}
		};
	};
	
	private boolean isTmp;
	
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
		mContext = getActivity();
		return R.layout.fragment_card_health;
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		heartRateImageView = findViewById(R.id.heart_rate_iv);
		mHeartRateTextView = findViewById(R.id.heart_rate_text);
		mBeginTest = findViewById(R.id.begin_test);
		mHeartRatePart = findViewById(R.id.heart_rate_num_part);
	}

	@Override
	protected void initData() {
		// TODO Auto-generated method stub
		mSensorManager = (SensorManager) mContext.getSystemService(Context.SENSOR_SERVICE);
		mHeartRateSensor = mSensorManager.getDefaultSensor(21);
		mSensorTest = mSensorManager.getDefaultSensor(1);
		dbUtil = new DBUtil(mContext);
		mHeartRate = 0;
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);
		view.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				// TODO Auto-generated method stub
				try {
					Intent i = new Intent(mContext, HeartRateActivity.class);
					i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					i.putExtra(HeartRateActivity.ARG_RECORDS, mRecords.toString());
					mContext.startActivity(i);
				} catch (Exception e) {
					// TODO: handle exception
					MxyLog.e(this, "gotoSleepActivity--" + e.toString());
				}
			
			}
		});
	}
	
	@Override
	protected void startFreshData() {
		// TODO Auto-generated method stub
		super.startFreshData();
		if(isTmp){
			return;
		}
		mRecords.setLength(0);
		handler.sendEmptyMessageDelayed(0, 3 * 1000);
	}
	
	@Override
	protected void endFreshData() {
		// TODO Auto-generated method stub
		super.endFreshData();
		if(isTmp){
			return;
		}
		if(mHeartRate != 0){
			Calendar calendar = Calendar.getInstance();
			calendar.set(Calendar.MILLISECOND, 0);
			long time = calendar.getTimeInMillis();
			dbUtil.insertHeartRate(time, mHeartRate);
		}
		reSetState();
	}

	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSensorChanged(SensorEvent arg0) {
		// TODO Auto-generated method stub
		int heartRate = (int) arg0.values[0];
		if(heartRate > 30 && heartRate < 200){
			mHeartRate = heartRate;
			if(mBeginTest.getVisibility() == View.VISIBLE){
				mBeginTest.setVisibility(View.GONE);
			}
			if(heartRateImageView.getVisibility() == View.VISIBLE){
				heartRateImageView.setVisibility(View.GONE);
			}
			if(mHeartRatePart.getVisibility() == View.GONE){
				mHeartRatePart.setVisibility(View.VISIBLE);
			}
			mHeartRateTextView.setText(String.valueOf(mHeartRate));
			
			mRecords.append(HeartRateActivity.DILIVER_RECORD + heartRate);
			mRecords.append(HeartRateActivity.DILIVER_TIME + System.currentTimeMillis());
		}
	}
	
	private void reSetState(){
		handler.removeMessages(0);
		handler.removeMessages(1);
		if(mHeartRateSensor != null){
			mSensorManager.unregisterListener(this, mHeartRateSensor);
			mSensorManager.unregisterListener(this,mSensorTest);
		}
		
		heartRateImageView.setBackground(null);
		heartRateImageView.setVisibility(View.VISIBLE);
		mHeartRatePart.setVisibility(View.GONE);
		mBeginTest.setText(R.string.heart_rate_begin);
		mBeginTest.setVisibility(View.VISIBLE);
		mHeartRate = 0;
	}
	
	private void beginTest(){
		mBeginTest.setText(R.string.heart_rate_is_testing);
		AnimationDrawable animationDrawable = (AnimationDrawable) getResources().getDrawable(R.anim.health_heart_anim);
		heartRateImageView.setBackground(animationDrawable);
		animationDrawable.start();
		if(mHeartRateSensor != null){
			mSensorManager.registerListener(HealthFragment.this, mHeartRateSensor, SensorManager.SENSOR_DELAY_NORMAL);
			mSensorManager.registerListener(HealthFragment.this, mSensorTest, SensorManager.SENSOR_DELAY_NORMAL);
			handler.sendEmptyMessageDelayed(1, 30 * 1000);
		}else{
			MxyLog.e("dmm s", "mHeartRateSensor == null");
		}
		MxyLog.e("dmm sensor", "mHeartRateSensor registerListener");
	}
	
}

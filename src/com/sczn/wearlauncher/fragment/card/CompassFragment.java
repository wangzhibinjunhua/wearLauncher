package com.sczn.wearlauncher.fragment.card;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AccelerateInterpolator;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sczn.wearlauncher.R;
import com.sczn.wearlauncher.activity.card.AltActivity;
import com.sczn.wearlauncher.activity.card.CompassActivity;
import com.sczn.wearlauncher.fragment.absFragment;
import com.sczn.wearlauncher.util.MxyLog;
import com.sczn.wearlauncher.util.MxyToast;
import com.sczn.wearlauncher.view.card.CompassView;

public class CompassFragment extends absFragment implements SensorEventListener {

	public static final String ARG_IS_TMP = "is_tmp";
	private boolean mStopDrawing;// 锟角凤拷停止指锟斤拷锟斤拷锟斤拷转锟侥憋拷志位
	private AccelerateInterpolator mInterpolator;// 锟斤拷锟斤拷锟接匡拷始锟斤拷锟斤拷锟斤拷锟斤拷锟戒化锟斤拷锟斤拷一锟斤拷锟斤拷锟劫的癸拷锟斤拷,锟斤拷锟斤拷一锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷
	private float mDirection;// 锟斤拷前锟斤拷锟姐方锟斤拷
	private float mTargetDegree;// 目锟疥浮锟姐方锟斤拷
	private final float MAX_ROATE_DEGREE = 1.0f;// 锟斤拷锟斤拷锟阶伙拷埽锟斤拷锟�360锟斤拷

	public static CompassFragment newInstance(boolean isTmp) {
		CompassFragment fragment = new CompassFragment();
		Bundle bdl = new Bundle();
		bdl.putBoolean(ARG_IS_TMP, isTmp);
		fragment.setArguments(bdl);
		return fragment;

	}

	private Context mContext;
	private RelativeLayout mRootView;
	private CompassView mCompassView;
	private SensorManager mSensorManager;
	private Sensor mCompassSensor, mAcceleSensor, mCalibrationSensor;
	private float mCurrentDegree = 0.0f;
	private TextView mCompassTextView, mCalibrationTextView;
	private float[] mAcceleValues = new float[3];
	private float[] mManeticValues = new float[3];
	private float mRotation = 0;
	
	private Handler handler = new Handler();

	// 锟斤拷锟斤拷歉锟斤拷锟街革拷锟斤拷锟斤拷锟阶拷锟斤拷叱蹋锟絟andler锟斤拷锟斤拷锟绞癸拷茫锟矫�20锟斤拷锟斤拷锟解方锟斤拷浠碉拷锟斤拷锟接︼拷锟斤拷锟街革拷锟斤拷锟斤拷锟阶�
	protected Runnable mCompassViewUpdater = new Runnable() {
		@Override
		public void run() {
			if (mCompassView != null && !mStopDrawing) {
				if (mDirection != mTargetDegree) {

					// calculate the short routine
					float to = mTargetDegree;
					if (to - mDirection > 180) {
						to -= 360;
					} else if (to - mDirection < -180) {
						to += 360;
					}

					// limit the max speed to MAX_ROTATE_DEGREE
					float distance = to - mDirection;
					if (Math.abs(distance) > MAX_ROATE_DEGREE) {
						distance = distance > 0 ? MAX_ROATE_DEGREE
								: (-1.0f * MAX_ROATE_DEGREE);
					}

					// need to slow down if the distance is short
					mDirection = normalizeDegree(mDirection
							+ ((to - mDirection) * mInterpolator
									.getInterpolation(Math.abs(distance) > MAX_ROATE_DEGREE ? 0.4f
											: 0.3f)));// 锟斤拷锟斤拷一锟斤拷锟斤拷锟劫讹拷锟斤拷去锟斤拷转图片锟斤拷锟斤拷细锟斤拷
					// Toast.makeText(mContext, "~~~to0====="+mDirection,
					// 0).show();
					mCompassView.updateDirection(mDirection);// 锟斤拷锟斤拷指锟斤拷锟斤拷锟斤拷转
				}
								mCompassTextView.setText(360 - (int) mDirection + getResources().getString(R.string.compass_degree));
				handler.postDelayed(mCompassViewUpdater, 20);// 20锟斤拷锟阶猴拷锟斤拷锟斤拷执锟斤拷锟皆硷拷锟斤拷锟饺讹拷时锟斤拷锟斤拷
			}
		}
	};

	// 锟斤拷锟斤拷锟斤拷锟津传革拷锟斤拷锟斤拷取锟斤拷值
	private float normalizeDegree(float degree) {
		return (degree + 720) % 360;
	}

	private boolean isTmp = false;

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
		return R.layout.fragment_card_compass;
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		mDirection = 0.0f;// 锟斤拷始锟斤拷锟斤拷始锟斤拷锟斤拷
		mTargetDegree = 0.0f;// 锟斤拷始锟斤拷目锟疥方锟斤拷
		mInterpolator = new AccelerateInterpolator();// 实锟斤拷锟斤拷锟斤拷锟劫讹拷锟斤拷锟斤拷锟斤拷

		mRootView = findViewById(R.id.compass_root);
		mCompassView = findViewById(R.id.compass_view);
		mCompassTextView = findViewById(R.id.compass_text);
		mCalibrationTextView = findViewById(R.id.compass_calibration);

//		mCalibrationTextView.setText(getString(R.string.compass_calibration));
	}

	@Override
	protected void initData() {
		// TODO Auto-generated method stub
		mSensorManager = (SensorManager) mContext
				.getSystemService(Context.SENSOR_SERVICE);
		mCompassSensor = mSensorManager
				.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
		mAcceleSensor = mSensorManager
				.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		mCalibrationSensor = mSensorManager
				.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD_UNCALIBRATED);
		
		mCalibrationTextView.setText(getString(R.string.compass_calibration));
		mCompassTextView.setText(360 + getResources().getString(R.string.compass_degree));
		initListener();
	}

	private void initListener() {
		mRootView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				try {
					Intent intent = new Intent(mContext, CompassActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					mContext.startActivity(intent);
				} catch (Exception e) {
					MxyLog.e("dmm", "apk null" + e.getMessage());
				}
			}
		});
	}

	@Override
	protected void startFreshData() {
		// TODO Auto-generated method stub
		super.startFreshData();
		MxyLog.d(this, "startFreshData--isTmp=" + isTmp);
		if (isTmp) {
			return;
		}
		mStopDrawing = false;
		handler.postDelayed(mCompassViewUpdater, 20);
		
		final long starttime = System.currentTimeMillis();
		MxyLog.d(this, "startFreshData--starttime=" + starttime);
		if (mAcceleSensor != null) {
			mSensorManager.registerListener(this, mAcceleSensor,
					SensorManager.SENSOR_DELAY_FASTEST);
		} else {
			MxyLog.e("dmm s", "mAcceleSensor == null");
		}
		if (mCompassSensor != null) {
			mSensorManager.registerListener(this, mCompassSensor,
					SensorManager.SENSOR_DELAY_FASTEST);
		} else {
			MxyLog.e("dmm s", "mCompassSensor == null");
		}
		if (mCalibrationSensor != null) {
			mSensorManager.registerListener(this, mCalibrationSensor,
					SensorManager.SENSOR_DELAY_FASTEST);
		} else {
			MxyLog.e("dmm s", "mCalibrationSensor == null");
		}
		MxyLog.d(this, "startFreshData--endtime=" + System.currentTimeMillis());
	}

	@Override
	protected void endFreshData() {
		// TODO Auto-generated method stub
		super.endFreshData();
		if (isTmp) {
			return;
		}
		mStopDrawing = true;
		
		if (mAcceleSensor != null) {
			mSensorManager.unregisterListener(this, mAcceleSensor);
		}
		if (mCompassSensor != null) {
			mSensorManager.unregisterListener(this, mCompassSensor);
		}
		if (mCalibrationSensor != null) {
			mSensorManager.unregisterListener(this, mCalibrationSensor);
		}
		// handler.removeMessages(1);
	}

	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSensorChanged(SensorEvent arg0) {
		// TODO Auto-generated method stub
		MxyLog.i(this, "onSensorChanged" + arg0.values[0]);
		switch (arg0.sensor.getType()) {
		case Sensor.TYPE_ACCELEROMETER:
			mAcceleValues = arg0.values.clone();
			break;
		case Sensor.TYPE_MAGNETIC_FIELD:
			mManeticValues = arg0.values.clone();
			break;
		case Sensor.TYPE_MAGNETIC_FIELD_UNCALIBRATED:
			int state = (int) arg0.values[0];
			if (state == 1) {
				// handler.sendEmptyMessage(1);
				mCalibrationTextView
						.setText(getString(R.string.compass_calibration_success));
				if (mCalibrationSensor != null) {
					mSensorManager.unregisterListener(this, mCalibrationSensor);
				}
			} else {
				mCalibrationTextView
						.setText(getString(R.string.compass_calibration));
			}
			break;
		}
		float[] values = new float[3];
		float[] R = new float[9];
		SensorManager.getRotationMatrix(R, null, mAcceleValues, mManeticValues);
		SensorManager.getOrientation(R, values);
		values[0] = (float) Math.toDegrees(values[0]);
		mTargetDegree = values[0];
		if (mTargetDegree < 0) {
			mTargetDegree = 360 + mTargetDegree;
		}
		mTargetDegree += mRotation;
		if (mTargetDegree > 360) {
			mTargetDegree -= 360;
		}

		// float r[] = new float[9];
		// float v[] = new float[3];
		// SensorManager.getRotationMatrix(r, null, mAcceleValues,
		// mManeticValues);
		// SensorManager.getOrientation(r, v); 
		// mTargetDegree = (float) Math.toDegrees(v[0]);
	}
}
package com.sczn.wearlauncher.fragment.card;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.AMapLocationClientOption.AMapLocationMode;
import com.sczn.wearlauncher.R;
import com.sczn.wearlauncher.fragment.absFragment;

public class AltitudeFragment extends absFragment {
	
	public static final String ARG_IS_TMP = "is_tmp";
	public static AltitudeFragment newInstance(boolean isTmp){
		AltitudeFragment fragment = new AltitudeFragment();
		Bundle bdl = new Bundle();
		bdl.putBoolean(ARG_IS_TMP, isTmp);
		fragment.setArguments(bdl);
		return fragment;
		
	}
	private Context mContext;
	private RelativeLayout mRootView;
	private TextView mAltTextView,mBadSignal;
	private int mAlt = 0;
	//高德地图定位获取海拔
	private LinearLayout mAltPart;
	private AMapLocationClient mLocationClient = null;
	private AMapLocationClientOption mLocationOption = null;
	private AMapLocationListener mLocationListener = new AMapLocationListener(){

		@Override
		public void onLocationChanged(AMapLocation location) {
			// TODO Auto-generated method stub
			int requestCode = location.getErrorCode();
			//错误列表参考：http://lbs.amap.com/api/android-location-sdk/guide/utilities/errorcode/
			switch (requestCode) {
			case 0://成功
				mBadSignal.setVisibility(View.INVISIBLE);
				progressBar.setVisibility(View.INVISIBLE);
				mAltTextView.setText(String.valueOf(location.getAltitude()));
				mAltPart.setVisibility(View.VISIBLE);
				break;
			default://当前信号不佳
				mBadSignal.setVisibility(View.VISIBLE);
				break;
			}
		}
		
	};
	
	private boolean isTmp = false;
	private ProgressBar progressBar;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Bundle bdl = getArguments();
		if(bdl != null){
			isTmp = bdl.getBoolean(ARG_IS_TMP, false);
		}
	}
	
	@Override
	protected int getLayoutResouceId() {
		// TODO Auto-generated method stub
		mContext = getActivity();
		return R.layout.fragment_card_altitude;
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		mRootView = findViewById(R.id.altitude_root);
		mAltTextView = findViewById(R.id.alt_text);
		mAltPart = findViewById(R.id.alt_part);
		mAltPart.setVisibility(View.GONE);
		mBadSignal = findViewById(R.id.bad_signal);
		progressBar = (ProgressBar)findViewById(R.id.progressBar1);
	}

	@Override
	protected void initData() {
		// TODO Auto-generated method stub
		locateSetting();
	}
	
	private void locateSetting(){
		if (mLocationClient == null) {
			mLocationClient = new AMapLocationClient(mContext);
			// 初始化定位参数
			mLocationOption = new AMapLocationClientOption();
			// 设置定位监听
			mLocationClient.setLocationListener(mLocationListener);
			// 设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
			mLocationOption
					.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
			// 设置定位间隔,单位毫秒,默认为2000ms
			mLocationOption.setInterval(2000);
			// 设置定位参数
			mLocationClient.setLocationOption(mLocationOption);
			// mLocationClient.startLocation();
		}
		
	}
	
	@Override
	protected void startFreshData() {
		// TODO Auto-generated method stub
		super.startFreshData();
		if(isTmp){
			return;
		}
		progressBar.setVisibility(View.VISIBLE);
		//启动定位
		locateSetting();
		mLocationClient.startLocation();
	}
	
	@Override
	protected void endFreshData() {
		// TODO Auto-generated method stub
		super.endFreshData();
		if(isTmp){
			return;
		}
		mAltPart.setVisibility(View.GONE);
		mBadSignal.setVisibility(View.GONE);
		if (mLocationClient!=null) {
			mLocationClient.unRegisterLocationListener(mLocationListener);
			mLocationClient = null;
		}
//		if(mBadSignal.getVisibility() == View.VISIBLE){
//			mBadSignal.setVisibility(View.GONE);
//		}
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		if (mLocationClient!=null) {
			mLocationClient.unRegisterLocationListener(mLocationListener);
			mLocationClient = null;
		}
	}
}

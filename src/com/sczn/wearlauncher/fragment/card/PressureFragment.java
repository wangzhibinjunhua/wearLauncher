package com.sczn.wearlauncher.fragment.card;


import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationClientOption.AMapLocationMode;
import com.amap.api.location.AMapLocationListener;
import com.sczn.wearlauncher.R;
import com.sczn.wearlauncher.fragment.absFragment;
import com.sczn.wearlauncher.util.MxyLog;
import com.sczn.wearlauncher.util.SharedPreferencesUtils;
import com.sczn.wearlauncher.view.card.CompassView;
import com.sczn.wearlauncher.yahooweather.WeatherHttpTask;
import com.sczn.wearlauncher.yahooweather.WeatherHttpTask.WeatherCallBack;
import com.sczn.wearlauncher.yahooweather.model.Atmosphere;
import com.sczn.wearlauncher.yahooweather.model.LocationInfo;
import com.sczn.wearlauncher.yahooweather.model.Weather;

public class PressureFragment extends absFragment{

	private RelativeLayout mRootView;
	private Context mContext;
	private CompassView mPressurePoint;
	private TextView mPressureTextView,mTempTextView,mBadSignal;
	private float mTargetDirection = 0.0f,mCurrentDirection = 0.0f;
	private float mPressure = 0.0f;
	private LinearLayout mPressurePart,mTempPart;
	private String mLastCity;
	private ProgressBar progressBar;

	
	//获取定位
	private AMapLocationClient mLocationClient = null;
	private AMapLocationClientOption mLocationOption = null;
	private AMapLocationListener mLocationListener = new AMapLocationListener(){

		@Override
		public void onLocationChanged(AMapLocation location) {
			// TODO Auto-generated method stub
			int requestCode = location.getErrorCode();
			MxyLog.e("pre", "requestCode:"+requestCode);
			//错误列表参考：http://lbs.amap.com/api/android-location-sdk/guide/utilities/errorcode/
			switch (requestCode) {
			case 0://成功
				String city = location.getCity();
				if(!city.equals(mLastCity)){
					getYahooWeather(location.getCity());
				}
				break;
			}
		}
		
	};
	
	
	private Handler handler = new Handler();
	private Runnable mPressureBarThread = new Runnable() {
		@Override
		public void run() {
			// TODO Auto-generated method stub
			if(mPressure > 0){
				if(Math.abs(mTargetDirection - mCurrentDirection) > 0.5){
					if(mCurrentDirection < mTargetDirection){
						mCurrentDirection += 0.3;
					}
					if(mCurrentDirection > mTargetDirection){
						mCurrentDirection -= 0.3;
					}
					
					mPressurePoint.updateDirection(mCurrentDirection);
				}
			}
			handler.postDelayed(mPressureBarThread, 30);
		}
	};

	@Override
	protected int getLayoutResouceId() {
		// TODO Auto-generated method stub
		mContext = getActivity();
		return R.layout.fragment_card_pressure;
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		mRootView = findViewById(R.id.pressure_root);
		mPressurePoint = findViewById(R.id.pressure_point);
		mPressureTextView = findViewById(R.id.pressure_text);
		mTempTextView = findViewById(R.id.temp_text);
		mPressurePart = findViewById(R.id.pressure_part);
		mTempPart = findViewById(R.id.temp_part);
		mPressurePart.setVisibility(View.GONE);
		mTempPart.setVisibility(View.GONE);
		mBadSignal = findViewById(R.id.bad_signal);
		progressBar = (ProgressBar)findViewById(R.id.progressBar1);
	}

	@Override
	protected void initData() {
		// TODO Auto-generated method stub
		locateSetting();
	}
	
	@Override
	protected void startFreshData() {
		// TODO Auto-generated method stub
		super.startFreshData();
		progressBar.setVisibility(View.VISIBLE);
		initData2();
		locateSetting();
		getYahooWeather(mLastCity);
		mLocationClient.startLocation();
	}
	
	@Override
	protected void endFreshData() {
		// TODO Auto-generated method stub
		super.endFreshData();
		initData2();
		progressBar.setVisibility(View.INVISIBLE);
	}
	
	private void initData2(){
		mTempPart.setVisibility(View.GONE);
		mPressurePart.setVisibility(View.GONE);
		mBadSignal.setVisibility(View.INVISIBLE);
		mTargetDirection = 0.0f;
		mCurrentDirection = 0.0f;
		mPressure = 0.0f;
		mPressurePoint.updateDirection(mCurrentDirection);
		
	}
	
	private void locateSetting(){
		mLocationClient = new AMapLocationClient(mContext);
		mLocationOption = new AMapLocationClientOption();
		mLocationClient.setLocationListener(mLocationListener);
		//设置定位模式为低精度模式，只使用网络。
		mLocationOption.setLocationMode(AMapLocationMode.Battery_Saving);
		//单次定位
		mLocationOption.setOnceLocation(true);
		//设置网络超时时间
		mLocationOption.setHttpTimeOut(8000);
		//给定位客户端对象设置定位参数
		mLocationClient.setLocationOption(mLocationOption);
		//初始化城市
		mLastCity = (String) SharedPreferencesUtils.getParam(mContext, "last_city", getString(R.string.city));
	}
	
	private void getYahooWeather(String city){
		MxyLog.e("city", city);
		WeatherHttpTask.getInstance().getWeatherInfo(city, new WeatherCallBack() {
			
			@Override
			public void onSuccess(Weather weather, LocationInfo cityInfo,
					Atmosphere atmosphere) {
				// TODO Auto-generated method stub
				String pressure = atmosphere.getPressure();
				String temp = transFormData(weather.getCondition().getTemp());
				mPressureTextView.setText(pressure);
				mTempTextView.setText(temp);
				mPressure = Float.valueOf(pressure);
				//旋转角度
				mTargetDirection = (float) ((mPressure - 1010) * 2.8);
				mBadSignal.setVisibility(View.INVISIBLE);
				progressBar.setVisibility(View.INVISIBLE);
				mPressurePart.setVisibility(View.VISIBLE);
				mTempPart.setVisibility(View.VISIBLE);
				//
				handler.postDelayed(mPressureBarThread, 1 * 1000);
			}
			
			@Override
			public void onError(String msg) {
				// TODO Auto-generated method stub
				MxyLog.e("pre", "error:"+msg);
				progressBar.setVisibility(View.INVISIBLE);
				mBadSignal.setVisibility(View.VISIBLE);
			}
		});
	}
	
	//华氏度转摄氏度
	private String transFormData(String temp) {
        int formerTemp = Integer.parseInt(temp);
        int newTemp = (int) ((formerTemp - 32) / 1.8);
        char symbol = (char) 176;//176为°的符号字符
        return String.valueOf(newTemp)+ String.valueOf(symbol);
    }
		
	
	@Override
	public void onDestroy() {
		super.onDestroy();
	}
}

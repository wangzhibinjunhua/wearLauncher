package com.sczn.wearlauncher.util;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.sczn.wearlauncher.LauncherApp;
import com.sczn.wearlauncher.yahooweather.model.Atmosphere;
import com.sczn.wearlauncher.yahooweather.model.LocationInfo;
import com.sczn.wearlauncher.yahooweather.model.Weather;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.SystemClock;
import android.speech.tts.TextToSpeech;

public class GeographicUtil {
	
	private static class Holder{
		private static GeographicUtil instance = new GeographicUtil();
	}
	public static GeographicUtil getInstance(){
		return Holder.instance;
	}
	
	public static final int FRESH_INTERVAL_MIN = 60*1000;
	private String currCity = "shenzhen";
	private LocationHelper mLocationHelper;
	private Handler mHandler;
	private IWeatherListen mWeatherListen;
	private IAltitudeListen mAltitedeListen;
	private long lastFreshTime = Long.MIN_VALUE;
	
	public GeographicUtil(){
		mLocationHelper = new LocationHelper();
		mHandler = new Handler();
	}
	
	public void start(){
		
	}
	
	public void stop(){
		
	}
	
	public void startGetWeather(IWeatherListen listen){
		this.mWeatherListen = listen;
		getWeather();
	}
	
	public void stopGetWeather(){
		mWeatherListen = null;
	}

	public void startGetAltitude(IAltitudeListen listen){
		this.mAltitedeListen = listen;
		getAltitude();
	}
	
	public void stopGetAltitude(){
		mAltitedeListen = null;
		mLocationHelper.startLocale(false);
	}

	public void getWeather(){
		final long currTime = SystemClock.currentThreadTimeMillis();
		if((currTime - lastFreshTime) < FRESH_INTERVAL_MIN){
			return;
		}
		lastFreshTime = currTime;
		mLocationHelper.startLocale(false);
	}
	public void getAltitude(){
		mLocationHelper.startLocale(true);
	}

	private class LocationHelper implements AMapLocationListener{
		
		public static final int STATE_IDLE = 0;
		public static final int STATE_LOCAL_ONCE = 1;
		public static final int STATE_LOCAL_REPEAT = 2;
		
		private int currState = STATE_IDLE;
		private AMapLocationClient mLocationClient = null;
		private String mLastCity = "unknow";
		
		public LocationHelper(){
			mLocationClient = new AMapLocationClient(LauncherApp.appContext);
		}
		
		public void startLocale(boolean isRepeat){
			switch (currState) {
				case STATE_IDLE:
					mLocationClient.setLocationOption(getLocalOption());
					mLocationClient.startLocation();
				case STATE_LOCAL_REPEAT:
				case STATE_LOCAL_ONCE:
					currState = isRepeat?STATE_LOCAL_REPEAT:STATE_LOCAL_ONCE;
					break;
				default:
					break;
			}
		}
		
		public AMapLocationClientOption getLocalOption(){
			// 初始化定位参数
			AMapLocationClientOption option = new AMapLocationClientOption();
			// 设置定位监听
			// 设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
			option.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
			// 设置定位间隔,单位毫秒,默认为2000ms
			option.setInterval(2000);
			// 设置定位参数
			
			return option;
		}

		@Override
		public void onLocationChanged(AMapLocation arg0) {
			// TODO Auto-generated method stub
			if(arg0.getErrorCode() != 0){
				return;
			}
			
			if(currState == STATE_LOCAL_ONCE){
				mLocationClient.unRegisterLocationListener(this);
				currState = STATE_IDLE;
			}
			
			final String city = arg0.getCity();
			if(city.equals(mLastCity)){
				return;
			}
			mLastCity = city;
			
		}
		
	}
	
	public interface IWeatherListen{
		void onSuccess(Weather weather, LocationInfo cityInfo,Atmosphere atmosphere);
		void onFailed();
	}
	public interface IAltitudeListen{
		void onSuccess();
	}
}

package com.sczn.wearlauncher.fragment.card;

import com.sczn.wearlauncher.Config;
import com.sczn.wearlauncher.R;
import com.sczn.wearlauncher.fragment.absFragment;
import com.sczn.wearlauncher.util.DateFormatUtil;

import android.os.Handler;
import android.widget.ImageView;
import android.widget.TextView;

public class WeatherFragment extends absFragment {

	private TextView mTime;
	private TextView mCity;
	private TextView mWeatherInfo;
	private TextView mWeatherTemp;
	private ImageView mWeatherIcon;
	
	private Handler mHandler = new Handler();
	
	@Override
	protected int getLayoutResouceId() {
		// TODO Auto-generated method stub
		return R.layout.fragment_card_weather;
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		mTime = findViewById(R.id.card_weather_time);
		mCity = findViewById(R.id.card_weather_city);
		mWeatherInfo = findViewById(R.id.card_weather_info);
		mWeatherTemp = findViewById(R.id.card_weather_temp);
		mWeatherIcon = findViewById(R.id.card_weather_icon);
	}

	@Override
	protected void initData() {
		// TODO Auto-generated method stub
		mTime.setText(DateFormatUtil.getCurrTimeString(DateFormatUtil.YYYY_MM_DD_HM));
		mCity.setText(Config.DEAFULT_CITY);
		mWeatherInfo.setText(R.string.Unkown);
		mWeatherIcon.setImageResource(R.drawable.statu_icon_sos);
		mWeatherTemp.setText(String.format(getString(R.string.weather_temp), 0));
	}

	@Override
	protected void startFreshData() {
		// TODO Auto-generated method stub
		super.startFreshData();
		mHandler.postDelayed(new Runnable(){   

		    public void run() {   
		    	mHandler.removeCallbacksAndMessages(null);
		    	mTime.setText(DateFormatUtil.getCurrTimeString(DateFormatUtil.YYYY_MM_DD_HM));
		    	mHandler.postDelayed(this,1000);
		    }   

		 }, 1000);
	}
	
	@Override
	protected void endFreshData() {
		// TODO Auto-generated method stub
		super.endFreshData();
		mHandler.removeCallbacksAndMessages(null);
	}
}

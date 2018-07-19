package com.sczn.wearlauncher.activity.card;

import java.util.Iterator;
import java.util.List;

import com.sczn.wearlauncher.R;
import com.sczn.wearlauncher.util.MxyLog;

import android.R.bool;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by mengmeng on 2016/10/25.
 */
public class CompassActivity extends Activity {

    private ImageView mScanningImageView,mReTest;
    private TextView mLatitude,mLongitude,mScanning,mLocationText; 
    private Animation mAnimation;
    private int mLatitudeValue,mLongitudeValue;
    private LocationManager mLocationManager;
    private GpsSatelliteState mSatelliteListener;
    private GpsLocation mLocationListener;
    private int mSatelliteNum = 0;
    private Handler handler = new Handler();
    private Runnable testGPS = new Runnable() {
		@Override
		public void run() {
			// TODO Auto-generated method stub
			getSatelliteInfo();
		}
	};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_compass);

        initView();
        initData();
        
    }

    private void initView(){
        mScanningImageView = (ImageView) findViewById(R.id.iv_scanning);
        mLatitude = (TextView) findViewById(R.id.latitude);
        mLongitude = (TextView) findViewById(R.id.longitude);
        mScanning = (TextView) findViewById(R.id.tv_sacnning);
        mLocationText = (TextView) findViewById(R.id.tv_current_location);
        mReTest = (ImageView) findViewById(R.id.retest_btn);
    }

    private void initData(){
        mAnimation = AnimationUtils.loadAnimation(this,R.anim.center_rotate_anim);
        LinearInterpolator linearInterpolator = new LinearInterpolator();
        mAnimation.setInterpolator(linearInterpolator);
        mScanningImageView.setAnimation(mAnimation);
        mLocationManager = (LocationManager) getSystemService("location");
        mSatelliteListener = new GpsSatelliteState();
        mLocationListener = new GpsLocation();
        mScanningImageView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(isOpenGPS()){
					mScanningImageView.setClickable(false);
					mScanning.setText(R.string.compass_locating);
					startLocation();
				}else{
					Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
					startActivity(intent);	
				}
			}
		});
        mReTest.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mLocationText.setVisibility(View.GONE);
    			mReTest.setVisibility(View.GONE);
    			mScanning.setVisibility(View.VISIBLE);
    			
    			mLatitude.setVisibility(View.GONE);
    			mLongitude.setVisibility(View.GONE);
    			mAnimation.start();
			}
		});
    }
    
    private boolean isOpenGPS(){
    	boolean isOpen = false;
    	isOpen = mLocationManager.isProviderEnabled("gps");
    	return isOpen;
    }
    
    private void updateGpsStatus(int paramInt, GpsStatus paramGpsStatus){
  	  if (paramGpsStatus == null){
  	      this.mSatelliteNum = 0;
  	      return;
  	  }
  	   
  	  if(paramInt != 4)
  		   return;
  	   
  	  int count = 0;
  	  Iterator<GpsSatellite> iterator = paramGpsStatus.getSatellites().iterator();
  	  float f = 0;
  	  while (iterator.hasNext()){
  	    f = ((GpsSatellite)iterator.next()).getSnr();
  	    if (f <30)
  	      return;
  	    count ++;
  	  }
  	  this.mSatelliteNum = count;
    }
   
    private void getSatelliteInfo(){
    	mScanning.setVisibility(View.VISIBLE);
    	mScanning.setText("GPS count:"+mSatelliteNum);
    	if (mSatelliteNum > 0){
    		mScanning.setText(R.string.compass_gps_good_text);
    		Location location = mLocationManager.getLastKnownLocation("gps");
    		if(location != null){
    			mScanning.setVisibility(View.GONE);
    			mScanningImageView.setAnimation(null);
    			mLocationText.setVisibility(View.VISIBLE);
    			mReTest.setVisibility(View.VISIBLE);
    			mLatitudeValue = (int) location.getLatitude();
        		mLongitudeValue = (int) location.getLongitude();
        		if(mLatitudeValue > 0){
        			mLatitude.setText(String.valueOf(mLatitudeValue)+getResources().getString(R.string.compass_gps_north));
        		}else{
        			mLatitudeValue = Math.abs(mLatitudeValue);
        			mLatitude.setText(String.valueOf(mLatitudeValue)+getResources().getString(R.string.compass_gps_south));
        			mLatitude.setVisibility(View.VISIBLE);
        		}
        		if(mLongitudeValue > 0){
        			mLongitude.setText(String.valueOf(mLongitudeValue)+getResources().getString(R.string.compass_gps_east));
        		}else{
        			mLongitudeValue = Math.abs(mLongitudeValue);
        			mLongitude.setText(String.valueOf(mLongitudeValue)+getResources().getString(R.string.compass_gps_west));
        			mLongitude.setVisibility(View.VISIBLE);
        		}
        		return;
    		}
      	}
    	handler.postDelayed(testGPS, 1 * 1000);
    }
    
    private void startLocation(){
    	mLocationManager.addGpsStatusListener(mSatelliteListener);
		mLocationManager.requestLocationUpdates("gps",2000L,0,mLocationListener);
		mScanningImageView.setAlpha(1.0f);
    }
    
    private void stopLocation(){
    	mLocationManager.removeGpsStatusListener(mSatelliteListener);
    	mLocationManager.removeUpdates(mLocationListener);
    	mScanningImageView.setAlpha(0.3f);
    }
    
    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

 
    class GpsSatelliteState implements GpsStatus.Listener{

		@Override
		public void onGpsStatusChanged(int state) {
			// TODO Auto-generated method stub
//			GpsStatus localGpsStatus = mLocationManager.getGpsStatus(null);
//			updateGpsStatus(state,localGpsStatus);
		}
    }
    
    class GpsLocation implements LocationListener{

		@Override
		public void onLocationChanged(Location location) {
			// TODO Auto-generated method stub
			if(location != null){
				mScanning.setVisibility(View.GONE);
				mScanningImageView.setVisibility(View.GONE);
				mLocationText.setVisibility(View.VISIBLE);
				mReTest.setVisibility(View.VISIBLE);
				mLatitudeValue = (int) location.getLatitude();
	    		mLongitudeValue = (int) location.getLongitude();
	    		if(mLatitudeValue > 0){
	    			mLatitude.setText(String.valueOf(mLatitudeValue)+getResources().getString(R.string.compass_gps_north));
	    		}else{
	    			mLatitudeValue = Math.abs(mLatitudeValue);
	    			mLatitude.setText(String.valueOf(mLatitudeValue)+getResources().getString(R.string.compass_gps_south));
	    		}
	    		if(mLongitudeValue > 0){
	    			mLongitude.setText(String.valueOf(mLongitudeValue)+getResources().getString(R.string.compass_gps_east));
	    		}else{
	    			mLongitudeValue = Math.abs(mLongitudeValue);
	    			mLongitude.setText(String.valueOf(mLongitudeValue)+getResources().getString(R.string.compass_gps_west));
	    		}
	    		mLatitude.setVisibility(View.VISIBLE);
	    		mLongitude.setVisibility(View.VISIBLE);
	    		stopLocation();
			}
		}

		@Override
		public void onProviderDisabled(String arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onProviderEnabled(String arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
			// TODO Auto-generated method stub
			
		}
    }
}

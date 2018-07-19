package com.sczn.wearlauncher.fragment.card;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sczn.wearlauncher.R;
import com.sczn.wearlauncher.activity.card.SleepActivity;
import com.sczn.wearlauncher.db.DBUtil;
import com.sczn.wearlauncher.db.bean.SleepState;
import com.sczn.wearlauncher.db.bean.SportStepsConstant;
import com.sczn.wearlauncher.db.bean.SportsStepCountInfo;
import com.sczn.wearlauncher.db.provider.Provider.SleepTimeDetailColumns;
import com.sczn.wearlauncher.fragment.absFragment;
import com.sczn.wearlauncher.util.DateFormatUtil;
import com.sczn.wearlauncher.util.MxyLog;
import com.sczn.wearlauncher.util.SharedPreferencesUtils;
import com.sczn.wearlauncher.view.card.CircleBarView;

public class SleepFragment extends absFragment {
	public static final String TAG = SleepFragment.class.getSimpleName();
	
	public static final long DURATION_IN_MILLLI = 12*60*60*1000;
	public static final long PER_HOUR = 60*60*1000;
	private Context mContext;
	private TextView mAllSleep,mDeepSleep,mLightSleep,mSleepState;
	private DBUtil dbUtil;
	
	private QueryDbTask mQueryDbTask;
	private ArrayList<SleepState> mValues;
	private String timeDeepSleep = "0.0";
	private String timeLightSleep = "0.0";
	private String timeAllSleep = "00.0";
	private double barValue = 0.0;
	
	@Override
	protected int getLayoutResouceId() {
		// TODO Auto-generated method stub
		mContext = getActivity();
		return R.layout.fragment_card_sleep;
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		mAllSleep = findViewById(R.id.total_hours_text);
		mDeepSleep = findViewById(R.id.depp_sleep_hours_text);
		mLightSleep = findViewById(R.id.light_sleep_hours_text);
		mSleepState = findViewById(R.id.sleep_state);
	}

	@Override
	protected void initData() {
		// TODO Auto-generated method stub
		dbUtil = new DBUtil(mContext);
		
		mDeepSleep.setText(timeDeepSleep);
		mLightSleep.setText(timeLightSleep);
		mAllSleep.setText(timeAllSleep);
		
		float deepHour;
		try {
			deepHour = Float.parseFloat(timeDeepSleep);
		} catch (NumberFormatException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			deepHour = 0;
		}
		if(deepHour > 2){
        	mSleepState.setText(R.string.sleep_state_good);
        }else if(deepHour > 1){
        	mSleepState.setText(R.string.sleep_state_normal);
        }else if(deepHour > 0){
        	mSleepState.setText(R.string.sleep_state_bad);
        }else if(deepHour == 0){
        	mSleepState.setText(R.string.sleep_state_null);
        }
		
		getView().setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				try {
					Intent i = new Intent(mContext, SleepActivity.class);
					i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					i.putParcelableArrayListExtra(SleepActivity.ARG_SLEEP_DATA, mValues);
					i.putExtra(SleepActivity.ARG_TIME_DEEP, timeDeepSleep);
					i.putExtra(SleepActivity.ARG_TIME_LIGHT, timeLightSleep);
					i.putExtra(SleepActivity.ARG_TIME_ALL, timeAllSleep);
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
		startGetData();
	}
	
	@Override
	protected void endFreshData() {
		// TODO Auto-generated method stub
		stopGetData();
		super.endFreshData();
	}
	
	@Override
	protected void onLocaleChanged() {
		// TODO Auto-generated method stub
		super.onLocaleChanged();
		//viewReload(mDeepSleep);
		//mAllSleep.setTextLocale(mCurrLocale);
		//viewReload(mAllSleep);
		//viewReload(mLightSleep);
		
	}
	
	private void setData(ArrayList<SleepState> values){
		this.mValues = values;
		if(mValues != null){
			final float deepHour = 10*SleepState.deepSleepTime/PER_HOUR;
			final float lightHour = 10*SleepState.lightSleepTime/PER_HOUR;
			MxyLog.d(this,"SleepState.lightSleepTime=" + SleepState.lightSleepTime + "--PER_HOUR=" + PER_HOUR + "--lightHour=" + lightHour);
			DecimalFormat decimalFormat=new DecimalFormat("0.0");
			timeDeepSleep = decimalFormat.format(deepHour/10.0);
			timeLightSleep = decimalFormat.format(lightHour/10.0);
			timeAllSleep = decimalFormat.format(deepHour/10.0 + lightHour/10.0);
			barValue = deepHour/10.0 + lightHour/10.0;
			
			mDeepSleep.setText(timeDeepSleep);
			mLightSleep.setText(timeLightSleep);
			mAllSleep.setText(timeAllSleep);
			
			if(deepHour > 2){
	        	mSleepState.setText(R.string.sleep_state_good);
	        }else if(deepHour > 1){
	        	mSleepState.setText(R.string.sleep_state_normal);
	        }else if(deepHour > 0){
	        	mSleepState.setText(R.string.sleep_state_bad);
	        }else if(deepHour == 0 && lightHour == 0){
	        	mSleepState.setText(R.string.sleep_state_null);
	        }
		}
	}
	
	private void stopGetData(){
		if(mQueryDbTask != null && !mQueryDbTask.isCancelled()){
			mQueryDbTask.cancel(true);
		}
	}
	private void startGetData(){
		stopGetData();
		mQueryDbTask = new QueryDbTask();
		mQueryDbTask.execute();
		
	}

	
	private class QueryDbTask extends AsyncTask<Void, Void, ArrayList<SleepState>>{
		
		private long getEndTime(){
			
			final Calendar calendar = Calendar.getInstance();
			calendar.set(Calendar.HOUR_OF_DAY, 9);
	        calendar.set(Calendar.MILLISECOND,0);
	        calendar.set(Calendar.SECOND,0);
	        calendar.set(Calendar.MINUTE,0);
			return calendar.getTimeInMillis();
		}
		
		@Override
		protected ArrayList<SleepState> doInBackground(Void... params) {
			// TODO Auto-generated method stub
			final long endTime = getEndTime();
			final long startTime = endTime - DURATION_IN_MILLLI;
			MxyLog.d(TAG, "startTime="  +startTime + "--endTime=" + endTime);
			String selection = SleepTimeDetailColumns.COLUMNS_END_TIME + " > " + startTime 
					+ " AND " + SleepTimeDetailColumns.COLUMNS_START_TIME + " < " + endTime ;
			if(getActivity() == null){
				return null;
			}
            Cursor cursor = getActivity().getContentResolver().query(SleepTimeDetailColumns.CONTENT_URI,null,selection,null,null);
            //MxyLog.d(this, "cursor.getColumnCount=" + cursor.getColumnCount() + "--cursor.getCount=" + cursor.getCount());
            if(cursor == null){
            	return null;
            }
            if(!cursor.moveToFirst()){
            	cursor.close();
            	return null;
            }
            final ArrayList<SleepState> sleepRecords = new ArrayList<SleepState>();
            SleepState.deepSleepTime = 0;
            SleepState.lightSleepTime = 0;
            long starTmp;
            long endTmp;
            do{

            	final int state = cursor.getInt(cursor.getColumnIndex(
            			SleepTimeDetailColumns.COLUMNS_SLEEP_STATE));
            	if(SleepState.STATE_DEEP_SLEEP != state && SleepState.STATE_LIGHT_SLEEP != state){
            		continue;
            	}

            	MxyLog.d(this, DateFormatUtil.getTimeString(DateFormatUtil.HMS, cursor.getLong(cursor.getColumnIndex(
            			SleepTimeDetailColumns.COLUMNS_START_TIME))));
            	MxyLog.d(this, DateFormatUtil.getTimeString(DateFormatUtil.HMS, cursor.getLong(cursor.getColumnIndex(
            			SleepTimeDetailColumns.COLUMNS_END_TIME))));
            	
        		final SleepState  record = new SleepState();
        		starTmp = cursor.getLong(cursor.getColumnIndex(
            			SleepTimeDetailColumns.COLUMNS_START_TIME));
        		endTmp = cursor.getLong(cursor.getColumnIndex(
            			SleepTimeDetailColumns.COLUMNS_END_TIME));
        		starTmp = starTmp >= startTime ? starTmp : startTime;
        		endTmp = endTmp <= endTime ? endTmp : endTime;
        		record.setState(state);
        		record.setStartOffset(starTmp - startTime);
        		record.setEndOffset(endTmp - startTime);
        		sleepRecords.add(record);
        		if(SleepState.STATE_DEEP_SLEEP == state){
        			SleepState.deepSleepTime += (endTmp - starTmp);
        		}else{
        			SleepState.lightSleepTime += (endTmp - starTmp);
        		}
        		if(isCancelled()){
        			cursor.close();
        			return null;
        		}
            }while(!isCancelled() && cursor.moveToNext());
            cursor.close();
            if(isCancelled()){
            	return null;
            }
			return sleepRecords;
		}

		@Override
		protected void onPostExecute(ArrayList<SleepState> result) {
			// TODO Auto-generated method stub
			if(result == null){
				return;
			}
			if(!isCancelled()){
				setData(result);
			}
		}
	}
}
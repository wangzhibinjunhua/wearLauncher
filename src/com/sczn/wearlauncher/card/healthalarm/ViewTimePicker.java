package com.sczn.wearlauncher.card.healthalarm;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import com.loc.ca;
import com.sczn.wearlauncher.R;
import com.sczn.wearlauncher.adapter.LetterIndexAdapter;
import com.sczn.wearlauncher.util.DateFormatUtil;
import com.sczn.wearlauncher.util.MxyLog;
import com.sczn.wearlauncher.view.LetterIndexView;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

public class ViewTimePicker extends LinearLayout {

	public static SimpleDateFormat HH = new SimpleDateFormat("HH",Locale.getDefault());
	public static SimpleDateFormat MM= new SimpleDateFormat("mm",Locale.getDefault());
	
	private LetterIndexView mHour;
	private LetterIndexView mMinute;
	
	private CharSequence letterHour = "0";
	private CharSequence letterMinute = "0";
	
	private boolean isFirstLayout = true;
	
	public ViewTimePicker(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		
		final Date date = new Date(Calendar.getInstance().getTimeInMillis());
		letterHour = HH.format(date);
		letterMinute = MM.format(date);
	}
	
	@Override
	protected void onFinishInflate() {
		// TODO Auto-generated method stub
		super.onFinishInflate();
		mHour = (LetterIndexView) findViewById(R.id.alarm_timepicker_hour);
		mMinute = (LetterIndexView) findViewById(R.id.alarm_timepicker_minuter);
	}
	
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		// TODO Auto-generated method stub
		super.onLayout(changed, l, t, r, b);
		
		if(isFirstLayout){
			initValue();
			isFirstLayout = false;
		}
	}
	
	private void initValue(){
		mHour.setSelectedLetter(letterHour);
		mMinute.setSelectedLetter(letterMinute);
	}
	
	public void setInitTime(CharSequence hour, CharSequence minute){
		letterHour = hour;
		letterMinute = minute;
		initValue();
	}

	public long getTimeInMill(){
		
		try {
			return (long)(Integer.parseInt(mHour.getSelectedLetter().toString())*60*60*1000
					+ Integer.parseInt(mMinute.getSelectedLetter().toString())*60*1000);
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			MxyLog.e(this, e.toString());
			return -1;
		}
	}
}

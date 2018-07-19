package com.sczn.wearlauncher.activity.card;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import com.sczn.wearlauncher.R;
import com.sczn.wearlauncher.db.bean.SleepState;
import com.sczn.wearlauncher.db.provider.Provider.SleepTimeDetailColumns;
import com.sczn.wearlauncher.util.DateFormatUtil;
import com.sczn.wearlauncher.util.MxyLog;
import com.sczn.wearlauncher.view.card.ChartView;
import com.sczn.wearlauncher.view.card.SleepChatView;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.widget.TextView;

/**
 * Created by mengmeng on 2016/12/19.
 */
public class SleepActivity extends Activity {
	public static final String ARG_SLEEP_DATA = "sleep_data";
	public static final String ARG_TIME_DEEP = "time_deep";
	public static final String ARG_TIME_LIGHT= "time_light";
	public static final String ARG_TIME_ALL = "time_all";
	private TextView mSleepDeep;
	private TextView mSleepLight;
	private TextView mSleepAll;
	private SleepChatView mSleepChatView;
	
	private ArrayList<SleepState> mValues;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        initView();
        iniData();
    }

	@Override
    protected void onDestroy() {
    	// TODO Auto-generated method stub

    	super.onDestroy();
    }
    
    private void initView() {
		// TODO Auto-generated method stub
    	setContentView(R.layout.activity_card_sleep);
    	mSleepDeep = (TextView) findViewById(R.id.sleep_deep);
    	mSleepLight = (TextView) findViewById(R.id.sleep_light);
    	mSleepAll = (TextView) findViewById(R.id.sleep_all);
    	mSleepChatView = (SleepChatView) findViewById(R.id.sleep_chat);
	}
    
    private String getIntentExtro(Intent i, String key){
    	final String value = i.getStringExtra(key);
    	return value == null ? "0.0" : value;
    }
    private void iniData() {
		// TODO Auto-generated method stub
    	final Intent i = getIntent();
		if(i != null){
			mValues = i.getParcelableArrayListExtra(ARG_SLEEP_DATA);
			mSleepChatView.setValues(mValues);
			mSleepDeep.setText(getIntentExtro(i, ARG_TIME_DEEP));
			mSleepLight.setText(getIntentExtro(i, ARG_TIME_LIGHT));
			mSleepAll.setText(getIntentExtro(i, ARG_TIME_ALL));
		}
	}

	@Override
    protected void onResume() {
    	// TODO Auto-generated method stub
    	super.onResume();

    }


}

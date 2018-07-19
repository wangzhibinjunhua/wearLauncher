package com.sczn.wearlauncher.activity.card;

import java.util.ArrayList;
import com.sczn.wearlauncher.R;
import com.sczn.wearlauncher.util.MxyLog;
import com.sczn.wearlauncher.view.card.HeartRateChatView;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

/**
 * Created by mengmeng on 2016/12/19.
 */
public class HeartRateActivity extends Activity {
	public static final String ARG_RECORDS = "records";
	public static final String DILIVER_RECORD = "#";
	public static final String DILIVER_TIME = "&"; 
	
	private ParseTask mParseTask;
	
	private TextView mAvergeValue;
	private TextView mRecordNone;
	private HeartRateChatView mHeartRateChat;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        initView();
        if(getIntent() != null){
        	final String records = getIntent().getStringExtra(ARG_RECORDS);
    		//final String records = "#55&1482415200000#55&1482415201000#103&1482415203000#65&1482415203500" + "#88&1482415210000" + "#69&1482415211100#78&1482415220000#73&1482415230000";
    		MxyLog.d(this, "onCreate--records=" + records);
        	if(records == null || records.isEmpty()){
        		mRecordNone.setVisibility(View.VISIBLE);
        		mHeartRateChat.setVisibility(View.GONE);
        	}else{
        		mRecordNone.setVisibility(View.GONE);
        		mHeartRateChat.setVisibility(View.VISIBLE);

        		mParseTask = new ParseTask();
                mParseTask.execute(records);
        	}
        }else{
        	mRecordNone.setVisibility(View.VISIBLE);
    		mHeartRateChat.setVisibility(View.GONE);
        }
    }
    
    @Override
    protected void onDestroy() {
    	// TODO Auto-generated method stub
    	if(mParseTask != null){
    		mParseTask.cancel(true);
    	}
    	super.onDestroy();
    }
    
    private void initView() {
		// TODO Auto-generated method stub
    	setContentView(R.layout.activity_card_heartrate);
    	mAvergeValue = (TextView) findViewById(R.id.heartrate_value);
    	mRecordNone = (TextView) findViewById(R.id.heartrate_none);
    	mHeartRateChat = (HeartRateChatView) findViewById(R.id.heartrate_chat);
	}

	private void freshData(ArrayList<HeartRateRecode> values) {
		if(values == null || values.isEmpty()){
			return;
		}
		mAvergeValue.setText(String.valueOf(HeartRateRecode.avergeValue));
		mHeartRateChat.setValues(values);
	}
	
	private class ParseTask extends AsyncTask<String, Void, ArrayList<HeartRateRecode>>{

		@Override
		protected ArrayList<HeartRateRecode> doInBackground(String... params) {
			// TODO Auto-generated method stub
			String[] strs = params[0].split(DILIVER_RECORD);
			
			//MxyLog.d(this, "params[0]=" + params[0] + "--strs.length=" + strs.length);
			String[] recordStr;
			
			HeartRateRecode.maxValue = Integer.MIN_VALUE;
			HeartRateRecode.startTime = Long.MAX_VALUE;
			HeartRateRecode.duration = 0;
			final ArrayList<HeartRateRecode> records = new ArrayList<HeartRateRecode>();
			
			int autoadd = 0;
			int valueAdd = 0;
			try {
				for(int i = 0; i < strs.length; i++){
					try {
						recordStr = strs[i].split(DILIVER_TIME);
						//MxyLog.d(this, "strs[i]=" + strs[i] + "--recordStr.length=" + recordStr.length);
						final long time = Long.parseLong(recordStr[1]);
						final int value = Integer.parseInt(recordStr[0]);
						//MxyLog.i(this, "time=" + time + "--value=" + value);
						if(Long.MAX_VALUE == HeartRateRecode.startTime){
							HeartRateRecode.startTime = time;
						}
						HeartRateRecode.duration = time - HeartRateRecode.startTime;
						HeartRateRecode.maxValue = Math.max(HeartRateRecode.maxValue, value);
						
						records.add(new HeartRateRecode(time - HeartRateRecode.startTime,value));
						
						valueAdd += value;
						autoadd ++;
					} catch (Exception e) {
						// TODO: handle exception
						MxyLog.e(this, e.toString());
						continue;
					}
				}
				if(autoadd != 0){
					HeartRateRecode.avergeValue = valueAdd/autoadd;
				}
			} catch (Exception e) {
				// TODO: handle exception
				MxyLog.e(this, e.toString());
				return null;
			}

			if(HeartRateRecode.maxValue >= 150){
				HeartRateRecode.maxValue = 200;
			}else if(HeartRateRecode.maxValue >= 100){
				HeartRateRecode.maxValue = 150;
			}else{
				HeartRateRecode.maxValue = 100;
			}
			
			return records;
		}

		@Override
		protected void onPostExecute(ArrayList<HeartRateRecode> result) {
			// TODO Auto-generated method stub
			if(!isCancelled()){
				freshData(result);
			}
		}

	}

	public static class HeartRateRecode{
		public static int maxValue;
		public static long startTime;
		public static long duration;
		public static int avergeValue = 0;
		private long timeoffset;
		private int value;
		
		public HeartRateRecode(long timeoffset, int value) {
			super();
			this.timeoffset = timeoffset > 0 ? timeoffset : 0;
			this.value = Math.max(Math.min(value, 200), 20);
		}
		
		public float[] getPointInChat(float width, float height, float paddingX, float paddingY){
			final float[] point = new float[2];
			point[0] = paddingX + width*timeoffset/duration;
			point[1] = paddingY + height - height*value/maxValue;
			//MxyLog.i(this, "timeoffset=" + timeoffset + "--duration=" + duration + "--point[0]=" + point[0]);
			//MxyLog.d(this, "value=" + value + "--maxValue=" + maxValue + "--point[1]=" + point[01]);
			return point;
		}
	}
}

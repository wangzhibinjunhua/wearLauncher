package com.sczn.wearlauncher.card.healthalarm;

import com.sczn.wearlauncher.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ViewWeekdayPick extends LinearLayout implements View.OnClickListener{

	private boolean isfinishInflate = false;
	
	private int mValue = 0;
	
	private TextView mMo;
	private TextView mTu;
	private TextView mWe;
	private TextView mTh;
	private TextView mFr;
	private TextView mSa;
	private TextView mSu;
	
	
	public ViewWeekdayPick(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		isfinishInflate = false;
	}

	@Override
	protected void onFinishInflate() {
		// TODO Auto-generated method stub
		super.onFinishInflate();
		
		mMo = (TextView) findViewById(R.id.weekday_mo);
		mTu = (TextView) findViewById(R.id.weekday_tu);
		mWe = (TextView) findViewById(R.id.weekday_we);
		mTh = (TextView) findViewById(R.id.weekday_th);
		mFr = (TextView) findViewById(R.id.weekday_fr);
		mSa = (TextView) findViewById(R.id.weekday_sa);
		mSu = (TextView) findViewById(R.id.weekday_su);
		
		
		mMo.setOnClickListener(this);
		mTu.setOnClickListener(this);
		mWe.setOnClickListener(this);
		mTh.setOnClickListener(this);
		mFr.setOnClickListener(this);
		mSa.setOnClickListener(this);
		mSu.setOnClickListener(this);
		
		initValue();
		isfinishInflate = true;
	}
	
	private void initValue(){
		mMo.setSelected(isWeekdaySelected(ModelAlarm.REPEAT_FLAG_MONDAY));
		mTu.setSelected(isWeekdaySelected(ModelAlarm.REPEAT_FLAG_TUESDAY));
		mWe.setSelected(isWeekdaySelected(ModelAlarm.REPEAT_FLAG_WEDNESDAY));
		mTh.setSelected(isWeekdaySelected(ModelAlarm.REPEAT_FLAG_THURSDAY));
		mFr.setSelected(isWeekdaySelected(ModelAlarm.REPEAT_FLAG_FRIDAY));
		mSa.setSelected(isWeekdaySelected(ModelAlarm.REPEAT_FLAG_SATURDAY));
		mSu.setSelected(isWeekdaySelected(ModelAlarm.REPEAT_FLAG_SUNDAY));
	}
	
	public void setInitValue(int value){
		mValue = value;
		
		if(isfinishInflate){
			initValue();
		}
	}
	
	public int getValue(){
		return mValue;
	}
	
	private void changeFlag(View v, int flag){
		if(v.isSelected()){
			mValue &= ~flag;
			v.setSelected(false);
		}else{
			mValue |= flag;
			v.setSelected(true);
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
			case R.id.weekday_mo:changeFlag(v, ModelAlarm.REPEAT_FLAG_MONDAY);break;
			case R.id.weekday_tu:changeFlag(v, ModelAlarm.REPEAT_FLAG_TUESDAY);break;
			case R.id.weekday_we:changeFlag(v, ModelAlarm.REPEAT_FLAG_WEDNESDAY);break;
			case R.id.weekday_th:changeFlag(v, ModelAlarm.REPEAT_FLAG_THURSDAY);break;
			case R.id.weekday_fr:changeFlag(v, ModelAlarm.REPEAT_FLAG_FRIDAY);break;
			case R.id.weekday_sa:changeFlag(v, ModelAlarm.REPEAT_FLAG_SATURDAY);break;
			case R.id.weekday_su:changeFlag(v, ModelAlarm.REPEAT_FLAG_SUNDAY);break;
			default:
				break;
		}
	}
	
	private boolean isWeekdaySelected(int repeatFlag){
		
		return (mValue & repeatFlag) != 0;
	}
}

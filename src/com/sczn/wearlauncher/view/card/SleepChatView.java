package com.sczn.wearlauncher.view.card;

import java.util.ArrayList;

import com.sczn.wearlauncher.R;
import com.sczn.wearlauncher.db.bean.SleepState;
import com.sczn.wearlauncher.util.MxyLog;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;

public class SleepChatView extends ChartView {

	private ArrayList<SleepState> mSleepValues;
	
	private Paint mSleepPaint;
	private int  mDeepColor;
	private int mLightColor;
	
	private float startX;
	private float startY;
	private float width;
	private float height;
	
	public SleepChatView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	protected void init(Context context, AttributeSet attrs) {
		// TODO Auto-generated method stub
		super.init(context, attrs);
		
		mSleepPaint = new Paint();
		mSleepPaint.setAntiAlias(true);
		mSleepPaint.setStyle(Paint.Style.FILL);;
		
		mDeepColor = getResources().getColor(R.color.sleep_deep);
		mLightColor = getResources().getColor(R.color.sleep_light);
	}
	
	@Override
	public void setValues(float[] values) {
		// TODO Auto-generated method stub
		//super.setValues(values);
	}
	
	@Override
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
		// TODO Auto-generated method stub
		super.onLayout(changed, left, top, right, bottom);
		
		startX = bottomGap + padding + padding/2;
		startY = fontHeight;
		//width = getMeasuredWidth() - startX*2;
		width = bottomGap * (bottomStr.length - 1);
		height = getMeasuredHeight() - startY*2;
		//MxyLog.i(this, "startX=" + startX + "--startY=" + startY + "--width=" + width + "--height=" + height);
	}
	
	@Override
	protected void drawValues(Canvas canvas) {
		// TODO Auto-generated method stub
		if(mSleepValues == null){
			return;
		}
		for(SleepState state : mSleepValues){
			switch (state.getState()) {
				case SleepState.STATE_DEEP_SLEEP:
					mSleepPaint.setColor(mDeepColor);
					//MxyLog.i(this,"STATE_DEEP_SLEEP=" + state.getRectF(width, height, startX, startY).toString());
					canvas.drawRect(state.getRectF(width, height, startX, startY),
							mSleepPaint);
					break;
				case SleepState.STATE_LIGHT_SLEEP:
					mSleepPaint.setColor(mLightColor);
					//MxyLog.i(this, "STATE_LIGHT_SLEEP=" + state.getRectF(width, height, startX, startY).toString());
					canvas.drawRect(state.getRectF(width, height, startX, startY),
							mSleepPaint);
					break;
				default:
					break;
			}
		}
	}

	public void setValues(ArrayList<SleepState> values){
		this.mSleepValues = values;
		postInvalidate();
	}
}

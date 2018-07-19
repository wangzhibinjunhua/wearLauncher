package com.sczn.wearlauncher.view.card;

import java.util.ArrayList;
import java.util.concurrent.Semaphore;

import com.sczn.wearlauncher.activity.card.HeartRateActivity.HeartRateRecode;
import com.sczn.wearlauncher.util.MxyLog;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Point;
import android.util.AttributeSet;

public class HeartRateChatView extends ChartView {

	private float startX;
	private float startY;
	private float width;
	private float height;
	private ArrayList<HeartRateRecode> mRecords;
	private ArrayList<float[]> mPoints;
	
	public HeartRateChatView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
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
		if(mRecords == null || mRecords.size() == 0){
			return;
		}
		
		float[] start = mRecords.get(0).getPointInChat(width, height, startX, startY);
		mPath.reset();
		mPath.moveTo(start[0],start[1]);
		for(HeartRateRecode recode : mRecords){
			start = recode.getPointInChat(width, height, startX, startY);
			 mPath.lineTo(start[0],start[1]);
		}
		canvas.drawPath(mPath, mPathPaint);
	}
	
	public void setValues(ArrayList<HeartRateRecode> records){
		this.mRecords = records;
		mPoints = new ArrayList<float[]>();
		setMaxValue(HeartRateRecode.maxValue);
		perValue = maxValue/dividerCount;
    	leftStr = new String[dividerCount + 1];
    	for(int i = 0; i < leftStr.length; i++){
    		leftStr[i] = String.valueOf(perValue*i);
    	}
		
		postInvalidate();
	}
}

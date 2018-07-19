package com.sczn.wearlauncher.view.menu;

import java.util.Calendar;

import com.sczn.wearlauncher.R;
import com.sczn.wearlauncher.util.MxyLog;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

public class MenuClockView extends View {
	
	public static final int SCAL_SIZE_INIT = 120;
	public static final long FRESH_DUARATION = 1000;

	private boolean mShouldFresh = false;
	
	private Drawable mClockPanel;
	private Drawable mClockHour;
	private Drawable mClockMiniter;
	private Drawable mClockSecond;
	private Drawable mClockCenter;
	
	private int mSize = SCAL_SIZE_INIT;
	private int mCenter = 0;
	private float density = 0f; 

	
	public MenuClockView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		setWillNotDraw(false);
		mClockPanel = getResources().getDrawable(R.drawable.clock_bg);
		mClockHour = getResources().getDrawable(R.drawable.clock_hour);
		mClockMiniter = getResources().getDrawable(R.drawable.clock_minuter);
		mClockSecond = getResources().getDrawable(R.drawable.clock_second);
		mClockCenter = getResources().getDrawable(R.drawable.clock_center);
		
		density = getContext().getResources().getDisplayMetrics().density;
	}
	
	public MenuClockView(Context context, int size) {
		super(context);
		// TODO Auto-generated constructor stub
		setWillNotDraw(false);
		mClockPanel = getResources().getDrawable(R.drawable.clock_bg);
		mClockHour = getResources().getDrawable(R.drawable.clock_hour);
		mClockMiniter = getResources().getDrawable(R.drawable.clock_minuter);
		mClockSecond = getResources().getDrawable(R.drawable.clock_second);
		mClockCenter = getResources().getDrawable(R.drawable.clock_center);
		
		density = getContext().getResources().getDisplayMetrics().density;
		this.mSize = size;
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

		//MxyLog.d(this, "onMeasure" + "--width=" + getMeasuredWidth());
		//mSize = Math.min(getMeasuredHeight(), getMeasuredWidth());
		setMeasuredDimension(mSize, mSize);
		MxyLog.d(this, "onMeasure" + "--width=" + getMeasuredWidth());
		mCenter = mSize / 2;
	}
	
	@Override
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
		// TODO Auto-generated method stub
		super.onLayout(changed, left, top, right, bottom);
		MxyLog.d(this, "onLayout" + "--left=" + left + "--right=" + right);
	}
	
	@Override
	protected void onAttachedToWindow() {
		// TODO Auto-generated method stub
		super.onAttachedToWindow();
		setShouldFresh(true);
	}
	
	@Override
	protected void onDetachedFromWindow() {
		// TODO Auto-generated method stub
		setShouldFresh(false);
		super.onDetachedFromWindow();
	}
	
	public void setShouldFresh(boolean shouldFresh){
		//MxyLog.d(this, "setShouldFresh--" + shouldFresh);
		this.mShouldFresh = shouldFresh;
		changeFreshState();

	}
	
	private void changeFreshState(){
		if( mShouldFresh){
			post(freshTime);
		}else{
			removeCallbacks(freshTime);
		}
	}
	

	private void drawRotationPicture(Canvas canvas, Drawable drawable, float rotateAngle){
		//MxyLog.i(this, "drawRotationPicture" + "--drawable.getIntrinsicWidth()=" + drawable.getIntrinsicWidth() + "--drawable.getIntrinsicHeight()=" + drawable.getIntrinsicHeight());
		
		final float xpix = drawable.getIntrinsicWidth()/density;
		final float ypix = drawable.getIntrinsicHeight()/density;
		int width = (int) (xpix*mSize/SCAL_SIZE_INIT);
        int height = (int) (ypix*mSize/SCAL_SIZE_INIT);
        
        width = Math.max(width, 2);
        height = Math.max(height, 2);

        canvas.save();
        canvas.rotate(rotateAngle, mCenter, mCenter);
        drawable.setBounds(mCenter - width / 2, mCenter - height / 2, mCenter + width / 2, mCenter + height / 2);
        drawable.draw(canvas);
        canvas.restore();
	}
	
	private float getHourRotation(Calendar calendar){
		float hourAngle = calendar.get(Calendar.HOUR_OF_DAY) / 12.0f * 360.0f;
        int minute = calendar.get(Calendar.MINUTE);
        return hourAngle + (minute * 30 / 60);
	}
	private float getMinuteRotation(Calendar calendar){
		return calendar.get(Calendar.MINUTE) / 60.0f * 360.0f;
	}
	private float getSecondRotation(Calendar calendar){
		return calendar.get(Calendar.SECOND) / 60.0f * 360.0f;
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		//MxyLog.d(this, "onDraw");
		Calendar calendar = Calendar.getInstance();
		drawRotationPicture(canvas, mClockPanel, 0);
		drawRotationPicture(canvas, mClockHour, getHourRotation(calendar));
		drawRotationPicture(canvas, mClockMiniter, getMinuteRotation(calendar));
		drawRotationPicture(canvas, mClockSecond, getSecondRotation(calendar));
		drawRotationPicture(canvas, mClockCenter, 0);
	}
	
	private Runnable freshTime = new Runnable() {
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			invalidate();
			postDelayed(this, FRESH_DUARATION);
		}
	};
}

package com.sczn.wearlauncher.view.menu;

import com.sczn.wearlauncher.util.MxyLog;
import com.sczn.wearlauncher.view.ScrollerTextView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.Gravity;

public class MenuTextView extends ScrollerTextView {
	public static final int SCROLLER_GAP = 10;
	public static final int SCROLLER_DISTANCE = 50;
	private Rect mRect = new Rect();
	private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
	private float offX = 0f;
	private float mStep = 1.5f;
	private String mText;

	private boolean needScroller = false;
	private int scrollerGap = SCROLLER_GAP;
	
	public MenuTextView(Context context) {
		this(context, null);
		// TODO Auto-generated constructor stub
	}

	public MenuTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		setSingleLine(true);
		setWillNotDraw(false);
	}

	@Override
	public boolean isFocused() {
		// TODO Auto-generated method stub
		return false;
	}
	
	public void startScroller(){
		removeCallbacks(freshRunable);
		post(freshRunable);
	}
	public void stopScroller(){
		removeCallbacks(freshRunable);
	}
	
	private void initPaint(){
		mText = getText().toString();
		mPaint.setColor(getCurrentTextColor());
		mPaint.setTextSize(getTextSize());
		mPaint.getTextBounds(mText, 0, mText.length(), mRect);
		//MxyLog.d(this, "getTextSize()=" + getTextSize() + "--mRect=" + mRect.toString() + "--" + mPaint.measureText(mText));
		if(mRect.width() > getMeasuredWidth()){
			needScroller = true;
			setGravity(Gravity.START);
			offX = getMeasuredWidth()/2;
			scrollerGap = (int) (offX);
		}else{
			needScroller = false;
			setGravity(Gravity.CENTER);
		}
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		initPaint();
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		if(!mText.equals(getText().toString())){
			initPaint();
		}
		
		if(!needScroller){
			super.onDraw(canvas);
			return;
		}
		float x, y;
		x = getMeasuredWidth() - offX;
		y = getMeasuredHeight() / 2 + (mPaint.descent() - mPaint.ascent()) / 2;
		//MxyLog.d(this, "x ()=" + x  + "--y=" + y + "--mText=" + mText);
		canvas.drawText(mText, x, y, mPaint);
		canvas.drawText(mText, x + mRect.width() + scrollerGap, y, mPaint);
		offX += mStep;
		if (offX >= getMeasuredWidth() + mRect.width()) {
			offX -= (mRect.width() + scrollerGap);
		}
		postDelayed(freshRunable, SCROLLER_DISTANCE);
	}
	
	private Runnable freshRunable = new Runnable() {
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			postInvalidate();
			//postDelayed(this, SCROLLER_DISTANCE);
		}
	};
}

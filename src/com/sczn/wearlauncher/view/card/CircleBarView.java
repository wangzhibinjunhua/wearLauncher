package com.sczn.wearlauncher.view.card;

import com.sczn.wearlauncher.R;
import com.sczn.wearlauncher.util.MxyLog;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

public class CircleBarView extends View{
	
	private Paint mCircleBarBg;
	
	private Paint mCircleBarProgress;
	
	private Paint mArrowPaint;
	
	private RectF mRectF;
	
	private double mBarValue;
	
	private Bitmap mArrow;
	
	private int mProgressColor;
	
	private static float SCREEN_WIDTH = 400;
	
	private static float DES = 15;
	
	private static final int BAR_WIDTH = 24;
	
	private Paint mCirclePaint;
	private int mBarColorActive;
	private int mBarColorBg;
	private int mBarWidth;
	private int mBarPaddingOut;
	private int mBarRadius;
	private int mSize;
	private int mBarValueActive;
	
	
	public CircleBarView(Context context, AttributeSet attrs) {
		this(context,attrs,0);
	}

	public CircleBarView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		// TODO Auto-generated constructor stub
		init(context,attrs);
	}
	
	private void init(Context context,AttributeSet attrs){
		
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CircleBarView);
		mBarColorBg = a.getColor(R.styleable.CircleBarView_progress_colorBg, Color.GRAY);
		mBarColorActive = a.getColor(R.styleable.CircleBarView_progress_color, Color.DKGRAY);
		mBarWidth = a.getDimensionPixelSize(R.styleable.CircleBarView_stroke_width, R.dimen.circle_bar_stroke_wodth);
		mBarPaddingOut = a.getDimensionPixelSize(R.styleable.CircleBarView_padingOut, R.dimen.circle_bar_padding_out);
		a.recycle();
		
		mCirclePaint = new Paint();
		mCirclePaint.setAntiAlias(true);
		mCirclePaint.setDither(true);
		mCirclePaint.setStyle(Paint.Style.STROKE);
		mCirclePaint.setStrokeCap(Paint.Cap.ROUND);
		mCirclePaint.setStrokeWidth(mBarWidth);
		
		mRectF = new RectF();
		
		mArrow = BitmapFactory.decodeResource(getResources(), R.drawable.card_arrow_1);
		
		//mArrowPaint = new Paint();
		//mArrowPaint.setAntiAlias(true);
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		mSize = Math.min(getMeasuredHeight(), getMeasuredWidth());
		setMeasuredDimension(mSize, mSize);
		
	}
	
	@Override
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
		// TODO Auto-generated method stub

		super.onLayout(changed, left, top, right, bottom);
		
		mRectF.top = top + mBarPaddingOut;
		mRectF.left = left + mBarPaddingOut;
		mRectF.right = right - mBarPaddingOut;
		mRectF.bottom = bottom - mBarPaddingOut;
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		mCirclePaint.setColor(mBarColorBg);
		canvas.drawArc(mRectF, 120, 300, false, mCirclePaint);
		mCirclePaint.setColor(mBarColorActive);
		canvas.drawArc(mRectF, 120, mBarValueActive, false, mCirclePaint);		
		//canvas.drawBitmap(mArrow, (float)(SCREEN_WIDTH / 2 - (SCREEN_WIDTH - DES) / 2 * Math.sin(2*Math.PI/360*30)),(float)(SCREEN_WIDTH / 2 + (SCREEN_WIDTH - DES) / 2 * Math.cos(2*Math.PI/360*30) - mArrow.getHeight() - 3), mArrowPaint);
		//super.onDraw(canvas);
	}

	public void setValue(double value) {
		if(value > 1){
			value = 1.0;
		}
		mBarValueActive = (int) (300*value);
		invalidate();
	}
	
	
}

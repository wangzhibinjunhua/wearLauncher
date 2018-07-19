package com.sczn.wearlauncher.view.menu;

import com.sczn.wearlauncher.util.MxyLog;
import com.sczn.wearlauncher.view.ClickIcon;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.util.AttributeSet;
import android.widget.ImageView;

public class MenuIconImage extends ClickIcon {
	private static final String TAG = MenuIconImage.class.getSimpleName();

	public static final float ZOOM_SCALE = 0.5f;

	private Paint mBitmapPaint;
	private Paint mBoundPaint;
	private int mRadius;
	private int mCenter;
	private Matrix mMatrix;
	private BitmapShader mBitmapShader;
	
	private int mSize;

	@SuppressLint("NewApi") public MenuIconImage(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		mBitmapPaint = new Paint();
		mBoundPaint = new Paint();
		mMatrix = new Matrix();
		setScaleType(ScaleType.FIT_XY);
		//setScaleX(ZOOM_SCALE);
		//setScaleY(ZOOM_SCALE);
	}
	
	public MenuIconImage(Context context) {
		this(context, null);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		
		mSize = Math.min(getMeasuredHeight(), getMeasuredWidth());
		setMeasuredDimension(mSize, mSize);
		mCenter = mSize / 2;
		mRadius = mSize / 2;
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		//setUpShader(canvas);
		//setUpBound(canvas);
	}
	
	private void setUpBound(Canvas canvas) {
		// TODO Auto-generated method stub
		mBoundPaint.setStyle(Paint.Style.STROKE);
		mBoundPaint.setStrokeWidth(2);
		mBoundPaint.setColor(Color.DKGRAY);

		mBoundPaint.setAntiAlias(true);
		mBoundPaint.setDither(true);
		
		canvas.drawCircle(mCenter, mCenter, mRadius -2, mBoundPaint);
	}

	private void setUpShader(Canvas canvas)
	{
		Drawable drawable = getDrawable();
		if (drawable == null)
		{
			return;
		}

		Bitmap bmp = drawableToBitamp(drawable);
		// 鐏忓摴mp娴ｆ粈璐熼惈锟介懝鎻掓珤閿涘苯姘ㄩ弰顖氭躬閹稿洤鐣鹃崠鍝勭厵閸愬懐绮崚绂竚p
		mBitmapShader = new BitmapShader(bmp, TileMode.CLAMP, TileMode.CLAMP);
		ShapeDrawable shape = new ShapeDrawable(new OvalShape());
		float scale = 1.0f;

		int bSize = Math.min(bmp.getWidth(), bmp.getHeight());
		
		if(bSize > 0){
			

			//scale = (mSize - 10) * 1.0f / bSize;
			scale = (mSize) * 1.0f / bSize;

			// shader閻ㄥ嫬褰夐幑銏㈢叐闂冪绱濋幋鎴滄粦鏉╂瑩鍣锋稉鏄忣洣閻€劋绨弨鎯с亣閹存牞锟藉懐缂夌亸锟�
			mMatrix.setScale(scale, scale);

			mBitmapShader.setLocalMatrix(mMatrix);
			mBitmapPaint.setShader(mBitmapShader);

			mBitmapPaint.setAntiAlias(true);
			mBitmapPaint.setDither(true);
			shape.getPaint().setShader(mBitmapShader); 
			//shape.setBounds(5, 5,mSize - 5,mSize - 5); 
			shape.setBounds(0, 0,mSize,mSize);
			//canvas.drawCircle(mCenter, mCenter, mRadius, mBitmapPaint);
		}else{
			
			final int offset = (mSize - bSize)/2;
			
			shape.getPaint().setShader(mBitmapShader);  
			shape.setBounds(0 + offset, 0 + offset,mSize - offset,mSize - offset);  

		}
		shape.getPaint().setAntiAlias(true);
		shape.getPaint().setDither(true);
		shape.draw(canvas); 
	}
	
	private Bitmap drawableToBitamp(Drawable drawable)
	{
		if (drawable instanceof BitmapDrawable)
		{
			BitmapDrawable bd = (BitmapDrawable) drawable;
			return bd.getBitmap();
		}
		int w = drawable.getIntrinsicWidth();
		int h = drawable.getIntrinsicHeight();
		Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		drawable.setBounds(0, 0, w, h);
		drawable.draw(canvas);
		return bitmap;
	}
}

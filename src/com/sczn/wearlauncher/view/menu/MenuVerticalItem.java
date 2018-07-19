package com.sczn.wearlauncher.view.menu;

import com.sczn.wearlauncher.R;
import com.sczn.wearlauncher.util.MxyLog;
import com.sczn.wearlauncher.util.SportListUtil;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.LinearLayout;

public class MenuVerticalItem extends LinearLayout {
	private static final String TAG = MenuVerticalItem.class.getSimpleName();
	
	private boolean isAppItem;
	private int translationMax;
	private int mParentHeight;
	private MenuVirticalRecyleView mParent;

	public MenuVerticalItem(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.MenuItem);
		isAppItem = a.getBoolean(R.styleable.MenuItem_is_app_item, true);
		translationMax = a.getDimensionPixelSize(R.styleable.MenuItem_item_translation_max, 70);
        a.recycle();
        
        setWillNotDraw(false);
	}
	
	public void setParentHeight(int parentHeight){
		this.mParentHeight = parentHeight;
	}
	
	public void setParentView(ViewGroup parent){
		if(parent instanceof MenuVirticalRecyleView){
			this.mParent = (MenuVirticalRecyleView) parent;
		}
	}
	
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		// TODO Auto-generated method stub
		super.onLayout(changed, l, t, r, b);
		if(changed){
			if(!isAppItem){
				setPivotX(getRight());
			}else{
				setPivotX(getLeft());
			}
			setPivotY(getHeight()/2);
			//post(reSizeAction);
			//doReSize();
		}
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
	}
	
	@Override
	public void offsetTopAndBottom(int offset) {
		// TODO Auto-generated method stub
		//post(reSizeAction);
		//doReSize();
		super.offsetTopAndBottom(offset);
	}

	public void reSize(float offset){
		final float scal = getSCal(offset ,getHeight());
		final float translation = getTranslationX(offset,isAppItem);
		
		setScaleX(scal);
		setScaleY(scal);
		//MxyLog.d(TAG, "reSize" + "--translation=" + translation + "--translationMax=" + translationMax);
		setTranslationX(translation);
	}

	public static float getCenterY(int parentHeight, int top, int bottom){
		return (parentHeight - top - bottom)/2;
	}
	public static float getSCal(float offset,int height){
		return (float) (1 - 0.44*offset/height);
	}
	public float getTranslationX(float offset, boolean isAppItem){
		if(isAppItem){
			return translationMax*offset/getHeight();
		}else{
			return (float) (0 - translationMax*offset/getHeight());
		}
	}
	
	public static class ReSizeAnimation extends AnimationSet{

		public ReSizeAnimation(boolean shareInterpolator) {
			super(shareInterpolator);
			// TODO Auto-generated constructor stub
		}
		
	}
}

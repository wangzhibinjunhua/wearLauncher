package com.sczn.wearlauncher.view.menu;

import com.sczn.wearlauncher.view.VerticalViewPager;

import android.content.Context;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class StyleChooseViewPager extends VerticalViewPager{

	private IStyleViewPagerSelected mStyleViewPagerSelected;
	
	public StyleChooseViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		setOnPageChangeListener(new MyChangeListener());
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		return super.dispatchTouchEvent(ev);
	}
	
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		return true;
	}
	
	
	private class MyChangeListener implements OnPageChangeListener{

    	private int scrollState = SCROLL_STATE_IDLE;
    	
		@Override
		public void onPageScrollStateChanged(int arg0) {
			// TODO Auto-generated method stub
			scrollState = arg0;
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			// TODO Auto-generated method stub
		}

		@Override
		public void onPageSelected(int arg0) {
			// TODO Auto-generated method stub
			if(mStyleViewPagerSelected != null){
				mStyleViewPagerSelected.onStyleSelected(arg0);
			}
		}	
    }
	
	public void setHorizalViewPagerSelected(IStyleViewPagerSelected listen){
		this.mStyleViewPagerSelected = listen;
	}
	public interface IStyleViewPagerSelected{
		public void onStyleSelected(int index);
	}
}

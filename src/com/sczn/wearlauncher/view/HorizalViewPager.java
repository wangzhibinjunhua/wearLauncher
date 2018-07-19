package com.sczn.wearlauncher.view;

import com.sczn.wearlauncher.adapter.LoopViewPageAdapter;
import com.sczn.wearlauncher.util.MxyLog;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewTreeObserver.OnScrollChangedListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Adapter;

public class HorizalViewPager extends ViewPager{
	
	private final static String TAG = "HorizalViewPager";
	
	private IHorizalViewPagerSelected mHorizalViewPagerSelected;

	public HorizalViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		setOnPageChangeListener(new LoopChangeListener());
	}
	
    
    @Override
	public void setCurrentItem(int item, boolean smoothScroll) {
		// TODO Auto-generated method stub
    	super.setCurrentItem(item, smoothScroll);
    	
	}

    private boolean setToLoop(int position){
		if(!isLoop()){
			return false;
		}

		//MxyLog.d(TAG, "setToLoop position=" + position + "--getAdapter().getCount()=" + getAdapter().getCount());
		if(position == getAdapter().getCount() - 1){
			setCurrentItem(1,false);
			return true;
		}else if(position == 0){
			setCurrentItem(getAdapter().getCount() - 2,false);
			return true;
		}
		return false;
	}

    private boolean isLoop(){
    	if(!(getAdapter() instanceof LoopViewPageAdapter)){
			return false;
		}
    	final LoopViewPageAdapter adapter = (LoopViewPageAdapter) getAdapter();
    	return adapter.isLoop();
    }

	private class LoopChangeListener implements OnPageChangeListener{

    	private int scrollState = SCROLL_STATE_IDLE;
    	private int targetPosition = 0;
    	
		@Override
		public void onPageScrollStateChanged(int arg0) {
			// TODO Auto-generated method stub
			scrollState = arg0;
			//MxyLog.d(this, "onPageScrollStateChanged" + arg0 + "--targetPosition=" + targetPosition);
			if(SCROLL_STATE_IDLE == arg0){
				setToLoop(targetPosition);
			}
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			// TODO Auto-generated method stub
			//MxyLog.d(this, "onPageScrolled" + arg0);
			if(scrollState == SCROLL_STATE_DRAGGING){
				//setToLoop(arg0);
			}
		}

		@Override
		public void onPageSelected(int arg0) {
			// TODO Auto-generated method stub
			targetPosition = arg0;
			//MxyLog.d(this, "onPageSelected" + arg0 + "--scrollState=" + scrollState);
			if(mHorizalViewPagerSelected != null){
				mHorizalViewPagerSelected.horizalViewPageSelected(arg0);
			}
		}
    }
	
	public void setHorizalViewPagerSelected(IHorizalViewPagerSelected listen){
		this.mHorizalViewPagerSelected = listen;
	}
	public interface IHorizalViewPagerSelected{
		public void horizalViewPageSelected(int index);
	}

}

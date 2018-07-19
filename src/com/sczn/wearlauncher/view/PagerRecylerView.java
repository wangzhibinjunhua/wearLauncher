package com.sczn.wearlauncher.view;

import com.sczn.wearlauncher.adapter.ClockSkinChooseAdapter;
import com.sczn.wearlauncher.util.MxyLog;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver.OnTouchModeChangeListener;

public class PagerRecylerView extends MyRecyclerView{
	private final static String TAG = PagerRecylerView.class.getSimpleName();

	public final static int FLING_DIEABLE = -1;
	public static final int INVALUED_POSITION = -1;
	public static final float INVALUED_DOWN = -1.0F;
	private int flingVelocity = 1;
	private int initPosition = INVALUED_POSITION;
	
	private float mDownY = INVALUED_DOWN;
	
	private PageLayoutManager mPageLayoutManager;
	
	public PagerRecylerView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}
	
	public void initLayoutManager(int oration, boolean isResver){
		mPageLayoutManager = new PageLayoutManager(getContext(), oration, isResver);
		setLayoutManager(mPageLayoutManager);
	}
	
	public void setFlingVelocity(int scale){
		this.flingVelocity = scale;
	}
	
	public void setInitPosition(int position){
		this.initPosition = position;
	}
	
	@Override
	protected void onMeasure(int widthSpec, int heightSpec) {
		// TODO Auto-generated method stub
		super.onMeasure(widthSpec, heightSpec);
		if(INVALUED_POSITION != initPosition){
			scrollToPosition(initPosition);
			initPosition = INVALUED_POSITION;
		}
	}
	
	public void scollerToPositionWithOffset(int position, int offset){
		if(getLayoutManager() == null || !(getLayoutManager() instanceof PageLayoutManager)){
			return;
		}
		final PageLayoutManager layoutManager = (PageLayoutManager) getLayoutManager();
		layoutManager.scrollToPositionWithOffset(position, offset);
	}
	
	@Override
	public void setLayoutManager(LayoutManager layout) {
		// TODO Auto-generated method stub
		if(!(layout instanceof PageLayoutManager)){
			//throw new IllegalArgumentException("only PageLayoutManager alowed for PageRecylerView.");
		}
		super.setLayoutManager(layout);
	}
	
	@Override
	public boolean fling(int velocityX, int velocityY) {
		// TODO Auto-generated method stub
		if(FLING_DIEABLE == flingVelocity){
			return false;
		}
		if(flingVelocity <= 1){
			flingVelocity = 1;
		}
		//MxyLog.i(TAG, "fling" + "--velocityX=" + velocityX + "--velocityY=" + velocityY);
		velocityX /= flingVelocity;
		velocityY /= flingVelocity;
		return super.fling(velocityX, velocityY);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent arg0) {
		// TODO Auto-generated method stub
		
		//MxyLog.d(TAG, "onTouchEvent--arg0=" + arg0);
		final boolean result =  super.onTouchEvent(arg0);
		switch (arg0.getAction()) {
			case MotionEvent.ACTION_DOWN:
				mDownY = arg0.getY();
				break;
			case MotionEvent.ACTION_UP:
				if(FLING_DIEABLE == flingVelocity){
					if(INVALUED_DOWN != mDownY){
						scrollerToNext(arg0.getY(), mDownY);
						mDownY = INVALUED_DOWN;
					}
				}else{
					if(FLING_DIEABLE != flingVelocity){
						if(SCROLL_STATE_IDLE == getScrollState()){
							//scrollerByPager();
						}
					}
				}
				break;
			case MotionEvent.ACTION_MOVE:
				if(INVALUED_DOWN == mDownY){
					mDownY = arg0.getY();
				}
			default:
				break;
		}
		return result;
	}
	
	private void scrollerToNext(float upY,float downY){
		//MxyLog.d(TAG, "scrollerToNext--upY=" + upY + "--downY=" + downY);
		if(mPageLayoutManager == null){
			return;
		}
		if(getChildCount() < 2){
			return;
		}
		final int centerY = getHeight()/2;
		if(upY > downY){
			//final int firstPosition = mPageLayoutManager.findFirstVisibleItemPosition();
			//MxyLog.d(TAG, "scrollerToNext--firstPosition=" + firstPosition);
			//smoothScrollToPosition(firstPosition);
			final View currView = getChildAt(0);
			final int currOffset = getViewCenterY(currView) - centerY;
			//MxyLog.d(TAG, "scrollerToNext--currOffset=" + currOffset);
			smoothScrollBy(0, currOffset);
		}else{
			//final int lastPosition = mPageLayoutManager.findLastVisibleItemPosition();
			//MxyLog.d(TAG, "scrollerToNext--lastPosition=" + lastPosition);
			//smoothScrollToPosition(lastPosition);
			final View nextView = getChildAt(1);
			final int nextOffset = getViewCenterY(nextView) - centerY;
			//MxyLog.d(TAG, "scrollerToNext--nextOffset=" + nextOffset);
			smoothScrollBy(0, nextOffset);
		}
	}
	
	private void scrollerByPager() {
		// TODO Auto-generated method stub
		if(getLayoutManager() == null){
			return;
		}
		if(getChildCount() < 2){
			return;
		}
		final View currView = getChildAt(0);
		final View nextView = getChildAt(1);
		if(getLayoutManager().canScrollHorizontally()){
			final int centerX = getWidth()/2;
			final int currOffset = getViewCenterX(currView) - centerX;
			final int nextOffset = getViewCenterX(nextView) - centerX;
			
			//MxyLog.d(TAG, "scrollerByPager" + "--centerX=" + centerX + "--currOffset=" + currOffset + "--nextOffset=" + nextOffset);
			if(Math.abs(currOffset) < Math.abs(nextOffset)){
				smoothScrollBy(currOffset, 0);
			}else{
				smoothScrollBy(nextOffset, 0);
			}
		}else{
			final int centerY = getHeight()/2;
			final int currOffset = getViewCenterY(currView) - centerY;
			final int nextOffset = getViewCenterY(nextView) - centerY;
			if(Math.abs(currOffset) < Math.abs(nextOffset)){
				smoothScrollBy(0, currOffset);
			}else{
				smoothScrollBy(0, nextOffset);
			}
		}
	}
	
	private int getViewCenterX(View view){
		final LayoutParams params = (LayoutParams) view.getLayoutParams();
		final int centerX = (view.getRight() - view.getLeft())/2 + view.getLeft();
		if(params != null){
			//return  centerX + params.leftMargin;
		}
		return centerX;
	}
	
	private int getViewCenterY(View view){
		final LayoutParams params = (LayoutParams) view.getLayoutParams();
		final int centerY = (view.getBottom() - view.getTop())/2 + view.getTop();
		if(params != null){
			//return  centerY + params.topMargin;
		}
		return centerY;
	}

	public class PageLayoutManager extends LinearLayoutManager{

		public PageLayoutManager(Context context, int orientation,
				boolean reverseLayout) {
			super(context, orientation, reverseLayout);
			// TODO Auto-generated constructor stub
		}
		
		
		
		
		@Override
		public void onScrollStateChanged(int state) {
			// TODO Auto-generated method stub
			super.onScrollStateChanged(state);
			//MxyLog.d(TAG, "onScrollStateChanged=" + state);
			if(SCROLL_STATE_IDLE == state){
				scrollerByPager();
			}
		}
		
		@Override
		public void scrollToPosition(int position) {
			// TODO Auto-generated method stub
			
			/*
			final View item = findViewByPosition(0);
			if(item == null){
				return;
			}
			final LayoutParams params = (LayoutParams) item.getLayoutParams();
			int offset = 0;
			if(params != null){
				if(HORIZONTAL == getOrientation()){
					offset += params.leftMargin;
				}else{
					offset += params.topMargin;
				}
			}
			super.scrollToPositionWithOffset(position, offset);*/
			if(getAdapter() == null || !(getAdapter() instanceof ClockSkinChooseAdapter)){
				super.scrollToPosition(position);
				return;
			}
			
			final ClockSkinChooseAdapter adapter = (ClockSkinChooseAdapter) getAdapter();
			if(HORIZONTAL == getOrientation()){
				//MxyLog.d(TAG, "scrollToPosition--getMeasuredWidth()=" + getMeasuredWidth() + "--adapter.getBoundGapScale()=" + adapter.getBoundGapScale());
				super.scrollToPositionWithOffset(position, adapter.getBoundGap(getMeasuredWidth()));
			}else{
				super.scrollToPositionWithOffset(position, adapter.getBoundGap(getMeasuredHeight()));
				//MxyLog.d(TAG, "scrollToPosition--getMeasuredWidth()*adapter.getMeasuredHeight()=" + getMeasuredHeight()*adapter.getBoundGapScale());
			}
		}
	}
}

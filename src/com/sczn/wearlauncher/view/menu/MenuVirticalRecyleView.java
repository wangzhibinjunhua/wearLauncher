package com.sczn.wearlauncher.view.menu;

import com.sczn.wearlauncher.adapter.MenuVirticalAdapter;
import com.sczn.wearlauncher.model.AppMenu;
import com.sczn.wearlauncher.util.MxyLog;
import com.sczn.wearlauncher.view.MyRecyclerView;

import android.content.Context;

import android.support.v7.widget.LinearLayoutManager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;


public class MenuVirticalRecyleView extends MyRecyclerView{
	private static final String TAG = MenuVirticalRecyleView.class.getSimpleName();
	
	public static int FLING_DISABLE = -1;
	private MenuVerticalBg mMenuSportBg;
	private MenuVirticalAdapter mMenuVirticalAdapter;
	private MenuVerticalLayoutManager mVerticalLayoutManager;
	private int velocityScale = 3;
	private boolean isForApp = true;

	public MenuVirticalRecyleView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		setWillNotDraw(false);
	}
	
	public void initLayoutManager(int oration, boolean isResver){
		mVerticalLayoutManager = new MenuVerticalLayoutManager(getContext(), oration, isResver);
		setLayoutManager(mVerticalLayoutManager);
	}
	
	public void setVelocityScale(int scale){
		this.velocityScale = scale;
	}
	
	@Override
	protected void onMeasure(int widthSpec, int heightSpec) {
		// TODO Auto-generated method stub
		super.onMeasure(widthSpec, heightSpec);
		final int size = Math.min(getMeasuredHeight(), getMeasuredWidth());
		setMeasuredDimension(size, size);
	}
	
	public void setBg(MenuVerticalBg view){
		if(!isForApp){
			this.mMenuSportBg = view;
		}
	}
	public void changeBg(AppMenu menu, float offset){
		if(mMenuSportBg != null){
			mMenuSportBg.freshBg(menu, offset);
		}
	}
	
	@Override
	public void setAdapter(Adapter adapter) {
		// TODO Auto-generated method stub
		if(!(adapter instanceof MenuVirticalAdapter)){
			throw new IllegalArgumentException("MenuVirticalRecyleView " +
					"can only set a adapter instance of MenuVirticalAdapter");
		}
		mMenuVirticalAdapter = (MenuVirticalAdapter) adapter;
		isForApp = mMenuVirticalAdapter.isApp();
		super.setAdapter(adapter);
		
	}
	
	
	@Override
	public boolean onTouchEvent(MotionEvent arg0) {
		// TODO Auto-generated method stub
		ensureToLooper();
		final boolean result = super.onTouchEvent(arg0);
		//ensureToLooper();
		return result;
	}
	
	@Override
	public boolean fling(int velocityX, int velocityY) {
		// TODO Auto-generated method stub
		if(FLING_DISABLE == velocityScale){
			return false;
		}
		return super.fling(velocityX/velocityScale, velocityY/velocityScale);
	}
	
	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		// TODO Auto-generated method stub
		super.onScrollChanged(l, t, oldl, oldt);
		ensureToLooper();
	}
	
	private void ensureToLooper() {
		// TODO Auto-generated method stub
		if(mMenuVirticalAdapter == null){
			return;
		}
		if(!mMenuVirticalAdapter.isLoop()){
			return;
		}
		
		if(mVerticalLayoutManager != null){
			mVerticalLayoutManager.scrollerToLooper();
		}
	}
	
	private void freshChildrens(int dy){
		final int childCount = getChildCount();
		for(int i=0; i<childCount; i++){
			final MenuVerticalItem item = (MenuVerticalItem) getChildAt(i);
			final float offset = MenuVerticalItem.getCenterY(getHeight(),
					item.getTop(), item.getBottom());
			final float absOffset = Math.abs(offset);

			item.reSize(absOffset);
			item.offsetTopAndBottom(dy);
			if(mMenuSportBg != null){
				final AppMenu menu = (AppMenu) item.getTag();
				mMenuSportBg.freshBg(menu, 1 - absOffset/item.getHeight());
			}
		}
	}

	private class MenuVerticalLayoutManager extends LinearLayoutManager{

		public MenuVerticalLayoutManager(Context context, int orientation,
				boolean reverseLayout) {
			super(context, orientation, reverseLayout);
			// TODO Auto-generated constructor stub
		}
		
		@Override
		public void onLayoutChildren(Recycler arg0, State arg1) {
			// TODO Auto-generated method stub
			super.onLayoutChildren(arg0, arg1);
			//resizeItem();
			freshChildrens(0);
		}
		
		@Override
		public void offsetChildrenHorizontal(int dx) {
			// TODO Auto-generated method stub
			super.offsetChildrenHorizontal(dx);
		}
		
		@Override
		public void offsetChildrenVertical(int dy) {
			// TODO Auto-generated method stub
			//super.offsetChildrenVertical(dy);
			freshChildrens(dy);
		}
		
		@Override
		public void onScrollStateChanged(int state) {
			// TODO Auto-generated method stub
			super.onScrollStateChanged(state);
			if(SCROLL_STATE_IDLE == state){
				scrollerByItem();
			}
		}	
		
		public void scrollerToLooper(){
			final int firstPosition = findFirstVisibleItemPosition();
			final int size = mMenuVirticalAdapter.getMenus().size();
			if(firstPosition <= size){
				if(HORIZONTAL == getOrientation()){
					scrollToPositionWithOffset(firstPosition + size,
							getChildAt(0).getLeft());
				}else{
					scrollToPositionWithOffset(firstPosition + size,
							getChildAt(0).getTop());
				}
			}else if(firstPosition > size*2 - 1){
				if(HORIZONTAL == getOrientation()){
					scrollToPositionWithOffset(firstPosition%size + size,
							getChildAt(0).getLeft());
				}else{
					scrollToPositionWithOffset(firstPosition%size + size,
							getChildAt(0).getTop());
				}
			}
		}
		public void scrollerByItem(){
			final View view = findChildViewUnder(getWidth()/2, getHeight()/2);
			//MxyLog.d(TAG, "scrollerByItem--centerview=" + view);
			if(view != null){
				if(HORIZONTAL == getOrientation()){
					
				}else{
					final int offset = view.getTop() + view.getHeight()/2;
					if(offset >= 1){
						smoothScrollBy(0, offset - getHeight()/2);
					}
				}
			}
		}
	}

}

package com.sczn.wearlauncher.view.menu;

import java.util.ArrayList;
import java.util.List;

import com.sczn.wearlauncher.R;
import com.sczn.wearlauncher.util.MxyLog;
import com.sczn.wearlauncher.view.ClickIcon;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.MeasureSpec;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout.LayoutParams;

public class MenuCirclePager extends FrameLayout{
	private final static String TAG = MenuCirclePager.class.getSimpleName();
	
	private int maxCount;
	private int menuCount;
	private ArrayList<MenuIconView> mMenus;
	
	private int parentCenterX;
	private int parentCenterY;
	
	private int parentSize;
	private int menuIconSize;
	private int menuRadium;
	
	private int clockSize;
	private MenuClockView mClockView;

	public MenuCirclePager(Context context,ViewGroup parent, int menuCount) {
		this(context, parent, menuCount, menuCount);
	}
	public MenuCirclePager(Context context,ViewGroup parent, int maxCount, int menuCount){
		super(context);
		
		this.maxCount = maxCount;
		this.menuCount = menuCount;
		final Resources resoures = context.getResources();
		final int weightAll = resoures.getInteger(R.integer.circle_menu_pager_weight);
		final int weightRadius = resoures.getInteger(R.integer.circle_menu_pager_weight_radius);
		final int weightIconSize = resoures.getInteger(R.integer.circle_menu_pager_weight_icon_size);
		final int weightClockSize = resoures.getInteger(R.integer.circle_menu_pager_weight_clock_size);
		
		parentCenterX = parent.getWidth()/2;
		parentCenterY = parent.getHeight()/2;
		
		final int offset = parent.getWidth() - parent.getHeight();
		if(offset >= 0){
			parentSize = parent.getHeight();
			setPadding(offset/2, 0, offset/2, 0);
		}else{
			parentSize = parent.getWidth();
			setPadding(0, Math.abs(offset)/2, 0, Math.abs(offset)/2);
		}
		menuIconSize = parentSize*weightIconSize/weightAll;
		menuRadium = parentSize*weightRadius/weightAll;
		clockSize = parentSize*weightClockSize/weightAll;
		
		
		mMenus = new ArrayList<MenuCirclePager.MenuIconView>();
		initView();
	}
	
	public void setMenuCount(int menucount){
		if(this.menuCount != menucount){
			initView();
			invalidate();
		}
	}
	
	public ArrayList<MenuIconView> getIconList(){
		if(mMenus == null){
			mMenus = new ArrayList<MenuCirclePager.MenuIconView>();
		}
		return mMenus;
	}
	
	private void initView(){
		removeAllViews();
		mMenus.clear();
		
		final int diliverCount = maxCount;
		final int rotation = 360/diliverCount;
		LayoutParams params = new LayoutParams(menuIconSize, menuIconSize);
		for(int i = 0; i< diliverCount; i++){
			final MenuIconView icon = new MenuIconView(getContext(), menuIconSize, i);
			icon.setCentetX((int) (parentCenterX + menuRadium*Math.cos(Math.toRadians(rotation*i - 90))));
			icon.setCenterY((int) (parentCenterY + menuRadium*Math.sin(Math.toRadians(rotation*i - 90))));
			//MxyLog.i(TAG, "initPosition" + "--icon.getIndex()" + icon.getIndex() + "--icon.getCentetX()" + icon.getCentetX() + "-icon.getCenterY()" + icon.getCenterY());
			mMenus.add(icon);
			addView(icon, params);
			icon.setImageResource(R.drawable.ic_launcher);
		}
		
		LayoutParams clockParams = new LayoutParams(menuIconSize, menuIconSize,Gravity.CENTER);
		MxyLog.i(this, "clockSize=" + clockSize);
		mClockView = new MenuClockView(getContext(), clockSize);
		addView(mClockView, clockParams);
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		setMeasuredDimension(parentSize + getPaddingLeft() + getPaddingRight(),
				parentSize + getPaddingTop() + getPaddingBottom());
		for(int i=0; i<getChildCount(); i++){
			//getChildAt(i).measure(menuIconSize, menuIconSize);
		}
		//mClockView.measure(clockSize, clockSize);
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		// TODO Auto-generated method stub
		//MxyLog.d(TAG, "onLayout" + "--l=" + l + "--t=" + t + "--r=" + r + "--b=" + b);
		for(int i=0; i<getChildCount(); i++){
			if(getChildAt(i) instanceof MenuIconView){
				final MenuIconView icon = (MenuIconView) getChildAt(i);
				icon.layout(icon.getNewLeft(),
						icon.getNewTop(),
						icon.getNewRight(),
						icon.getNewBottom());
			}
		}	
		
		mClockView.layout(parentCenterX - clockSize/2, parentCenterY - clockSize/2,
				parentCenterX + clockSize/2, parentCenterY + clockSize/2);
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		mClockView.draw(canvas);
	}

	public static class MenuIconView extends MenuIconImage{
		
		private int index;
		private int size;
		
		private int centetX;
		private int centerY;
		
		private int newLeft;
		private int newRight;
		private int newTop;
		private int newBottom;

		public MenuIconView(Context context, int size, int index) {
			super(context, null);
			// TODO Auto-generated constructor stub
			this.index = index;
			this.size = size;
		}

		public int getIndex() {
			return index;
		}

		public int getCentetX() {
			return centetX;
		}

		public void setCentetX(int centetX) {
			this.centetX = centetX;
			newLeft = centetX - size/2;
			newRight = centetX + size/2;
		}

		public int getCenterY() {
			return centerY;
		}

		public void setCenterY(int centerY) {
			this.centerY = centerY;
			newTop = centerY - size/2;
			newBottom = centerY + size/2;
		}

		public int getNewLeft() {
			return newLeft;
		}

		public int getNewRight() {
			return newRight;
		}
		public int getNewTop() {
			return newTop;
		}

		public int getNewBottom() {
			return newBottom;
		}

		@Override
		protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
			// TODO Auto-generated method stub
			//MxyLog.d(TAG, "onMeasure--widthMeasureSpec=" + widthMeasureSpec + "--heightMeasureSpec=" + heightMeasureSpec);
			super.onMeasure(widthMeasureSpec, heightMeasureSpec);
			//MxyLog.d(TAG, "onMeasure--size=" + size + "--getMeasuredHeight=" + getMeasuredHeight() + "--getMeasuredWidth=" + getMeasuredWidth());
		}
	}
}

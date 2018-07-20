package com.sczn.wearlauncher.view.menu;

import java.util.ArrayList;
import java.util.List;

import com.sczn.wearlauncher.R;
import com.sczn.wearlauncher.util.MxyLog;
import com.sczn.wearlauncher.view.ScrollerTextView;
import com.sczn.wearlauncher.view.ClickIcon;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewDebug.ExportedProperty;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MenuSquarePager extends ViewGroup {
	private static final String TAG = MenuSquarePager.class.getSimpleName();

	public static final int MENU_COUNT = 4;
	private ViewGroup mParent;
	private final int textSize;
	private int parentSize;
	private int menuWidth;
	private int menuHeight;
	private int gapLeft;
	private int gapTop;
	private int gapMenuH;
	private int gapMenuV;
	
	private int menuTextHeight;

	private List<MenuSquareUnit> mMenus;
	
	public MenuSquarePager(Context context,ViewGroup parent, boolean isStyle) {
		super(context);
		// TODO Auto-generated constructor stub
		this.mParent = parent;
		final Resources resoures = context.getResources();
		
		if(isStyle){
			textSize = resoures.getDimensionPixelSize(R.dimen.menu_square_textsize_style);
		}else{
			textSize = resoures.getDimensionPixelSize(R.dimen.menu_square_textsize);
		}
				
		final int weightAll = resoures.getInteger(R.integer.menu_square_pager_weight);
		final int weightmenuW = resoures.getInteger(R.integer.menu_square_pager_menu_width);
		final int weightmenuH = resoures.getInteger(R.integer.menu_square_pager_menu_height);
		final int weightgapTop = resoures.getInteger(R.integer.menu_square_pager_gap_top);
		final int weightgapLeft = resoures.getInteger(R.integer.menu_square_pager_gap_left);
		final int weightgapMenuH = resoures.getInteger(R.integer.menu_square_pager_gap_menu_h);
		final int weightgapMenuV = resoures.getInteger(R.integer.menu_square_pager_gap_menu_v);
		
		final int offset = parent.getWidth() - parent.getHeight();
		if(offset >= 0){
			parentSize = parent.getHeight();
			setPadding(offset/2, 0, offset/2, 0);
		}else{
			parentSize = parent.getWidth();
			setPadding(0, Math.abs(offset)/2, 0, Math.abs(offset)/2);
		}
		
		//MxyLog.d(TAG, "MenuSquarePager" + "--parentSize=" + parentSize);
		
		menuWidth = parentSize*weightmenuW/weightAll;
		menuHeight = parentSize*weightmenuH/weightAll;
		gapTop = parentSize*weightgapTop/weightAll;
		gapLeft = parentSize*weightgapLeft/weightAll;
		gapMenuH = parentSize*weightgapMenuH/weightAll;
		gapMenuV = parentSize*weightgapMenuV/weightAll;
		
		menuTextHeight = parentSize*weightgapMenuV/weightAll;
		
		mMenus = new ArrayList<MenuSquarePager.MenuSquareUnit>();
		initView();
	}
	
	@Override
	@ExportedProperty(category = "focus")
	public boolean isFocused() {
		// TODO Auto-generated method stub
		//return super.isFocused();
		return true;
	}
	
	private void initView(){
		mMenus.clear();
		
		int i = 0;
		
		int startX = getPaddingLeft()  +gapLeft;
		int startY = getPaddingTop() + gapTop;
		final MenuSquareUnit menu11 = new MenuSquareUnit(getContext(),i++,
				startY,startY + menuHeight,startX,startX + menuWidth);
		startX += (menuWidth + gapMenuH);
		final MenuSquareUnit menu12 = new MenuSquareUnit(getContext(),i++,
				startY,startY + menuHeight,startX,startX + menuWidth);
		startX += (menuWidth + gapMenuH);
		//final MenuSquareUnit menu13 = new MenuSquareUnit(getContext(),i++,
		//		startY,startY + menuHeight,startX,startX + menuWidth);
		
		startX = getPaddingLeft() + gapLeft;
		startY += (menuHeight + gapMenuV);
		final MenuSquareUnit menu21 = new MenuSquareUnit(getContext(),i++,
				startY,startY + menuHeight,startX,startX + menuWidth);
		startX += (menuWidth + gapMenuH);
		final MenuSquareUnit menu22 = new MenuSquareUnit(getContext(),i++,
				startY,startY + menuHeight,startX,startX + menuWidth);
		startX += (menuWidth + gapMenuH);
		//final MenuSquareUnit menu23 = new MenuSquareUnit(getContext(),i++,
		//		startY,startY + menuHeight,startX,startX + menuWidth);
		
		mMenus.add(menu11);
		mMenus.add(menu12);
		//mMenus.add(menu13);
		mMenus.add(menu21);
		mMenus.add(menu22);
		//mMenus.add(menu23);
		
		for(MenuSquareUnit icon : mMenus){
			addView(icon);
			icon.getIconView().setImageResource(R.drawable.ic_launcher);
		}
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
	
		setMeasuredDimension(parentSize + getPaddingLeft() + getPaddingRight(),
				parentSize + getPaddingTop() + getPaddingBottom());
		for(int i=0; i<getChildCount(); i++){
			getChildAt(i).measure(menuWidth, menuHeight);
		}
	}
	
	@Override
	protected void onLayout(boolean arg0, int arg1, int arg2, int arg3, int arg4) {
		// TODO Auto-generated method stub
		//MxyLog.d(TAG, "onLayout" + "--getChildCount()=" + getChildCount());
		for(int i=0; i<getChildCount(); i++){
			if(getChildAt(i) instanceof MenuSquareUnit){
				final MenuSquareUnit icon = (MenuSquareUnit) getChildAt(i);
				icon.layout(icon.getNewLeft(),
						icon.getNewTop(),
						icon.getNewRight(),
						icon.getNewBottom());
			}
		}
	}

	public class MenuSquareUnit extends LinearLayout{
		
		private int posiiton;
		private MenuTextView nameView;
		private MenuIconImage iconView;
		private int newTop;
		private int newBottom;
		private int newLeft;
		private int newRight;

		public MenuSquareUnit(Context context, int posiiton, int newTop,
				int newBottom, int newLeft, int newRight) {
			super(context);
			this.posiiton = posiiton;
			this.newTop = newTop;
			this.newBottom = newBottom;
			this.newLeft = newLeft;
			this.newRight = newRight;
			
			getNameView();
			getIconView();
		}

		public int getPosiiton() {
			return posiiton;
		}

		public TextView getNameView() {
			if(nameView == null){
				nameView = new MenuTextView(getContext());
				nameView.setTextSize(textSize);
				//LayoutParams params = new LayoutParams(menuWidth, menuTextHeight);
				LayoutParams params = new LayoutParams(menuWidth, android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
				MenuSquareUnit.this.addView(nameView, params);
				//nameView.setGravity(Gravity.CENTER);
			}
			return nameView;
		}

		public MenuIconImage getIconView() {
			if(iconView == null){
				iconView = new MenuIconImage(getContext());
				LayoutParams params = new LayoutParams(menuWidth, menuWidth);
				MenuSquareUnit.this.addView(iconView, params);
				//iconView.setScaleType(ScaleType.FIT_XY);
			}
			return iconView;
		}

		public int getNewTop() {
			return newTop;
		}

		public int getNewBottom() {
			return newBottom;
		}

		public int getNewLeft() {
			return newLeft;
		}

		public int getNewRight() {
			return newRight;
		}
		
		@Override
		protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
			// TODO Auto-generated method stub
			//MxyLog.d(TAG, "onMeasure" + "--widthMeasureSpec=" + widthMeasureSpec + "--heightMeasureSpec=" + heightMeasureSpec);
			super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		}

		@Override
		protected void onLayout(boolean arg0, int arg1, int arg2, int arg3,
				int arg4) {
			// TODO Auto-generated method stub
			//MxyLog.d(TAG, "onLayout" + "--arg0=" + arg0 + "--arg1=" + arg1 + "--arg2=" + arg2 + "--arg3=" + arg3 + "--arg4=" + arg4);
			if(arg0){
				//getIconView().layout(arg1, arg2, arg1 + menuWidth, arg2 + menuWidth);
				getIconView().layout(0, 0, menuWidth, menuWidth);
				
				getNameView().layout(0, menuHeight - getNameView().getMeasuredHeight(),menuWidth, menuHeight);
			}
		}
		
	}
}

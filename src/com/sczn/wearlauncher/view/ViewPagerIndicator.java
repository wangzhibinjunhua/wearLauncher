package com.sczn.wearlauncher.view;

import com.sczn.wearlauncher.R;
import com.sczn.wearlauncher.util.MxyLog;

import android.content.Context;
import android.text.method.HideReturnsTransformationMethod;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;

public class ViewPagerIndicator extends LinearLayout {
	private static final String TAG = ViewPagerIndicator.class.getSimpleName();
	
	public final static int TYPE_NORMAL = 0;
	public final static int TYPE_STYLE_CHOOSE = 1;
	public final static int TYPE_CARD_CONTAIN = 2;
	public final static int TYPE_STATUS_CONTAIN = 3;
	
	public final static int COUNT_MAX = 12;

	private int offsetMax;
	
	public ViewPagerIndicator(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	private int mViewCount;
	private int mViewType;

	public void init(int count, int viewType){
		
		if(count <= 1){
			setVisibility(GONE);
			return;
		}else{
			setVisibility(VISIBLE);
		}
		
		count = count > COUNT_MAX ? COUNT_MAX : count;
		
		mViewCount = count;
		mViewType = viewType;
		offsetMax = 0;
		setPadding(0, 0, 0, 15);
		initView();
	}

	private void initView() {
		// TODO Auto-generated method stub
		removeAllViews();
		LinearLayout.LayoutParams params;
		if(HORIZONTAL == getOrientation()){
			params = new LayoutParams(0, LayoutParams.MATCH_PARENT, 1);
		}else{
			params = new LayoutParams(LayoutParams.MATCH_PARENT, 0, 1);
		}
		for(int i = 0; i < mViewCount; i++){
			addView(getNewView(), params);
		}

	}
	
	private View getNewView(){
		switch (mViewType) {
			case TYPE_STYLE_CHOOSE:
				//final View lineView = new View(getContext());
				//lineView.setBackgroundResource(R.drawable.indicator_line_bg);
				//return lineView;
			case TYPE_CARD_CONTAIN:
			case TYPE_STATUS_CONTAIN:
			case TYPE_NORMAL:
				final ImageView dotView = new ImageView(getContext());
				dotView.setImageResource(R.drawable.indicator_dot);
				dotView.setScaleType(ScaleType.CENTER_INSIDE);
				return dotView;
			default:
				return new View(getContext());
		}
	}
	
	public void setSelect(int index){
		//MxyLog.d(TAG, "setSelect--index=" + index + "--offsetMax=" + offsetMax);
		if(index >= getChildCount() + offsetMax){
			offsetMax = index - getChildCount() + 1;
			index = getChildCount() -1;
		}
		else if(index < offsetMax){
			offsetMax = index;
			index = 0;
		}else {
			index -= offsetMax;
		}
		//MxyLog.d(TAG, "setSelect--acture set=" + index);
		for(int i = 0; i < getChildCount(); i++){
			if(index == i){
				getChildAt(i).setSelected(true);
			}else{
				getChildAt(i).setSelected(false);
			}
		}
	}
	
}

package com.sczn.wearlauncher.view;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.AbsListView;
import android.widget.ListView;

import com.sczn.wearlauncher.R;
import com.sczn.wearlauncher.adapter.LetterIndexAdapter;
import com.sczn.wearlauncher.adapter.LetterIndexAdapter.IIndexSelected;
import com.sczn.wearlauncher.adapter.LetterIndexAdapter.ViewHolder;
import com.sczn.wearlauncher.util.MxyLog;

public class LetterIndexView extends ListView implements  ListView.OnScrollListener,IIndexSelected{
	
	private FreshHandler mFreshHandler;
	private LetterIndexAdapter mAdapter;
	private int mCenterX = 0;
	private int mCenterY = 0;
	private int mScrollerState = SCROLL_STATE_IDLE;
	private int mLastSelectedPosition = INVALID_POSITION;
	
	private CharSequence letterSelected;
	
	public LetterIndexView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.LetterView);
		
		final int showCount = a.getInteger(R.styleable.LetterView_showcount, 5);
		final int letterSize = a.getDimensionPixelSize(R.styleable.LetterView_textsize_normal,
				getResources().getDimensionPixelSize(R.dimen.healthalarm_timer_pick_normal));
		final int letterSizeSelected = a.getDimensionPixelSize(R.styleable.LetterView_textsize_selected,
				getResources().getDimensionPixelSize(R.dimen.healthalarm_timer_pick_selected));
		final int letterColor = a.getColor(R.styleable.LetterView_color_normal,
				getResources().getColor(R.color.healthalarm_timer_picker_normal));
		final int letterColorSelected = a.getColor(R.styleable.LetterView_color_selected,
				getResources().getColor(R.color.healthalarm_timer_picker_selected));
		final CharSequence[] letters = a.getTextArray(R.styleable.LetterView_letters);
		
		final boolean needLoop = a.getBoolean(R.styleable.LetterView_need_loop, false);
		
		a.recycle();
		
		setFriction(ViewConfiguration.getScrollFriction() * 5);
		setWillNotDraw(false);
		
		mAdapter = new LetterIndexAdapter(getContext(), needLoop, showCount);
		mAdapter.initStyle(letterSize, letterSizeSelected, letterColor, letterColorSelected);
		mAdapter.setLetters(letters);
		mAdapter.setIndexSelected(this);
		
		
		setAdapter(mAdapter);
		setOnScrollListener(this);
		setDivider(null);
		setScrollBarSize(0);
		
		mFreshHandler = new FreshHandler(this);
	}
	
	public void setSelectedLetter(CharSequence letter){

		setSelectionFromTop(mAdapter.getLetterPosition(letter), 
				mAdapter.getCenterIndex()*mAdapter.getItenHeight());
	}
	
	public CharSequence getSelectedLetter(){
		return letterSelected;
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		
		final int mode = getMeasuredHeight()%mAdapter.getShowCount();
		if(mode > 1){
			setMeasuredDimension(getMeasuredWidth(), getMeasuredHeight() - mode);
		}
		//MxyLog.d(this, "onMeasure--getMeasuredHeight()22=" + getMeasuredHeight());
		

		final int itemHeight = getMeasuredHeight()/mAdapter.getShowCount();
		mAdapter.setItemHeight(itemHeight);

		mCenterX = (getMeasuredWidth() - getPaddingRight() + getPaddingLeft())/2;
		mCenterY = getMeasuredHeight()/2;
		
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// TODO Auto-generated method stub
		this.mScrollerState = scrollState;
		//MxyLog.d(this, "onScrollStateChanged--" + mScrollerState);
		switch (scrollState) {
			case SCROLL_STATE_IDLE:
				//setSelectionFromTop(getFirstVisiblePosition() + 3, 3*mAdapter.getItenHeight());
				int position = pointToPosition(mCenterX, mCenterY);
				//MxyLog.d(this, "onScroll--position=" + position + "--mAdapter.getSelectedLetter()=" + mAdapter.getSelectedLetter());
				if(INVALID_POSITION != position){
					//mAdapter.setSelectedLetter(position);
					setSelectionFromTop(position, mAdapter.getCenterIndex()*
							mAdapter.getItenHeight());
				}else{
					setSelectionFromTop(mAdapter.getSelectedLetter(), mAdapter.getCenterIndex()*
							mAdapter.getItenHeight());
				}
				break;
	
			default:
				break;
		}
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		// TODO Auto-generated method stub
		//pointToPosition(x, y);
		//MxyLog.d(this, "onScroll--mScrollerState=" + mScrollerState);
		int position = pointToPosition(mCenterX, mCenterY);
		//MxyLog.d(this, "onScroll--position=" + position);
		if(INVALID_POSITION == position){
			return;
		}
		
		if(mAdapter.isLoop()){
			final int size = mAdapter.getLetters().size();
			
			if(firstVisibleItem <= size){
				setSelectionFromTop(firstVisibleItem + size, getChildAt(0).getTop());
			}else if(firstVisibleItem > size*2 - 1){
				setSelectionFromTop(firstVisibleItem%size + size, getChildAt(0).getTop());
			}
		}
		
		if(mLastSelectedPosition != position){
			mLastSelectedPosition = position;
			mAdapter.setSelectedPosition(position);
			mFreshHandler.sendFreshMsg(mLastSelectedPosition);
		}
	}
	private void resizeItem(int selectedPosition){
		//MxyLog.d(this, "resizeItem" + "--selectedPosition=" + selectedPosition + "--mLastSelectedPosition=" + mLastSelectedPosition);
		if(selectedPosition != mLastSelectedPosition){
			return;
		}
		final int firstPosition = getFirstVisiblePosition();
		for(int i = 0; i < getChildCount(); i++){
			final View item = getChildAt(i);
			if(item.getTag() == null || !(item.getTag() instanceof LetterIndexAdapter.ViewHolder)){
				continue;
			}
			final LetterIndexAdapter.ViewHolder holder = (ViewHolder) item.getTag();
			final int position = firstPosition + i;
			holder.mLetter.setState(position == selectedPosition);
			//MxyLog.d(this, "position=" + position + "--firstPosition=" + firstPosition + "--selectedPosition=" + selectedPosition);
		}
	}
	private static class FreshHandler extends Handler{
		private WeakReference<LetterIndexView> mListView;
		private static final int MSG_RESIZE = 0;
		
		public FreshHandler(LetterIndexView listview){
			mListView = new WeakReference<LetterIndexView>(listview);
		}
		
		public void sendFreshMsg(int position){
			removeMessages(MSG_RESIZE);
			Message msg = Message.obtain();
			msg.what = MSG_RESIZE;
			msg.arg1 = position;
			sendMessage(msg);
		}
		
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			final LetterIndexView listview = mListView.get();
			if(listview == null){
				return;
			}
			switch (msg.what) {
				case MSG_RESIZE:
					listview.resizeItem(msg.arg1);
					break;
	
				default:
					break;
			}
		}
		
	}
	@Override
	public void OnIndexSelected(CharSequence letter) {
		// TODO Auto-generated method stub
		letterSelected = letter;
	}

}

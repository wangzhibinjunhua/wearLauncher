package com.sczn.wearlauncher.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

public class MyRecyclerView extends RecyclerView{
	private static final String TAG = MyRecyclerView.class.getSimpleName();
	
	private View mEmptyView;

	public MyRecyclerView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		//initViewSlop();
	}
	
	public void setEmpty(View emptyView){
		this.mEmptyView = emptyView;
		checkEmpty();
	}
	
	@Override
	public void setAdapter(Adapter adapter) {
		// TODO Auto-generated method stub
		final Adapter oldAdapter = getAdapter();
		if (oldAdapter != null) {
			oldAdapter.unregisterAdapterDataObserver(mAdapterDataObserver);
        }
        super.setAdapter(adapter);
        if (adapter != null) {
            adapter.registerAdapterDataObserver(mAdapterDataObserver);
        }
        
        checkEmpty();

	}
	
	private void checkEmpty(){
		if(mEmptyView != null && getAdapter() != null){
			final boolean emptyViewVisible =
                    getAdapter().getItemCount() == 0;
			mEmptyView.setVisibility(emptyViewVisible ? VISIBLE : GONE);
			setVisibility(emptyViewVisible ? GONE : VISIBLE);
		}
	}
	
	@Override
	public void requestLayout() {
		// TODO Auto-generated method stub
		super.requestLayout();
		if(mEmptyView != null){
			if(getAdapter() == null){
				return;
			}
			if(getAdapter().getItemCount() == 0){
				mEmptyView.setVisibility(View.VISIBLE);
			}else{
				mEmptyView.setVisibility(View.GONE);
			}
		}
	}

	private AdapterDataObserver mAdapterDataObserver  = new AdapterDataObserver() {

		@Override
		public void onChanged() {
			// TODO Auto-generated method stub
			super.onChanged();
			checkEmpty();
		}

		@Override
		public void onItemRangeInserted(int positionStart, int itemCount) {
			// TODO Auto-generated method stub
			super.onItemRangeInserted(positionStart, itemCount);
			checkEmpty();
		}

		@Override
		public void onItemRangeRemoved(int positionStart, int itemCount) {
			// TODO Auto-generated method stub
			super.onItemRangeRemoved(positionStart, itemCount);
			checkEmpty();
		}
		
	};
}

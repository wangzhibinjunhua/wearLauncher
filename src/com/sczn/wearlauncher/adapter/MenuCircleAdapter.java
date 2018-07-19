package com.sczn.wearlauncher.adapter;

import java.util.ArrayList;

import com.sczn.wearlauncher.R;
import com.sczn.wearlauncher.model.AppMenu;
import com.sczn.wearlauncher.util.MxyLog;
import com.sczn.wearlauncher.view.menu.MenuCirclePager;
import com.sczn.wearlauncher.view.menu.MenuCirclePager.MenuIconView;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;


public class MenuCircleAdapter extends Adapter<MenuCircleAdapter.MenuCircleHolder> {
	private final static String TAG = MenuCircleAdapter.class.getSimpleName();
	
	public static final int MENU_COUNT_PER_PAGER = 6;
	public static final int ITEM_TYPE_FULL_PAGER = 0;
	public static final int ITEM_TYPE_UNFULL_PAGER = 1;
	
	private Context mContext;
	private ArrayList<AppMenu> mMenuList;
	private OnCircleMenuClickListen mOnCircleMenuClickListen;
	private boolean isStyle = false;
	
	public MenuCircleAdapter(Context mContext,
			OnCircleMenuClickListen mOnCircleMenuClickListen, boolean isStyle) {
		super();
		this.mContext = mContext;
		this.mOnCircleMenuClickListen = mOnCircleMenuClickListen;
		this.isStyle = isStyle;
		mMenuList = new ArrayList<AppMenu>();
	}



	public void setMenuList(ArrayList<AppMenu> menuList){
		mMenuList.clear();
		mMenuList.addAll(menuList);
		notifyDataSetChanged();
	}

	@Override
	public int getItemCount() {
		// TODO Auto-generated method stub
		if(mMenuList.size()%MENU_COUNT_PER_PAGER == 0){
			return mMenuList.size()/MENU_COUNT_PER_PAGER;
		}else{
			return mMenuList.size()/MENU_COUNT_PER_PAGER + 1;
		}
	}


	@Override
	public int getItemViewType(int position) {
		// TODO Auto-generated method stub
		if(position == getItemCount() - 1){
			if(position * MENU_COUNT_PER_PAGER < mMenuList.size()){
				return ITEM_TYPE_UNFULL_PAGER;
			}
		}
		return ITEM_TYPE_FULL_PAGER;
	}

	@Override
	public void onBindViewHolder(MenuCircleHolder holder, int position) {
		// TODO Auto-generated method stub
		int startIndex = position*MENU_COUNT_PER_PAGER;
		
		//MxyLog.i(TAG, "onBindViewHolder" + "--position=" + position + "--holder.getMenus().size()=" + holder.getMenus().size());

		for(int i = 0; i< holder.getMenus().size(); i++){
			if(ITEM_TYPE_UNFULL_PAGER == getItemViewType(position)){
				final int menuCount = mMenuList.size()%MENU_COUNT_PER_PAGER;
				if(menuCount != 0 && i >= menuCount){
					holder.getMenus().get(i).setVisibility(View.INVISIBLE);
					continue;
				}
			}
			final MenuIconView view = holder.getMenus().get(i);
			view.setVisibility(View.VISIBLE);
			view.setImageDrawable(getIcon(startIndex + i));
			if(startIndex + i < mMenuList.size()){
				view.setTag(mMenuList.get(startIndex + i));
			}
			view.setOnClickListener(new OnItemClick());
		}
	}
	
	private Drawable getIcon(int position){
		if(position < mMenuList.size()){
			if(isStyle){
				return mMenuList.get(position).getIconWithoutCache(mContext);
			}else{
				return mMenuList.get(position).getIcon(mContext);
			}
		}
		return mContext.getResources().getDrawable(R.drawable.ic_launcher);
	}

	@Override
	public MenuCircleHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		// TODO Auto-generated method stub
		//MxyLog.i(TAG, "onCreateViewHolder mMenuList.size()=" + mMenuList.size() + "--getItemCount()=" + getItemCount());
		final MenuCirclePager view;
		if(ITEM_TYPE_FULL_PAGER == viewType){
			view = new MenuCirclePager(mContext, parent,  MENU_COUNT_PER_PAGER);
		}else{
			view = new MenuCirclePager(mContext, parent,
					MENU_COUNT_PER_PAGER, mMenuList.size()%MENU_COUNT_PER_PAGER);
		}
		
		return new MenuCircleHolder(view);
	}

	public class MenuCircleHolder extends ViewHolder{
		
		private ArrayList<MenuIconView> mMenus;
		private int mMenuCount;

		public MenuCircleHolder(MenuCirclePager parent) {
			super(parent);
			/*
			mMenus = new ArrayList<MenuIconView>();
			mMenuCount = parent.getChildCount();
			for(int i = 0; i < mMenuCount; i++){
				mMenus.add((MenuIconView) parent.getChildAt(i));
			}*/
			mMenus = parent.getIconList();
		}
		
		public ArrayList<MenuIconView> getMenus(){
			return mMenus;
		}
	}
	
	private class OnItemClick implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if(mOnCircleMenuClickListen != null){
				mOnCircleMenuClickListen.onCircleMenuClick(v);
			}
		}
		
	}

	public interface OnCircleMenuClickListen{
		public void onCircleMenuClick(View view);
	}
}

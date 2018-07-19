package com.sczn.wearlauncher.adapter;

import java.util.ArrayList;

import com.sczn.wearlauncher.R;
import com.sczn.wearlauncher.model.AppMenu;
import com.sczn.wearlauncher.view.ScrollerTextView;
import com.sczn.wearlauncher.view.menu.MenuIconImage;
import com.sczn.wearlauncher.view.menu.MenuTextView;
import com.sczn.wearlauncher.view.menu.MenuVerticalItem;

import android.content.Context;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;

public class MenuVirticalAdapter extends Adapter<MenuVirticalAdapter.MenuVirtalHolder> {
	private final static String TAG = MenuVirticalAdapter.class.getSimpleName();
	
	private final static int ITEM_TPYE_NORMAL = 0;
	private final static int ITEM_TYPE_EMPTY = 1;
	private final static int HEADER_COUNT = 1;
	private final static int FOOTER_COUNT = 1;
	private boolean isApp;
	private ArrayList<AppMenu> mMenuList;
	private IMenuVerticalClickListen menuVerticalClickListen;
	private boolean isStyle = false;
	private boolean needLoop = false;
	private boolean isLoop = false;

	public MenuVirticalAdapter(boolean isApp, boolean needLoop){
		this(false, isApp, needLoop);
	}
	public MenuVirticalAdapter(boolean isStyle, boolean isApp, boolean needLoop){
		this.isStyle = isStyle;
		this.isApp = isApp;
		this.needLoop = needLoop;
		mMenuList = new ArrayList<AppMenu>();
	}
	
	public void setMenuClickListem(IMenuVerticalClickListen listen){
		this.menuVerticalClickListen = listen;
	}
	  
	public boolean isNeedLoop() {
		return needLoop;
	}

	public boolean isLoop() {
		return isLoop;
	}
	
	public boolean isApp(){
		return isApp;
	}
	
	public void setMenus(ArrayList<AppMenu> menuList){
		mMenuList.clear();
		mMenuList.addAll(menuList);
		if(needLoop && mMenuList.size() > 3){
			
			isLoop = true;
		}else{
			isLoop = false;
		}
		notifyDataSetChanged();
	}
	
	private AppMenu getItem(int position){
		if(isLoop){
			return mMenuList.get(position%mMenuList.size());
		}
		return mMenuList.get(position);
	}
	
	public ArrayList<AppMenu> getMenus(){
		return mMenuList;
	}

	@Override
	public int getItemCount() {
		// TODO Auto-generated method stub
		if(isLoop){
			return mMenuList.size()*3;
		}
		return mMenuList.size() + HEADER_COUNT + FOOTER_COUNT;
	}
	
	@Override
	public int getItemViewType(int position) {
		// TODO Auto-generated method stub
		if(isLoop){
			return ITEM_TPYE_NORMAL;
		}
		
		if(position == 0 || position == getItemCount() - 1){
			return ITEM_TYPE_EMPTY;
		}
		return ITEM_TPYE_NORMAL;
	}

	@Override
	public void onBindViewHolder(MenuVirtalHolder arg0, int arg1) {
		// TODO Auto-generated method stub

		if(ITEM_TYPE_EMPTY == getItemViewType(arg1)){
			return;
		}
		if(!isLoop){
			if(arg1 < HEADER_COUNT || arg1 > (HEADER_COUNT + mMenuList.size() - 1)){
				return;
			}
			arg1 -= HEADER_COUNT;
		}
		
		final AppMenu menu = getItem(arg1%mMenuList.size());
		arg0.mName.setText(menu.getName(arg0.itemView.getContext()));
		if(isStyle){
			arg0.mIcon.setImageDrawable(menu.getIconWithoutCache(arg0.itemView.getContext()));
		}else{
			arg0.mIcon.setImageDrawable(menu.getIcon(arg0.itemView.getContext()));
		}
		arg0.mIcon.setTag(menu);
		arg0.mIcon.setOnClickListener(new OnItemClick());
		if(!isApp){
			arg0.itemView.setTag(menu);
		}
	}
	
	@Override
	public MenuVirtalHolder onCreateViewHolder(ViewGroup arg0, int arg1) {
		// TODO Auto-generated method stub
		final MenuVerticalItem view;
		if(isApp){
			view = (MenuVerticalItem) LayoutInflater.from(arg0.getContext()).inflate(R.layout.list_item_applist,arg0, false);
		}else{
			//view = (MenuVerticalItem) LayoutInflater.from(arg0.getContext()).inflate(R.layout.list_item_sportlist,arg0, false);
			view = (MenuVerticalItem) LayoutInflater.from(arg0.getContext()).inflate(R.layout.list_item_applist,arg0, false);
		}
		
		if(isStyle){
			view.setPadding(0, 0, 0, 0);
		}
		
		if(ITEM_TYPE_EMPTY == arg1){
			view.setVisibility(View.INVISIBLE);
		}
		view.setParentHeight(arg0.getHeight());
		if(!isApp){
			view.setParentView(arg0);
		}
		final int iconSize = view.getLayoutParams().height = arg0.getHeight()/3;
		return new MenuVirtalHolder(view, iconSize);
	}
	
	public class MenuVirtalHolder extends ViewHolder{

		private MenuIconImage mIcon;
		private ScrollerTextView mName;
		private int mIconSize;

		public MenuVirtalHolder(MenuVerticalItem view, int iconSize) {
			// TODO Auto-generated constructor stub
			super(view);
			mIcon = (MenuIconImage) view.findViewById(R.id.applist_icon);
			mName = (ScrollerTextView) view.findViewById(R.id.applist_name);
			this.mIconSize = iconSize;
			
			mIcon.getLayoutParams().width = mIconSize;
			mIcon.getLayoutParams().height = mIconSize;
		}
	}

	private class OnItemClick implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if(menuVerticalClickListen != null){
				menuVerticalClickListen.onVerticalMenuClick(v);
			}
		}
		
	}

	public interface IMenuVerticalClickListen{
		public void onVerticalMenuClick(View view);
	}
}

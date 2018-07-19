package com.sczn.wearlauncher.view.menu;

import java.util.ArrayList;
import java.util.HashMap;

import com.sczn.wearlauncher.model.AppMenu;
import com.sczn.wearlauncher.util.MxyLog;
import com.sczn.wearlauncher.util.SportListUtil;
import com.sczn.wearlauncher.util.SysServices;

import android.content.Context;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.provider.Settings;
import android.util.AttributeSet;
import android.widget.Adapter;
import android.widget.FrameLayout;
import android.widget.ImageView;

public class MenuVerticalBg extends FrameLayout{
	private static final String TAG = MenuVerticalBg.class.getSimpleName();
	
	private int menuSportBgType = SportListUtil.MENU_SPORT_BG;
	private MenuBgObserve mMenuBgObserve;
	private HashMap<AppMenu, BgImage> mSportBgs = new HashMap<AppMenu, BgImage>();
	
	public MenuVerticalBg(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		mMenuBgObserve = new MenuBgObserve(new Handler());
	}
	
	@Override
	protected void onAttachedToWindow() {
		// TODO Auto-generated method stub
		super.onAttachedToWindow();
		mMenuBgObserve.register(getContext());
		menuSportBgType = SysServices.getSystemSettingInt(getContext(),
				SportListUtil.SETTING_KEY_MENU_SPORT_BG, menuSportBgType);
	}
	
	@Override
	protected void onDetachedFromWindow() {
		// TODO Auto-generated method stub
		mMenuBgObserve.unRegister(getContext());
		super.onDetachedFromWindow();
	}

	public void setSportMenus(ArrayList<AppMenu> menus){
		final LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		mSportBgs.clear();
		//MxyLog.d(TAG, "setSportMenus" + menus.size());
		for(AppMenu menu : menus){
			final BgImage image = new BgImage(getContext(), menu);
			mSportBgs.put(menu, image);
			addView(image, params);
		}
	}
	
	public void freshBg(AppMenu menu, float alpha){
		if(menu == null){
			return;
		}
		if(alpha <= 0){
			alpha = 0;
		}
		final BgImage image = mSportBgs.get(menu);
		if(image != null){
			image.setAlpha(alpha);
		}
		
	}
	
	private class BgImage extends ImageView{
		private AppMenu menu;
		
		public BgImage(Context context, AppMenu menu) {
			super(context);
			this.menu = menu;
			setVisibility(GONE);
		}


		@Override
		public void setAlpha(float alpha) {
			// TODO Auto-generated method stub
			if(alpha < 0.1){
				setVisibility(GONE);
				return;
			}else{
				setVisibility(VISIBLE);
				setImageResource(SportListUtil.MENU_SPORT_BG == menuSportBgType?
						menu.getBgImageRes():menu.getBgImageBwRes());
			}
			super.setAlpha(alpha);
		}
		
	}
	
	private class MenuBgObserve extends ContentObserver{

		private Uri menuSportenuBgUri;
		public MenuBgObserve(Handler handler) {
			super(handler);
			// TODO Auto-generated constructor stub
		}
		
		public void register(Context context){
			menuSportenuBgUri = getMenuSportBgUri(context);
			if(menuSportenuBgUri != null){
				context.getContentResolver().registerContentObserver(menuSportenuBgUri, true,this);
			}
		}
		
		public void unRegister(Context context){
			context.getContentResolver().unregisterContentObserver(this);
		}
		private Uri getMenuSportBgUri(Context context){
			final Uri uri = Settings.System.getUriFor(SportListUtil.SETTING_KEY_MENU_SPORT_BG);
			if(uri == null){
				SysServices.setSystemSettingInt(context, SportListUtil.SETTING_KEY_MENU_SPORT_BG, 
						SportListUtil.MENU_SPORT_BG);
			}
			return Settings.System.getUriFor(SportListUtil.SETTING_KEY_MENU_SPORT_BG);
		}
		@Override
		public void onChange(boolean selfChange, Uri uri) {
			// TODO Auto-generated method stub
			
			if(menuSportenuBgUri.equals(uri)){
				menuSportBgType = SysServices.getSystemSettingInt(getContext(),
						SportListUtil.SETTING_KEY_MENU_SPORT_BG, menuSportBgType);
				final int childCount = getChildCount();
				for(int i=0; i< childCount; i++){
					
				}
				return;
			}
		}
		
		
	}
}

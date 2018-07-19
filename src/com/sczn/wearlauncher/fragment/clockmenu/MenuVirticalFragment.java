package com.sczn.wearlauncher.fragment.clockmenu;

import java.util.ArrayList;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import com.sczn.wearlauncher.R;
import com.sczn.wearlauncher.adapter.MenuVirticalAdapter;
import com.sczn.wearlauncher.adapter.MenuVirticalAdapter.IMenuVerticalClickListen;
import com.sczn.wearlauncher.model.AppMenu;
import com.sczn.wearlauncher.util.AppListUtil;
import com.sczn.wearlauncher.util.MxyLog;
import com.sczn.wearlauncher.util.MxyToast;
import com.sczn.wearlauncher.util.SportListUtil;
import com.sczn.wearlauncher.util.SysServices;
import com.sczn.wearlauncher.view.menu.MenuVerticalBg;
import com.sczn.wearlauncher.view.menu.MenuVirticalRecyleView;

public class MenuVirticalFragment extends absMenuFragment implements IMenuVerticalClickListen{
	private final static String TAG = MenuVirticalFragment.class.getSimpleName();
	
	public static MenuVirticalFragment newInstance(boolean isApp, boolean isStyle){
		MenuVirticalFragment fragment = new MenuVirticalFragment();
		Bundle bdl = new Bundle();
		bdl.putBoolean(ARG_IS_APP, isApp);
		bdl.putBoolean(ARG_IS_STYLE, isStyle);
		fragment.setArguments(bdl);
		return fragment;
	}

	private MenuVirticalRecyleView menuVirticalRecyleView;
	private MenuVirticalAdapter mVirticalAdapter;
	private MenuVerticalBg mMenuSportBg;
	
	@Override
	protected int getLayoutResouceId() {
		// TODO Auto-generated method stub
		return R.layout.fragment_clockmenu_menu_virtical;
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		menuVirticalRecyleView = findViewById(R.id.menu_virtical_recyleview);
		mMenuSportBg = findViewById(R.id.menu_virtical_recyleview_bg);

		mVirticalAdapter = new MenuVirticalAdapter(isStyle, isApp, false);
		if(!isStyle){
			mVirticalAdapter.setMenuClickListem(this);
		}
		menuVirticalRecyleView.initLayoutManager(LinearLayoutManager.VERTICAL, false);
		menuVirticalRecyleView.setAdapter(mVirticalAdapter);
	}
	
	

	@Override
	protected void initData() {
		// TODO Auto-generated method stub
		super.initData();
		if(!isApp){
			mVirticalAdapter.setMenus(SportListUtil.getSportList());
			mMenuSportBg.setVisibility(View.VISIBLE);
			mMenuSportBg.setSportMenus(SportListUtil.getSportList());
			menuVirticalRecyleView.setBg(mMenuSportBg);
		}else{
			mVirticalAdapter.setMenus(mAppListUtil.getAppList());
		}
	}

	@Override
	protected void freshData() {
		// TODO Auto-generated method stub
		MxyLog.d(TAG, "freshData" + "isApp=" + isApp + "--mAppListUtil.getAppList()=" + mAppListUtil.getAppList().size());
		if(isApp){
			mVirticalAdapter.setMenus(mAppListUtil.getAppList());
		}else{
			mVirticalAdapter.setMenus(SportListUtil.getSportList());
		}
	}

	@Override
	public void onVerticalMenuClick(View view) {
		doMenuClick(view);
	}

}

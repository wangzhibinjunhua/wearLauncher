package com.sczn.wearlauncher.fragment.clockmenu;

import java.util.ArrayList;
import java.util.Observable;

import com.sczn.wearlauncher.R;
import com.sczn.wearlauncher.adapter.MenuSquareAdapter;
import com.sczn.wearlauncher.adapter.MenuSquareAdapter.OnSquareMenuClickListen;
import com.sczn.wearlauncher.model.AppMenu;
import com.sczn.wearlauncher.util.AppListUtil;
import com.sczn.wearlauncher.util.MxyLog;
import com.sczn.wearlauncher.util.MxyToast;
import com.sczn.wearlauncher.util.SportListUtil;
import com.sczn.wearlauncher.util.SysServices;
import com.sczn.wearlauncher.view.PagerRecylerView;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class MenuSquareFragment extends absMenuFragment implements OnSquareMenuClickListen{
	
private final static String TAG = MenuSquareFragment.class.getSimpleName();
	
	public static MenuSquareFragment newInstance(boolean isApp,boolean isStyle){
		MenuSquareFragment fragment = new MenuSquareFragment();
		Bundle bdl = new Bundle();
		bdl.putBoolean(ARG_IS_APP, isApp);
		bdl.putBoolean(ARG_IS_STYLE, isStyle);
		fragment.setArguments(bdl);
		return fragment;
	}

	private PagerRecylerView mMenuRecyclerView;
	private MenuSquareAdapter menuSquareAdapter;

	@Override
	protected int getLayoutResouceId() {
		// TODO Auto-generated method stub
		return R.layout.fragment_clockmenu_menu_square;
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		mMenuRecyclerView = findViewById(R.id.menu_square);
		mMenuRecyclerView.initLayoutManager(LinearLayoutManager.VERTICAL, false);
		mMenuRecyclerView.setFlingVelocity(PagerRecylerView.FLING_DIEABLE);
		menuSquareAdapter = new MenuSquareAdapter(getActivity(),this,isStyle);
		mMenuRecyclerView.setAdapter(menuSquareAdapter);

	}
	
	@Override
	protected void initData() {
		// TODO Auto-generated method stub
		super.initData();
		if(!isApp){
			menuSquareAdapter.setMenuList(SportListUtil.getSportList());
		}else{
			menuSquareAdapter.setMenuList(mAppListUtil.getAppList());
		}
	}

	@Override
	protected void freshData() {
		// TODO Auto-generated method stub
		if(isApp){
			menuSquareAdapter.setMenuList(mAppListUtil.getAppList());
		}
	}
	
	

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}
	
	

	@Override
	public void update(Observable observable, Object data) {
		// TODO Auto-generated method stub
		super.update(observable, data);
	}

	@Override
	public void onSquareMenuClick(View view) {
		doMenuClick(view);
	}
}

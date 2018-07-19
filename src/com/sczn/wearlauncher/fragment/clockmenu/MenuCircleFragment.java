package com.sczn.wearlauncher.fragment.clockmenu;

import java.util.ArrayList;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.LauncherActivity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;

import com.sczn.wearlauncher.LauncherApp;
import com.sczn.wearlauncher.R;
import com.sczn.wearlauncher.adapter.MenuCircleAdapter;
import com.sczn.wearlauncher.adapter.MenuCircleAdapter.OnCircleMenuClickListen;
import com.sczn.wearlauncher.model.AppMenu;
import com.sczn.wearlauncher.util.AppListUtil;
import com.sczn.wearlauncher.util.MxyLog;
import com.sczn.wearlauncher.util.MxyToast;
import com.sczn.wearlauncher.util.SportListUtil;
import com.sczn.wearlauncher.util.SysServices;
import com.sczn.wearlauncher.view.PagerRecylerView;
import com.sczn.wearlauncher.view.menu.MenuClockView;

public class MenuCircleFragment extends absMenuFragment implements OnCircleMenuClickListen{
	private static final String TAG = MenuCircleFragment.class.getSimpleName();

	public static MenuCircleFragment newInstance(boolean isApp, boolean isStyle){
		MenuCircleFragment fragment = new MenuCircleFragment();
		Bundle bdl = new Bundle();
		bdl.putBoolean(ARG_IS_APP, isApp);
		bdl.putBoolean(ARG_IS_STYLE, isStyle);
		fragment.setArguments(bdl);
		return fragment;
	}
	public static final String FRAGMENT_TAG_MENU_CLOCK = "fragment_menu_clock"; 
	
	private PagerRecylerView mMenuCirclerView;
	private MenuCircleAdapter menuCircleAdapter;
	private MenuClockView menuClockView;
	
	@Override
	protected int getLayoutResouceId() {
		// TODO Auto-generated method stub
		return R.layout.fragment_clockmenu_menu_circle;
		
	}
	
	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		//menuClockView = findViewById(R.id.menu_circle_clock);
		mMenuCirclerView = findViewById(R.id.menu_circle);
		mMenuCirclerView.initLayoutManager(LinearLayoutManager.VERTICAL, false);
		mMenuCirclerView.setFlingVelocity(PagerRecylerView.FLING_DIEABLE);
		menuCircleAdapter = new MenuCircleAdapter(getActivity(), this, isStyle);
		//mMenuCirclerView.setScrollingTouchSlop(RecyclerView.TOUCH_SLOP_PAGING);
		
		mMenuCirclerView.setAdapter(menuCircleAdapter);
	}

	@Override
	protected void initData() {
		// TODO Auto-generated method stub
		super.initData();
		if(!isApp){
			menuCircleAdapter.setMenuList(SportListUtil.getSportList());
		}else{
			menuCircleAdapter.setMenuList(mAppListUtil.getAppList());
		}
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);
	}

	@Override
	protected void freshData() {
		// TODO Auto-generated method stub
		if(isApp){
			menuCircleAdapter.setMenuList(mAppListUtil.getAppList());
		}
		
	}
	
	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		// TODO Auto-generated method stub
		super.setUserVisibleHint(isVisibleToUser);
		if(getActivity() == null || getChildFragmentManager() == null){
			return;
		}
		final Fragment frament = getChildFragmentManager().findFragmentByTag(FRAGMENT_TAG_MENU_CLOCK);
		if(frament != null){
			frament.setUserVisibleHint(isVisibleToUser);
		}
	}
	
	@Override
	public void onCircleMenuClick(View view) {
		doMenuClick(view);
	}
	
	@Override
	protected void startFreshData() {
		// TODO Auto-generated method stub
		MxyLog.d(this, "startFreshData()");
		super.startFreshData();
		if(menuClockView != null){
			//menuClockView.setShouldFresh(true);
		}
	}
	
	@Override
	protected void endFreshData() {
		// TODO Auto-generated method stub
		MxyLog.d(this, "endFreshData()");
		if(menuClockView != null){
			//menuClockView.setShouldFresh(false);
		}
		super.endFreshData();
	}

}

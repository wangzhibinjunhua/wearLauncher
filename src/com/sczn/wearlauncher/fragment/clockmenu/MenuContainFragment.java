package com.sczn.wearlauncher.fragment.clockmenu;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.widget.HeterogeneousExpandableList;

import com.sczn.wearlauncher.R;
import com.sczn.wearlauncher.fragment.absFragment;
import com.sczn.wearlauncher.util.AppListUtil;
import com.sczn.wearlauncher.util.MxyLog;

public class MenuContainFragment extends absMenuFragment {
	
	public static MenuContainFragment newInstance(boolean isApp){
		MenuContainFragment fragment = new MenuContainFragment();
		Bundle bdl = new Bundle();
		bdl.putBoolean(ARG_IS_APP, isApp);
		fragment.setArguments(bdl);
		return fragment;
	}
	private final static String TAG = MenuContainFragment.class.getSimpleName();
	
	
	private static final String FRAGMENT_TAG_MENU_VIRTICAL = "menu_virtical";
	private static final String FRAGMENT_TAG_MENU_CIRCLE = "menu_circle";
	private static final String FRAGMENT_TAG_MENU_SQUARE = "menu_square";

	private int mFragmentContainId;
	private absMenuFragment menuFragment;
	//private int mMenuStyle = Integer.MIN_VALUE;
	@Override
	protected int getLayoutResouceId() {
		// TODO Auto-generated method stub
		return R.layout.fragment_clockmenu_menu_contain;
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		mFragmentContainId = R.id.menu_style;
	}
	
	@Override
	protected void startFreshData() {
		// TODO Auto-generated method stub
		MxyLog.d(this, "startFreshData--needFresh" + needFresh);
		super.startFreshData();
		if(menuFragment != null){
			menuFragment.onParentVisibleChange();
		}
	}
	
	@Override
	protected void endFreshData() {
		// TODO Auto-generated method stub
		if(menuFragment != null){
			menuFragment.onParentVisibleChange();
		}
		super.endFreshData();
	}

	@Override
	protected void freshData() {
		// TODO Auto-generated method stub
		
		final int style;
		if(isApp){
			style = AppListUtil.getMenuStyle(getActivity());
		}else{
			style = AppListUtil.MENU_STYLE_VERTICAL;
		}
		/*
		final FragmentManager fm = getChildFragmentManager();
		
		final int currStyle;
		if(fm.findFragmentByTag(FRAGMENT_TAG_MENU_CIRCLE) != null){
			currStyle = AppListUtil.MENU_STYLE_CIRCLE;
		}else if(fm.findFragmentByTag(FRAGMENT_TAG_MENU_SQUARE) != null){
			currStyle = AppListUtil.MENU_STYLE_SQUARE;
		}else if(fm.findFragmentByTag(FRAGMENT_TAG_MENU_VIRTICAL) != null){
			currStyle = AppListUtil.MENU_STYLE_VERTICAL;
		}else{
			currStyle = Integer.MIN_VALUE;
		}
		
		MxyLog.d(this, "freshData()--style=" + style + "--currStyle=" + currStyle);
		if(currStyle != style){
			fragmentReplace(style);
		}*/
		fragmentChange(style);
	}
	
	private void fragmentChange(int style){
		final FragmentManager fm = getChildFragmentManager();
		final FragmentTransaction ft = getChildFragmentManager().beginTransaction();
		
		final Fragment fragment = getChildFragmentManager().findFragmentByTag(getTagByStyle(style));
		if(fragment != null && (fragment instanceof absMenuFragment)){
			MxyLog.d(this, "freshData()--style=" + style + "--fragment=" + fragment.toString());
			menuFragment = (absMenuFragment) fragment;
			ft.show(menuFragment);
		}else{
			menuFragment = getFragment(getTagByStyle(style));
			ft.replace(mFragmentContainId, menuFragment,getTagByStyle(style));
		}
		
		if(menuFragment != null){
			menuFragment.setParentFragment(this);
			menuFragment.setUserVisibleHint(true);
		}
		ft.commitAllowingStateLoss();
	}
	
	private String getTagByStyle(int style){
		switch (style) {
			case AppListUtil.MENU_STYLE_CIRCLE:
				return FRAGMENT_TAG_MENU_CIRCLE;
			case AppListUtil.MENU_STYLE_SQUARE:
				return FRAGMENT_TAG_MENU_SQUARE;
			case AppListUtil.MENU_STYLE_VERTICAL:
			default:
				return FRAGMENT_TAG_MENU_VIRTICAL;
		}
	}
	
	private void fragmentReplace(int style){

		FragmentTransaction ft = getChildFragmentManager().beginTransaction();
		switch (style) {
			case AppListUtil.MENU_STYLE_CIRCLE:
				if(!fragmentAlreadyAdd(FRAGMENT_TAG_MENU_CIRCLE)){
					menuFragment = getFragment(FRAGMENT_TAG_MENU_CIRCLE);
					ft.replace(mFragmentContainId, menuFragment,FRAGMENT_TAG_MENU_CIRCLE);
				}
				break;
			case AppListUtil.MENU_STYLE_SQUARE:
				if(!fragmentAlreadyAdd(FRAGMENT_TAG_MENU_SQUARE)){
					menuFragment = getFragment(FRAGMENT_TAG_MENU_SQUARE);
					ft.replace(mFragmentContainId, menuFragment,FRAGMENT_TAG_MENU_SQUARE);
				}
				break;
			case AppListUtil.MENU_STYLE_VERTICAL:
			default:
				MxyLog.d(TAG, "fragmentReplace--fragmentAlreadyAdd(FRAGMENT_TAG_MENU_VIRTICAL)=" + fragmentAlreadyAdd(FRAGMENT_TAG_MENU_VIRTICAL));
				if(!fragmentAlreadyAdd(FRAGMENT_TAG_MENU_VIRTICAL)){
					menuFragment = getFragment(FRAGMENT_TAG_MENU_VIRTICAL);
					ft.replace(mFragmentContainId, menuFragment,FRAGMENT_TAG_MENU_VIRTICAL);
				}
				break;
		}
		if(menuFragment != null){
			menuFragment.setParentFragment(this);
			menuFragment.setUserVisibleHint(true);
		}
		ft.commitAllowingStateLoss();
	}
	
	private boolean fragmentAlreadyAdd(String tag){
		final Fragment fragment = getChildFragmentManager().findFragmentByTag(tag);
		if(fragment != null){
			
			return true;
		}
		return false;
	}
	private absMenuFragment getFragment(String tag){

		if(FRAGMENT_TAG_MENU_CIRCLE.equals(tag)){
			return MenuCircleFragment.newInstance(isApp, false);
		}
		if(FRAGMENT_TAG_MENU_SQUARE.equals(tag)){
			return MenuSquareFragment.newInstance(isApp, false);
		}
		return MenuVirticalFragment.newInstance(isApp, false);
	}
	
	@Override
	protected void menuChanged(int type) {
		// TODO Auto-generated method stub
		if(AppListUtil.OBSERVER_DATA_STYLE == type){
			freshData();
		}
	}

}

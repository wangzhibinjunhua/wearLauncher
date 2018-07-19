package com.sczn.wearlauncher.fragment;

import java.util.ArrayList;

import com.sczn.wearlauncher.adapter.LoopViewPageAdapter;
import com.sczn.wearlauncher.util.MxyLog;
import com.sczn.wearlauncher.view.HorizalViewPager;
import com.sczn.wearlauncher.view.ViewPagerIndicator;

import android.app.Fragment;
import android.os.Bundle;

public abstract class absViewPagerFragment extends absFragment {
	private static final String TAG = absViewPagerFragment.class.getSimpleName();
	
	public static final String ARG_VIEWPAGER_CURR_INDDEX = "arg_curr_index";
	public static final String ARG_VIEWPAGER_IS_LOOP = "arg_is_loop";
	
	protected HorizalViewPager mHorizalViewPager;
	protected LoopViewPageAdapter mLoopViewPageAdapter;
	protected ViewPagerIndicator mViewPagerIndicator;
	protected ArrayList<absFragment> mFragmentList = new ArrayList<absFragment>(); 
	protected int viewPagerCurrIndex;
	protected boolean isLoop;
	protected boolean showIndicator;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		if(savedInstanceState != null){
			viewPagerCurrIndex = savedInstanceState.getInt(ARG_VIEWPAGER_CURR_INDDEX,
					getDefaultPagerCurrIndex());
		}else{
			viewPagerCurrIndex = getDefaultPagerCurrIndex();
		}
		
		Bundle bdl = getArguments();
		if(bdl != null){
			isLoop = bdl.getBoolean(ARG_VIEWPAGER_IS_LOOP, false);
		}
	}
	
	@Override
	protected void initView(){
		mHorizalViewPager = findViewById(getViewPagerId());
	}

	@Override
	protected void initData(){
		initFragmentList();
		for(absFragment fragment : mFragmentList){
			fragment.setParentFragment(this);
		}
		mLoopViewPageAdapter = new LoopViewPageAdapter(this.getChildFragmentManager(),isLoop,mHorizalViewPager);
		mHorizalViewPager.setAdapter(mLoopViewPageAdapter);
		mLoopViewPageAdapter.setList(mFragmentList);
		mHorizalViewPager.setCurrentItem(getDefaultPagerCurrIndex());
	}
	
	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		// TODO Auto-generated method stub
		super.setUserVisibleHint(isVisibleToUser);
		if(mFragmentList == null){
			return;
		}
		for(absFragment fragment : mFragmentList){
			synchronized (fragment) {
				if(fragment != null && fragment.isVisible()){
					fragment.onParentVisibleChange();
				}
			}
		}
	}
	@Override
	public void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		outState.putInt(ARG_VIEWPAGER_CURR_INDDEX, getPagerCurrIndex());
		super.onSaveInstanceState(outState);
	}
	
	@SuppressWarnings("unchecked")
	protected <T extends Fragment> T findFragmentByIndex(int index){
		if(mHorizalViewPager != null){
			final Fragment fragment = getChildFragmentManager().findFragmentByTag(
					LoopViewPageAdapter.makeFragmentName(mHorizalViewPager.getId(), index));
			try {
				return (T) fragment;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				MxyLog.e(TAG, "findFragmentByIndex--e=" + e.toString());
				return null;
			}
		}
		return null;
	}

	@Override
	protected abstract int getLayoutResouceId();
	
	protected abstract int getPagerCurrIndex();
	protected abstract int getDefaultPagerCurrIndex();
	protected abstract int getViewPagerId();
	protected abstract void initFragmentList();
	
}

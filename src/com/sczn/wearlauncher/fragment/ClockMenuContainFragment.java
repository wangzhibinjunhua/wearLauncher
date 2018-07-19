package com.sczn.wearlauncher.fragment;

import android.os.Bundle;
import android.view.View;

import com.sczn.wearlauncher.R;
import com.sczn.wearlauncher.fragment.clockmenu.Clockfragment;
import com.sczn.wearlauncher.fragment.clockmenu.MenuContainFragment;
import com.sczn.wearlauncher.fragment.status.FirstStatusFragment;
import com.sczn.wearlauncher.util.MxyLog;

public class ClockMenuContainFragment extends absViewPagerFragment {
	
	public static ClockMenuContainFragment newInstance(boolean isLoop){
		ClockMenuContainFragment fragment = new ClockMenuContainFragment();
		Bundle bdl = new Bundle();
		bdl.putBoolean(ARG_VIEWPAGER_IS_LOOP, isLoop);
		fragment.setArguments(bdl);
		return fragment;
	}
	
	private final static String TAG = "ClockMenuContainFragment";
	
	public static final int ITEM_INDEX_SPORT = 0;
	public static final int ITEM_INDEX_CLOCK = ITEM_INDEX_SPORT + 1;
	public static final int ITEM_INDEX_APP = ITEM_INDEX_CLOCK + 1;
	
	private MenuContainFragment mAppListFragment;
	private Clockfragment mClockfragment;
	private MenuContainFragment mSportFragment;
	private NotificationMainFragment mNotificationFragment;

	public ClockMenuContainFragment() {
		super();
		//MxyLog.d(TAG, "ClockMenuContainFragment new=" + toString());
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
	}

	@Override
	protected int getLayoutResouceId() {
		// TODO Auto-generated method stub
		return R.layout.fragment_clockmenu_contain;
	}
	
	@Override
	protected int getViewPagerId() {
		// TODO Auto-generated method stub
		return R.id.viewpager;
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);
		mHorizalViewPager.setOffscreenPageLimit(2);
	}

	@Override
	protected void initFragmentList() {
		// TODO Auto-generated method stub
		mFragmentList.clear();
		
		//mFragmentList.add(ITEM_INDEX_SPORT, getmSportFragment());
		mFragmentList.add(ITEM_INDEX_SPORT, getmNotificationFragment());
		mFragmentList.add(ITEM_INDEX_CLOCK, getmClockfragment());
		mFragmentList.add(ITEM_INDEX_APP, getmAppListFragment());
	}

	@Override
	protected void startFreshData() {
		// TODO Auto-generated method stub
		super.startFreshData();
	}
	
	public MenuContainFragment getmAppListFragment() {
		
		mAppListFragment = (MenuContainFragment) findFragmentByIndex(ITEM_INDEX_APP);
		if(mAppListFragment == null){
			mAppListFragment = MenuContainFragment.newInstance(true);
		}
		return mAppListFragment;
	}

	public Clockfragment getmClockfragment() {
		mClockfragment = (Clockfragment) findFragmentByIndex(ITEM_INDEX_CLOCK);
		if(mClockfragment == null){
			mClockfragment = Clockfragment.newInstance(getActivity().getResources().getDisplayMetrics().widthPixels,true);
		}
		return mClockfragment;
	}

	public MenuContainFragment getmSportFragment() {
		mSportFragment = (MenuContainFragment) findFragmentByIndex(ITEM_INDEX_SPORT);
		if(mSportFragment == null){
			mSportFragment = MenuContainFragment.newInstance(false);
		}
		return mSportFragment;
	}
	public NotificationMainFragment getmNotificationFragment() {
		mNotificationFragment = (NotificationMainFragment) findFragmentByIndex(ITEM_INDEX_SPORT);
		if(mNotificationFragment == null){
			mNotificationFragment = NotificationMainFragment.newInstance(false);
		}
		return mNotificationFragment;
	}
	
	public boolean canScrollerVirtical(){
		if(mHorizalViewPager == null){

			MxyLog.e(TAG, "canScrollerVirtical mApplistViewPager == null");
			return false;
		}
		return ITEM_INDEX_CLOCK == mHorizalViewPager.getCurrentItem();
	}
	
	public void setToClock(boolean smoothScroll){
		if(ITEM_INDEX_CLOCK != mHorizalViewPager.getCurrentItem()){
			mHorizalViewPager.setCurrentItem(ITEM_INDEX_CLOCK, smoothScroll);
		}
	}

	@Override
	protected int getPagerCurrIndex() {
		// TODO Auto-generated method stub
		if(mHorizalViewPager != null){
			return mHorizalViewPager.getCurrentItem();
		}
		return getDefaultPagerCurrIndex();
	}

	@Override
	protected int getDefaultPagerCurrIndex() {
		// TODO Auto-generated method stub
		return ITEM_INDEX_CLOCK;
	}
}

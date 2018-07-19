package com.sczn.wearlauncher.fragment;


import android.os.Bundle;
import com.sczn.wearlauncher.R;
import com.sczn.wearlauncher.fragment.card.AltitudeFragment;
import com.sczn.wearlauncher.fragment.card.CompassFragment;
import com.sczn.wearlauncher.fragment.card.GeographicFragment;
import com.sczn.wearlauncher.fragment.card.HealthAlamFragment;
import com.sczn.wearlauncher.fragment.card.HealthFragment;
import com.sczn.wearlauncher.fragment.card.PressureFragment;
import com.sczn.wearlauncher.fragment.card.SleepFragment;
import com.sczn.wearlauncher.fragment.card.StepFragment;
import com.sczn.wearlauncher.fragment.card.WeatherFragment;
import com.sczn.wearlauncher.view.ViewPagerIndicator;
import com.sczn.wearlauncher.view.HorizalViewPager.IHorizalViewPagerSelected;

public class CardContainFragment extends absViewPagerFragment implements IHorizalViewPagerSelected{
	private final static String TAG = "CardContainFragment";
	
	public static CardContainFragment newInstance(boolean isLoop){
		CardContainFragment fragment = new CardContainFragment();
		Bundle bdl = new Bundle();
		bdl.putBoolean(ARG_VIEWPAGER_IS_LOOP, isLoop);
		fragment.setArguments(bdl);
		return fragment;
	}
	
	public static final int ITEM_INDEX_START = 0;
	private static int AUTO_ADD = 0;
	public static final int ITEM_INDEX_LAST_TMP = ITEM_INDEX_START + AUTO_ADD++;
	
	//public static final int ITEM_INDEX_HEALTH = ITEM_INDEX_START + AUTO_ADD++;
	public static final int ITEM_INDEX_SLEEP = ITEM_INDEX_START + AUTO_ADD++;
	public static final int ITEM_INDEX_HEALTHALARM = ITEM_INDEX_START + AUTO_ADD++; 
	public static final int ITEM_INDEX_STEP = ITEM_INDEX_START + AUTO_ADD++;
	//public static final int ITEM_INDEX_WEATHER = ITEM_INDEX_START + AUTO_ADD++;
	//public static final int ITEM_INDEX_GEOGRAPHIC = ITEM_INDEX_START + AUTO_ADD++;
	//public static final int ITEM_INDEX_COMPASS = ITEM_INDEX_START + AUTO_ADD++;
	
	public static final int ITEM_INDEX_FIRST_TMP = ITEM_INDEX_START + AUTO_ADD++;
	public static final int ITEM_COUNT = ITEM_INDEX_START + AUTO_ADD;
	
	private absFragment mLastFragmentTmp;
	private HealthFragment mHealthFragment;
	private SleepFragment mSleepFragment;
	private HealthAlamFragment mHealthAlamFragment;
	private StepFragment mStepFragment;
	private WeatherFragment mWeatherFragment;
	private GeographicFragment mGeographicFragment;
	private CompassFragment mCompassFragment;
	private absFragment mFirstFramentTmp;

	private ViewPagerIndicator mCardViewPageIndicator;
	
	@Override
	protected int getLayoutResouceId() {
		// TODO Auto-generated method stub
		return R.layout.fragment_card_contain;
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		super.initView();
		mCardViewPageIndicator = findViewById(R.id.card_page_ind);
		
		mHorizalViewPager.setHorizalViewPagerSelected(this);
	}

	@Override
	protected void initData() {
		// TODO Auto-generated method stub
		super.initData();
		mCardViewPageIndicator.init(mFragmentList.size() - 2,ViewPagerIndicator.TYPE_CARD_CONTAIN);
		mCardViewPageIndicator.setSelect(getLoopIndicator(getPagerCurrIndex()));
	}
	@Override
	protected int getViewPagerId() {
		// TODO Auto-generated method stub
		return R.id.viewpager;
	}

	@Override
	protected void initFragmentList() {
		// TODO Auto-generated method stub
		mFragmentList.clear();

		mFragmentList.add(ITEM_INDEX_LAST_TMP, getmLastFragmentTmp());
		
		//mFragmentList.add(ITEM_INDEX_HEALTH, getmHealthFragment());
		mFragmentList.add(ITEM_INDEX_SLEEP, getmSleepFragment());
		mFragmentList.add(ITEM_INDEX_HEALTHALARM, getmHealthAlamFragment());
		mFragmentList.add(ITEM_INDEX_STEP, getmStepFragment());
		//mFragmentList.add(ITEM_INDEX_WEATHER, getmWeatherFragment()); //del by wzb 
		//mFragmentList.add(ITEM_INDEX_GEOGRAPHIC, getmGeographicFragment());//del by wzb 
		//mFragmentList.add(ITEM_INDEX_COMPASS, getmCompassFragment());//del by wzb
		
		mFragmentList.add(ITEM_INDEX_FIRST_TMP, getmFirstFramentTmp());
	}
	
	/*
	public HealthFragment getmHealthFragment() {
		mHealthFragment = (HealthFragment) findFragmentByIndex(ITEM_INDEX_HEALTH);
		if(mHealthFragment == null){
			mHealthFragment = new HealthFragment();
		}
		return mHealthFragment;
	}*/
	
	public SleepFragment getmSleepFragment() {
		mSleepFragment = (SleepFragment) findFragmentByIndex(ITEM_INDEX_SLEEP);
		if(mSleepFragment == null){
			mSleepFragment = new SleepFragment();
		}
		return mSleepFragment;
	}
	
	
	public HealthAlamFragment getmHealthAlamFragment() {
		mHealthAlamFragment = (HealthAlamFragment) findFragmentByIndex(ITEM_INDEX_HEALTHALARM);
		if(mHealthAlamFragment == null){
			mHealthAlamFragment = HealthAlamFragment.newInstance(false);
		}
		return mHealthAlamFragment;
	}

	public StepFragment getmStepFragment() {
		mStepFragment = (StepFragment) findFragmentByIndex(ITEM_INDEX_STEP);
		if(mStepFragment == null){
			mStepFragment = new StepFragment();
		}
		return mStepFragment;
	}
	/*
	public WeatherFragment getmWeatherFragment() {
		mWeatherFragment = (WeatherFragment) findFragmentByIndex(ITEM_INDEX_WEATHER);
		if(mWeatherFragment == null){
			mWeatherFragment = new WeatherFragment();
		}
		return mWeatherFragment;
	}
	
	public GeographicFragment getmGeographicFragment() {
		mGeographicFragment = (GeographicFragment) findFragmentByIndex(ITEM_INDEX_GEOGRAPHIC);
		if(mGeographicFragment == null){
			mGeographicFragment = GeographicFragment.newInstance(false);
		}
		return mGeographicFragment;
	}

	public CompassFragment getmCompassFragment() {
		mCompassFragment = (CompassFragment) findFragmentByIndex(ITEM_INDEX_COMPASS);
		if(mCompassFragment == null){
			mCompassFragment = CompassFragment.newInstance(false);
		}
		return mCompassFragment;
	}*///del by wzb

	public absFragment getmLastFragmentTmp() {
		mLastFragmentTmp = (StepFragment) findFragmentByIndex(ITEM_INDEX_LAST_TMP);
		if(mLastFragmentTmp == null){
			mLastFragmentTmp = new StepFragment();
		}
		return mLastFragmentTmp;
	}

	public absFragment getmFirstFramentTmp() {
		mFirstFramentTmp = (SleepFragment) findFragmentByIndex(ITEM_INDEX_FIRST_TMP);
		if(mFirstFramentTmp == null){
			mFirstFramentTmp = new SleepFragment();
		}
		return mFirstFramentTmp;
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
		return ITEM_INDEX_STEP;
	}
	
	private int getLoopIndicator(int position){

    	if(position == ITEM_COUNT - 1){
			return ITEM_INDEX_START;
		}else if(position == ITEM_INDEX_START){
			return ITEM_COUNT - 3;
		}else{
			return position - 1;
		}
    }

	@Override
	public void horizalViewPageSelected(int index) {
		// TODO Auto-generated method stub
		if(mFragmentList == null){
			return;
		}		
		if(mCardViewPageIndicator != null){
			mCardViewPageIndicator.setSelect(getLoopIndicator(index));
		}
	}

	public void resetPagerCurrIndex(){
		if(mHorizalViewPager != null){
			mHorizalViewPager.setCurrentItem(getDefaultPagerCurrIndex(), false);
		}
	}
}

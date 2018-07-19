package com.sczn.wearlauncher.activity;

import java.util.ArrayList;
import com.sczn.wearlauncher.R;
import com.sczn.wearlauncher.adapter.LoopViewPageAdapter;
import com.sczn.wearlauncher.fragment.BtConnectAndroidFragment;
import com.sczn.wearlauncher.fragment.BtConnectIosFragment;
import com.sczn.wearlauncher.fragment.absFragment;
import com.sczn.wearlauncher.view.HorizalViewPager;
import com.sczn.wearlauncher.view.ViewPagerIndicator;
import com.sczn.wearlauncher.view.HorizalViewPager.IHorizalViewPagerSelected;

import android.os.Bundle;

public class BtConnectActivity extends AbsActivity implements IHorizalViewPagerSelected{
	
	private static int VIEWPAGER_INDEX_START = 0;
	private static final int VIEWPAGER_INDEX_ANDROID = VIEWPAGER_INDEX_START++;
	//private static final int VIEWPAGER_INDEX_IOS = VIEWPAGER_INDEX_START++;
	private static final int VIEWPAGER_INDEX_COUNT = VIEWPAGER_INDEX_START;
	

	private HorizalViewPager mViewPager;
	private ViewPagerIndicator mPagerIndicator;
	private LoopViewPageAdapter mPageAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		initView();
		initData();
	}

	private void initView() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_btconnect);
		
		mViewPager = (HorizalViewPager) findViewById(R.id.bt_connectviewpager);
		mPagerIndicator = (ViewPagerIndicator) findViewById(R.id.bt_connect_ind);
	}

	private void initData() {
		// TODO Auto-generated method stub
		mPageAdapter = new LoopViewPageAdapter(getFragmentManager(), false, mViewPager);
		mViewPager.setHorizalViewPagerSelected(this);
		
		mViewPager.setAdapter(mPageAdapter);
		mPageAdapter.setList(getPagerList());
		mViewPager.setCurrentItem(VIEWPAGER_INDEX_ANDROID);
		
		mPagerIndicator.init(VIEWPAGER_INDEX_COUNT,ViewPagerIndicator.TYPE_CARD_CONTAIN);
		mPagerIndicator.setSelect(VIEWPAGER_INDEX_ANDROID);
	}
	
	private ArrayList<absFragment> getPagerList(){
		final ArrayList<absFragment> list = new ArrayList<absFragment>();
		list.add(VIEWPAGER_INDEX_ANDROID, new BtConnectAndroidFragment());
		//list.add(VIEWPAGER_INDEX_IOS, new BtConnectIosFragment());
		return list;
	}

	@Override
	public void horizalViewPageSelected(int index) {
		// TODO Auto-generated method stub
		mPagerIndicator.setSelect(index);
	}
}

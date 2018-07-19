package com.sczn.wearlauncher.activity;

import java.util.ArrayList;

import com.sczn.wearlauncher.R;
import com.sczn.wearlauncher.adapter.LoopViewPageAdapter;
import com.sczn.wearlauncher.fragment.absFragment;
import com.sczn.wearlauncher.fragment.clockmenu.MenuCircleFragment;
import com.sczn.wearlauncher.fragment.clockmenu.MenuSquareFragment;
import com.sczn.wearlauncher.fragment.clockmenu.MenuVirticalFragment;
import com.sczn.wearlauncher.util.AppListUtil;
import com.sczn.wearlauncher.util.MxyLog;
import com.sczn.wearlauncher.view.ViewPagerIndicator;
import com.sczn.wearlauncher.view.menu.StyleChooseViewPager;
import com.sczn.wearlauncher.view.menu.StyleChooseViewPager.IStyleViewPagerSelected;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.TextView;

public class StyleChooseActivity extends AbsActivity implements IStyleViewPagerSelected{
	private static final String TAG = StyleChooseActivity.class.getSimpleName();
	public static final int ITEM_INDEX_VIRTICAL = 0;
	public static final int ITEM_INDEX_SQUARE = ITEM_INDEX_VIRTICAL + 1;
	public static final int ITEM_INDEX_CIRCLE = ITEM_INDEX_SQUARE + 1;
	private MenuVirticalFragment mVirticalFragment;
	private MenuSquareFragment mSquareFragment;
	private MenuCircleFragment mCircleFragent;
	
	private ArrayList<absFragment> mFragmentList = new ArrayList<absFragment>(); 
	private StyleChooseViewPager mStyleViewPager;
	private LoopViewPageAdapter mViewpageAdapter;
	private ViewPagerIndicator mStyleViewPageIndicator;
	private TextView mSkinChange;
	
	private AppListUtil mAppListUtil;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		initAppMenu();
		
		setContentView(R.layout.activity_style_choose);
		initView();
		initData();
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		mAppListUtil.stopFresh(this);
		super.onDestroy();
	}
	
	private void initAppMenu() {
		// TODO Auto-generated method stub
		mAppListUtil = AppListUtil.getInctance();
		mAppListUtil.startFresh(this);
	}

	private void initView() {
		// TODO Auto-generated method stub
		mStyleViewPager = (StyleChooseViewPager) findViewById(R.id.style_viewpager);
		mStyleViewPager.setOffscreenPageLimit(2);
		mStyleViewPageIndicator = (ViewPagerIndicator) findViewById(R.id.style_choose_style_indicator);
		mSkinChange = (TextView) findViewById(R.id.style_choose_skin_text);
		
		mStyleViewPager.setHorizalViewPagerSelected(this);
		mSkinChange.setOnClickListener(onMskinChange);
	}
	
	private void initData() {
		// TODO Auto-generated method stub
		initViewPage();
		mStyleViewPageIndicator.init(mFragmentList.size(),ViewPagerIndicator.TYPE_STYLE_CHOOSE);
		
		final int defaultIndex = getDefaultIndex();
		mStyleViewPageIndicator.setSelect(defaultIndex);
		
		mStyleViewPager.setCurrentItem(defaultIndex);
	}
	private void initViewPage(){
		mViewpageAdapter = new LoopViewPageAdapter(this.getFragmentManager(),false,mStyleViewPager);
		mStyleViewPager.setAdapter(mViewpageAdapter);
		mFragmentList.clear();
		mFragmentList.add(ITEM_INDEX_VIRTICAL, getmVirticalFragment());
		mFragmentList.add(ITEM_INDEX_SQUARE, getmSquareFragment());
		mFragmentList.add(ITEM_INDEX_CIRCLE, getmCircleFragment());
		mViewpageAdapter.setList(mFragmentList);
	}

	private int getDefaultIndex(){
		final int style =  AppListUtil.getMenuStyle(this);
		if(AppListUtil.MENU_STYLE_CIRCLE == style){
			return ITEM_INDEX_CIRCLE;
		}
		if(style == AppListUtil.MENU_STYLE_SQUARE){
			return ITEM_INDEX_SQUARE;
		}
		return ITEM_INDEX_VIRTICAL;
	}

	public MenuVirticalFragment getmVirticalFragment() {
		mVirticalFragment =(MenuVirticalFragment) findFragmentByIndex(ITEM_INDEX_VIRTICAL);
		if(mVirticalFragment == null){
			mVirticalFragment = MenuVirticalFragment.newInstance(true,true);
		}
		return mVirticalFragment;
	}

	public MenuSquareFragment getmSquareFragment() {
		mSquareFragment =(MenuSquareFragment) findFragmentByIndex(ITEM_INDEX_SQUARE);
		if(mSquareFragment == null){
			mSquareFragment = MenuSquareFragment.newInstance(true, true);
		}
		return mSquareFragment;
	}
	public MenuCircleFragment getmCircleFragment() {
		mCircleFragent =(MenuCircleFragment) findFragmentByIndex(ITEM_INDEX_CIRCLE);
		if(mCircleFragent == null){
			mCircleFragent = MenuCircleFragment.newInstance(true, true);
		}
		return mCircleFragent;
	}
	
	@SuppressWarnings("unchecked")
	private <T extends Fragment> T findFragmentByIndex(int index){
		if(mStyleViewPager != null){
			final Fragment fragment = getFragmentManager().findFragmentByTag(
					LoopViewPageAdapter.makeFragmentName(mStyleViewPager.getId(), index));
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
	

	private OnClickListener onMskinChange = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if(AppListUtil.SKIN_PATH1.equals(AppListUtil.getMenuSkin(StyleChooseActivity.this))){
				AppListUtil.setMenuSkin(StyleChooseActivity.this, AppListUtil.SKIN_PATH2);
			}else{
				AppListUtil.setMenuSkin(StyleChooseActivity.this, AppListUtil.SKIN_PATH1);
			}
		}
	};

	@Override
	public void onStyleSelected(int index) {
		// TODO Auto-generated method stub
		if(ITEM_INDEX_CIRCLE == index){
			AppListUtil.setMenuStyle(this, AppListUtil.MENU_STYLE_CIRCLE);
		}else if(ITEM_INDEX_SQUARE == index){
			AppListUtil.setMenuStyle(this, AppListUtil.MENU_STYLE_SQUARE);
		}else{
			AppListUtil.setMenuStyle(this, AppListUtil.MENU_STYLE_VERTICAL);
		}
		mStyleViewPageIndicator.setSelect(index);
	}

}

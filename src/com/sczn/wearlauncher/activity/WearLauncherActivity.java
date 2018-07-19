package com.sczn.wearlauncher.activity;

import java.util.ArrayList;

import com.sczn.wearlauncher.R;
import com.sczn.wearlauncher.adapter.LoopViewPageAdapter;
import com.sczn.wearlauncher.fragment.CardContainFragment;
import com.sczn.wearlauncher.fragment.ClockMenuContainFragment;
import com.sczn.wearlauncher.fragment.StatusContainFragment;
import com.sczn.wearlauncher.fragment.absDialogFragment;
import com.sczn.wearlauncher.fragment.absFragment;
import com.sczn.wearlauncher.receivers.AbsBroadcastReceiver;
import com.sczn.wearlauncher.util.AppListUtil;
import com.sczn.wearlauncher.util.MxyLog;
import com.sczn.wearlauncher.util.MxyToast;
import com.sczn.wearlauncher.view.VerticalViewPager;
import com.sczn.wearlauncher.view.VerticalViewPager.VerticalScrollerAble;

import android.provider.Settings;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.content.BroadcastReceiver;  

public class WearLauncherActivity extends AbsActivity implements VerticalScrollerAble{
	private final static String TAG = WearLauncherActivity.class.getSimpleName();
	
	public static final String ARG_IS_GOTO_CLOCK = "isGotoClock";
	public static final String ARG_VIEWPAGER_CURR_INDDEX = "arg_curr_index";
	public static final int ITEM_INDEX_STATUS = 0;
	public static final int ITEM_INDEX_CLOCKMENU = ITEM_INDEX_STATUS + 1;
	public static final int ITEM_INDEX_CARD = ITEM_INDEX_CLOCKMENU + 1;
	
	private PlugStateReceiver mPlugStateReceiver;
	private VerticalViewPager mMainViewpager;
	private ArrayList<absFragment> mFragmentList = new ArrayList<absFragment>(); 
	private LoopViewPageAdapter mViewPageAdapter;
	
	
	private ClockMenuContainFragment mClockMenuContain;
	private StatusContainFragment mStatusContain;
	private CardContainFragment mCardContain;
	private int viewPagerCurrIndex = ITEM_INDEX_CLOCKMENU;
	
	private AppListUtil mAppListUtil;
	private boolean isInMainClock = false;
	private boolean isResume = false;;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MxyLog.d(TAG, "onCreate" + getIntent().getExtras());
        mPlugStateReceiver = new PlugStateReceiver();
        mPlugStateReceiver.register(this);
        
        initAppMenu();
        
        if(savedInstanceState != null){
        	viewPagerCurrIndex = savedInstanceState.getInt(ARG_VIEWPAGER_CURR_INDDEX, ITEM_INDEX_CLOCKMENU);
        	MxyLog.i(TAG, "onCreate--viewPagerCurrIndex=" + viewPagerCurrIndex);
        }else{
        	viewPagerCurrIndex = ITEM_INDEX_CLOCKMENU;
        }
        setContentView(R.layout.activity_launcher);  
		
		
        
        initView();
        initData();
        
        //CheckIntent();
    }
    
    @Override
    protected void onNewIntent(Intent intent) {
    	// TODO Auto-generated method stub
    	super.onNewIntent(intent);
    	
    	setIntent(intent);
    	CheckIntent();
    }
    
    private void CheckIntent(){
    	if(getIntent().getBooleanExtra(ARG_IS_GOTO_CLOCK, false)){
    		viewPagerCurrIndex = ITEM_INDEX_CLOCKMENU;
    		getmClockMenuContain().setToClock(true);
    		if(mDialogFragments != null){
    			for(absDialogFragment dialog : mDialogFragments){
    				dialog.dismissAllowingStateLoss();
    			}
    		}
    	}
    }
    
    
    @Override
    protected void onDestroy() {
    	// TODO Auto-generated method stub
    	mAppListUtil.stopFresh(this);
    	mPlugStateReceiver.unRegister(this);
    	super.onDestroy();
    }
    
    @Override
	protected void onResume() {
		// TODO Auto-generated method stub
    	isResume = true;
		super.onResume();
		if(viewPagerCurrIndex != mMainViewpager.getCurrentItem()){
			mMainViewpager.setCurrentItem(viewPagerCurrIndex);
		}
		customGestureBack();
		
		IntentFilter filter = new IntentFilter();  
        filter.addAction("com.sczn.FistView");  
        //registerReceiver(setFistViewReceiver, filter);
	}
    
    @Override
    protected void onPause() {
    	// TODO Auto-generated method stub
    	isResume = false;
    	viewPagerCurrIndex = mMainViewpager.getCurrentItem();
    	customGestureBack();
    	super.onPause();
		//unregisterReceiver(setFistViewReceiver);
    }
    
    private void initAppMenu() {
		// TODO Auto-generated method stub
		mAppListUtil = AppListUtil.getInctance();
		mAppListUtil.startFresh(this);
	}

	@Override
    public void onConfigurationChanged(Configuration newConfig) {
    	// TODO Auto-generated method stub
    	super.onConfigurationChanged(newConfig);
    	AppListUtil.getInctance().executeTask();
    	finish();
    }
	
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onRestoreInstanceState(savedInstanceState);
		if(savedInstanceState != null){
        	viewPagerCurrIndex = savedInstanceState.getInt(ARG_VIEWPAGER_CURR_INDDEX, ITEM_INDEX_CLOCKMENU);
        	MxyLog.i(TAG, "onCreate--viewPagerCurrIndex=" + viewPagerCurrIndex);
        }else{
        	viewPagerCurrIndex = ITEM_INDEX_CLOCKMENU;
        }
		MxyLog.i(TAG, "onRestoreInstanceState--viewPagerCurrIndex=" + viewPagerCurrIndex);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		if(mMainViewpager != null){
			viewPagerCurrIndex = mMainViewpager.getCurrentItem();
			outState.putInt(ARG_VIEWPAGER_CURR_INDDEX, viewPagerCurrIndex);
			MxyLog.i(TAG, "onSaveInstanceState--viewPagerCurrIndex=" + mMainViewpager.getCurrentItem());
		}
		super.onSaveInstanceState(outState);
		
	}

	private void initView() {
		// TODO Auto-generated method stub
		mMainViewpager = (VerticalViewPager) findViewById(R.id.vertical_viewpager);

	}
	
	private void initData() {
		// TODO Auto-generated method stub
		mFragmentList.clear();
		mFragmentList.add(ITEM_INDEX_STATUS, getmStatusContain());
		mFragmentList.add(ITEM_INDEX_CLOCKMENU, getmClockMenuContain());
		mFragmentList.add(ITEM_INDEX_CARD, getmCardContain());
		
		mViewPageAdapter = new LoopViewPageAdapter(getFragmentManager(),false,mMainViewpager);
		mMainViewpager.setAdapter(mViewPageAdapter);
		mViewPageAdapter.setList(mFragmentList);	
		mMainViewpager.setVerticalScrollerAble(this);
		mMainViewpager.setOnPageChangeListener(MainViewPagerPageChangeListener);
	}

	public ClockMenuContainFragment getmClockMenuContain() {
		if(mMainViewpager != null){
			final Fragment fragment = getFragmentManager().findFragmentByTag(
					LoopViewPageAdapter.makeFragmentName(mMainViewpager.getId(), ITEM_INDEX_CLOCKMENU));
			if(fragment instanceof ClockMenuContainFragment){
				mClockMenuContain = (ClockMenuContainFragment) fragment;
			}
		}
		if(mClockMenuContain == null){
			mClockMenuContain = ClockMenuContainFragment.newInstance(false);
		}
		return mClockMenuContain;
	}

	public StatusContainFragment getmStatusContain() {
		if(mMainViewpager != null){
			final Fragment fragment = getFragmentManager().findFragmentByTag(
					LoopViewPageAdapter.makeFragmentName(mMainViewpager.getId(), ITEM_INDEX_STATUS));
			if(fragment instanceof StatusContainFragment){
				mStatusContain = (StatusContainFragment) fragment;
			}
		}
		if(mStatusContain == null){
			mStatusContain = StatusContainFragment.newInstance(true);
		}
		return mStatusContain;
	}

	public CardContainFragment getmCardContain() {
		if(mMainViewpager != null){
			final Fragment fragment = getFragmentManager().findFragmentByTag(
					LoopViewPageAdapter.makeFragmentName(mMainViewpager.getId(), ITEM_INDEX_CARD));
			if(fragment instanceof CardContainFragment){
				mCardContain = (CardContainFragment) fragment;
			}
		}
		if(mCardContain == null){
			mCardContain = CardContainFragment.newInstance(true);
		}
		return mCardContain;
	}

	@Override
	public boolean isVerticalScrollerAble() {
		// TODO Auto-generated method stub
		if(mMainViewpager == null){
			return false;
		}
		if(mMainViewpager.getCurrentItem() != ITEM_INDEX_CLOCKMENU){
			return true;
		}
		if(getmClockMenuContain().canScrollerVirtical()){
			return true;
		}
		return false;
	}
	
	private void customPowerKey(){
		if(mDialogFragments != null && mDialogFragments.size() > 0){
			Settings.System.putInt(getContentResolver(), "isHome", 0);
			return;
		}
		Settings.System.putInt(getContentResolver(), "isHome", isInMainClock?1:0);
	}
	private void customGestureBack(){
		if(mDialogFragments != null && mDialogFragments.size() > 0){
			Settings.System.putInt(getContentResolver(), "disable_gesture_back", 0);
			return;
		}
		Settings.System.putInt(getContentResolver(), "disable_gesture_back", isResume?1:0);
	}

	public void dialogManager(boolean isAdd, String tag, absDialogFragment dialog){
		//MxyLog.d(TAG, "dialogManager" + "--tag=" + tag + "--isAdd=" + isAdd + "--count=" + mDialogs.size());
		super.dialogManager(isAdd, tag, dialog);
		customPowerKey();
		customGestureBack();
	}
	
	public void setInMainClock(boolean isInMainClock){
		this.isInMainClock = isInMainClock;
		customPowerKey();
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		MxyLog.d(this,"onBackPressed--mMainViewpager.getCurrentItem()="  + mMainViewpager.getCurrentItem());
		if(mMainViewpager.getCurrentItem() != ITEM_INDEX_CLOCKMENU){
			mMainViewpager.setCurrentItem(ITEM_INDEX_CLOCKMENU, true);
			return;
		}
		
		if(!getmClockMenuContain().canScrollerVirtical()){
			getmClockMenuContain().setToClock(true);
			return;
		}
		
		MxyLog.e(TAG, "onBackPressed");
		Settings.System.putInt(getContentResolver(), "isHome", 1);
	}
	
	private void resetCardAndStatus(){
		getmCardContain().resetPagerCurrIndex();
		getmStatusContain().resetPagerCurrIndex();
	}
	
	private OnPageChangeListener MainViewPagerPageChangeListener = new OnPageChangeListener() {
		
		@Override
		public void onPageSelected(int arg0) {
			// TODO Auto-generated method stub
			if(ITEM_INDEX_CLOCKMENU == arg0){
				resetCardAndStatus();
			}
		}
		
		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void onPageScrollStateChanged(int arg0) {
			// TODO Auto-generated method stub
			
		}
	};
	
	private class PlugStateReceiver extends AbsBroadcastReceiver{

		@Override
		public IntentFilter getIntentFilter() {
			// TODO Auto-generated method stub
			IntentFilter filter = new IntentFilter();
			filter.addAction(Intent.ACTION_POWER_CONNECTED);
			filter.addAction(Intent.ACTION_POWER_DISCONNECTED);
			return filter;
		}

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			if(Intent.ACTION_POWER_CONNECTED.equals(intent.getAction())){
				showPowerToast(context, true);
				return;
			}
			if(Intent.ACTION_POWER_DISCONNECTED.equals(intent.getAction())){
				showPowerToast(context, false);
				return;
			}
		}
		
		private void showPowerToast(Context context, boolean isPower){
			final View view = LayoutInflater.from(context).inflate(R.layout.toast_power, null);
			final TextView stateText = (TextView) view.findViewById(R.id.powerState);
			final ImageView stateImage = (ImageView) view.findViewById(R.id.powerImage);
			if(isPower){
				stateText.setText(R.string.power_connect);
				stateImage.setImageResource(R.drawable.power_connected);
			}else{
				stateText.setText(R.string.power_disconnect);
				stateImage.setImageResource(R.drawable.power_disconnected);
			}
			MxyToast.showView(context, view);
		}
		
	}
	
	private BroadcastReceiver setFistViewReceiver = new BroadcastReceiver(){  
  
        @Override  
        public void onReceive(Context context, Intent intent) {  
            // TODO Auto-generated method stub  
			
			if(mMainViewpager.getCurrentItem() != ITEM_INDEX_CLOCKMENU){
				mMainViewpager.setCurrentItem(ITEM_INDEX_CLOCKMENU, true);
				return;
			}
			if(!getmClockMenuContain().canScrollerVirtical()){
				getmClockMenuContain().setToClock(true);
				return;
			}
			
			
        }  
          
    };  

}

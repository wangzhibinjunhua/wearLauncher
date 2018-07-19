package com.sczn.wearlauncher.fragment;

import java.lang.reflect.Field;
import java.util.Locale;

import com.sczn.wearlauncher.util.MxyLog;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

public abstract class absFragment extends Fragment implements Cloneable{
	
	private static final String TAG = absFragment.class.getSimpleName();
	private static final Boolean DEBUG = false;
	
	protected Activity mActivity;
	protected View mRootView;

	private boolean isUserVisible = false;
	protected Boolean isResumed = false;
	protected Locale mCurrLocale;
	
	protected absFragment mParentFragment;
	
	private void absFragmentLog(String info){
		if(DEBUG){
			MxyLog.d(this,"--" + info);
		}
	}
	
	public void setParentFragment(absFragment viewPagerFragment){
		this.mParentFragment = viewPagerFragment;
		onParentVisibleChange();
	}

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		absFragmentLog("onAttach");
		super.onAttach(activity);
		mActivity = activity;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		absFragmentLog("onCreate");
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		absFragmentLog("onCreateView");
		mRootView = inflater.inflate(getLayoutResouceId(), container, false);
		initView();
		return mRootView;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		absFragmentLog("onViewCreated");
		initData();
		super.onViewCreated(view, savedInstanceState);
	}
	

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		absFragmentLog("onStart");
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		isResumed = true;
		absFragmentLog("onResume");
		if(isUserVisible()){
			startFreshData();
		}
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		absFragmentLog("onPause");
		isResumed = false;
		endFreshData();
		super.onPause();
	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		absFragmentLog("onStop");
		super.onStop();
	}

	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		super.onDestroyView();
	}
	
	@Override  
	public void onDetach() {  
		absFragmentLog("onDetach");
		super.onDetach();  
	    try {  
	    	Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");  
	        childFragmentManager.setAccessible(true);  
	        childFragmentManager.set(this, null);  
	  
	    } catch (NoSuchFieldException e) {  
	        throw new RuntimeException(e);  
	    } catch (IllegalAccessException e) {  
	    	throw new RuntimeException(e);  
	    }  
	      
	}
	
	private void changeFreshState(){
		if(!isResumed){
			return;
		}
		if(isUserVisible()){
			startFreshData();
		}else{
			endFreshData();
		}
	}

	public void onParentVisibleChange(){
		changeFreshState();
	}
	
	protected boolean isUserVisible() {
		if(mParentFragment != null){
			if(!mParentFragment.isUserVisible()){
				return false;
			}
		}
		return isUserVisible;
	}

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		// TODO Auto-generated method stub
		absFragmentLog("setUserVisibleHint =" + isVisibleToUser);
		//MxyLog.d(TAG , "--" + toString() + "--" + "setUserVisibleHint =" + isVisibleToUser);
		if(getActivity() != null && getFragmentManager() != null){
			super.setUserVisibleHint(isVisibleToUser);
		}

		this.isUserVisible = isVisibleToUser;
		
		changeFreshState();
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
	
	
	public <T extends absFragment> T cloneInstance(){
		T ac = null;
		try {
			ac = (T) clone();
		} catch (Exception e) {
			// TODO: handle exception
		}
		return ac;
	}
	
	
	@Override
	protected Object clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return super.clone();
	}
	
	protected void startFreshData(){
		if(mCurrLocale == null){
			mCurrLocale = (Locale) getResources().getConfiguration().locale.clone();
			return;
		}
		if(!mCurrLocale.equals(getResources().getConfiguration().locale)){
			mCurrLocale = (Locale) getResources().getConfiguration().locale.clone();
			onLocaleChanged();
		}

	}
	
	protected void onLocaleChanged() {
		// TODO Auto-generated method stub
		
	}
	
	protected void viewReload(View view){
		if(view != null){
			view.postInvalidate();
		}
	}

	protected void endFreshData(){

	}

	protected abstract int getLayoutResouceId();
	protected abstract void initView();
	protected abstract void initData();
	
	protected <T extends View> T findViewById(int resId){
		if(mRootView == null){
			return null;
		}
		
		final View view = mRootView.findViewById(resId);
		if(view == null){
			return null;
		}
		return  (T) view;
	}
}

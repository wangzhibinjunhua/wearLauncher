package com.sczn.wearlauncher.fragment;

import com.sczn.wearlauncher.R;
import com.sczn.wearlauncher.activity.WearLauncherActivity;
import com.sczn.wearlauncher.util.SysServices;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.ContentResolver;
import android.os.Bundle;
import android.provider.Settings;
import android.provider.Settings.SettingNotFoundException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class absDialogFragment extends DialogFragment {

	protected View mRootView;
	protected WearLauncherActivity mWearLauncherActivity;
	protected String mTag;
	
	public absDialogFragment(){
		setShowsDialog(false);
		setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
	}
	
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		if(activity instanceof WearLauncherActivity){
			mWearLauncherActivity = (WearLauncherActivity) activity;
			mTag = getTag();
			if(mTag == null){
				mTag = getClass().getSimpleName();
			}
			
			mWearLauncherActivity.dialogManager(true, mTag, this);
		}
	}
	
	@Override
	public void onDetach() {
		// TODO Auto-generated method stub
		if(mWearLauncherActivity != null){
			mWearLauncherActivity.dialogManager(false, mTag, this);
		}
		mWearLauncherActivity = null;
		super.onDetach();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		mRootView = inflater.inflate(getLayoutResouceId(), container, false);
		creatView();
		return mRootView;
	}

	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		destorytView();
		super.onDestroyView();
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		final Dialog dialog =  super.onCreateDialog(savedInstanceState);
		dialog.getWindow().getAttributes().windowAnimations = R.style.dialogAnim;
		return dialog;
	}
	
	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		//dismiss();
	}
	@Override
	public void show(FragmentManager manager, String tag) {
		// TODO Auto-generated method stub
		if(!isAdded()){
			super.show(manager, tag);
		}
	}
	@Override
	public void dismiss() {
		// TODO Auto-generated method stub
		super.dismiss();
	}
	public int getSystemSettingInt(String name){
		return SysServices.getSystemSettingInt(getActivity(), name);
	}
	
	public void setSystemSettingInt(String name,int value){
		SysServices.setSystemSettingInt(getActivity(), name, value);
	}
	
	
	protected abstract int getLayoutResouceId();
	protected abstract void creatView();
	protected abstract void destorytView();
	
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

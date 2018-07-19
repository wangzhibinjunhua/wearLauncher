package com.sczn.wearlauncher.activity;

import java.util.ArrayList;

import com.sczn.wearlauncher.fragment.absDialogFragment;
import com.sczn.wearlauncher.util.MxyLog;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.KeyEvent;

public abstract class AbsActivity extends Activity {
	private static final String TAG = AbsActivity.class.getSimpleName();

	public static final String ARG_DIALOG_COUNT = "arg_dialog_count";
	protected int mDialogCount;
	protected ArrayList<String> mDialogs;
	protected ArrayList<absDialogFragment> mDialogFragments;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
        if(savedInstanceState != null){
        	mDialogs = savedInstanceState.getStringArrayList(ARG_DIALOG_COUNT);
        	MxyLog.i(TAG, "onCreate--mDialogCount=" + mDialogCount);
        }
        if(mDialogs == null){
        	mDialogs = new ArrayList<String>();
        }
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
	
	public void dialogManager(boolean isAdd, String tag, absDialogFragment dialog){
		//MxyLog.d(TAG, "dialogManager" + "--tag=" + tag + "--isAdd=" + isAdd + "--count=" + mDialogs.size());
		if(mDialogs == null){
			mDialogs = new ArrayList<String>();
		}
		if(isAdd && !mDialogs.contains(tag)){
			mDialogs.add(tag);
		}else if(!isAdd && mDialogs.contains(tag)){
			mDialogs.remove(tag);
		}
		
		if(mDialogFragments == null){
			mDialogFragments = new ArrayList<absDialogFragment>();
		}
		if(isAdd){
			mDialogFragments.add(dialog);
		}else{
			mDialogFragments.remove(dialog);
		}
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		outState.putStringArrayList(ARG_DIALOG_COUNT, mDialogs);
		super.onSaveInstanceState(outState);
	}
	
	@Override
	public void onAttachFragment(Fragment fragment) {
		// TODO Auto-generated method stub
		super.onAttachFragment(fragment);
	}


	@Override
	public boolean onKeyLongPress(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		return super.onKeyLongPress(keyCode, event);
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
	}
}

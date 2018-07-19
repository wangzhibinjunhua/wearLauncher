package com.sczn.wearlauncher.fragment.status;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.View.OnClickListener;

import com.sczn.wearlauncher.LauncherApp;
import com.sczn.wearlauncher.R;
import com.sczn.wearlauncher.activity.BtConnectActivity;
import com.sczn.wearlauncher.fragment.QrcodeFragment;
import com.sczn.wearlauncher.fragment.absFragment;
import com.sczn.wearlauncher.fragment.card.AltitudeFragment;
import com.sczn.wearlauncher.util.MxyLog;
import com.sczn.wearlauncher.view.ClickIcon;

public class FirstStatusFragment extends absFragment{
	
	public static final String FRAGMENT_TAG_QRCODE = "status_tag_qrcode";
	public static final String FRAGMENT_TAG_VOLUME = "status_tag_volume";
	
	public static final String ARG_IS_TMP = "is_tmp";
	public static FirstStatusFragment newInstance(boolean isTmp){
		FirstStatusFragment fragment = new FirstStatusFragment();
		Bundle bdl = new Bundle();
		bdl.putBoolean(ARG_IS_TMP, isTmp);
		fragment.setArguments(bdl);
		return fragment;
		
	}
	
	private boolean isTmp = false;

	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		MxyLog.d(this, "onCreate" + isTmp);
		super.onCreate(savedInstanceState);
		Bundle bdl = getArguments();
		if(bdl != null){
			isTmp = bdl.getBoolean(ARG_IS_TMP, false);
		}
	}
	
	@Override
	protected int getLayoutResouceId() {
		// TODO Auto-generated method stub
		return R.layout.fragment_status_first;
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
	}

	@Override
	protected void initData() {
		// TODO Auto-generated method stub
	}
}

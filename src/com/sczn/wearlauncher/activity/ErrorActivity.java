package com.sczn.wearlauncher.activity;

import com.sczn.wearlauncher.R;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class ErrorActivity extends AbsActivity implements OnClickListener{
	
	public static final String ARG_MESSAGE = "error_message";
	private TextView mErrorMessage;
	private TextView mErrorStart;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		initView();
		initData();
	}
	
	private void initView() {
		// TODO Auto-generated method stub
		mErrorMessage = (TextView) findViewById(R.id.error_message);
		mErrorStart = (TextView) findViewById(R.id.error_restart);
	}

	private void initData() {
		// TODO Auto-generated method stub
		mErrorMessage.setOnClickListener(this);
		if(getIntent() != null){
			final String msg = getIntent().getStringExtra(ARG_MESSAGE);
			mErrorMessage.setText(msg);
		}
	}
	
	private void restartApk(){
		android.os.Process.killProcess(android.os.Process.myPid()); 
		System.exit(10);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
			case R.id.error_restart:
				
				break;
	
			default:
				break;
		}
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		//super.onBackPressed();
	}
}

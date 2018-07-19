package com.sczn.wearlauncher.card;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

public abstract class CardActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(getLayoutId());
		initView();
		initData();
	}

	protected abstract int getLayoutId();
	protected abstract void initView();
	protected abstract void initData();
}

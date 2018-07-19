package com.sczn.wearlauncher.activity;

import java.util.ArrayList;
import java.util.Iterator;

import com.sczn.wearlauncher.R;
import com.sczn.wearlauncher.model.AppMenu;
import com.sczn.wearlauncher.util.SysServices;
import com.sczn.wearlauncher.util.NotificationUtil.MyNotification;
import com.sczn.wearlauncher.view.menu.MenuIconImage;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class BtStyleChooseActivity extends AbsActivity implements OnClickListener{
	private static final String TAG = BtStyleChooseActivity.class.getSimpleName();
	
	public static final String SETTING_KEY_BT_STYLE = "ble_type";
	public static final int BT_STYLE_UNKNOW= -1;
	public static final int BT_STYLE_ANDROID = 0;
	public static final int BT_STYLE_IOS = 1;
	
	public static final String ARG_STYLE_CHOOSE = "bt_style_choose";
	
	public static final int RESULT_CODE_OK = 1;
	public static final int RESULT_CODE_CANCLE = -1;
	
	private MenuIconImage mChooseAndroid;
	private MenuIconImage mChooseIos;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		initView();
		initData();
	}
	
	private void initView() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_btstylechoose);
		mChooseAndroid = (MenuIconImage) findViewById(R.id.bt_style_choose_android);
		mChooseIos = (MenuIconImage) findViewById(R.id.bt_style_choose_ios);
	}

	private void initData() {
		// TODO Auto-generated method stub
		mChooseAndroid.setOnClickListener(this);
		mChooseIos.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
			case R.id.bt_style_choose_android:
				SysServices.setSystemSettingInt(this, SETTING_KEY_BT_STYLE, BT_STYLE_ANDROID);
				setResult(RESULT_CODE_OK, getIntent().putExtra(ARG_STYLE_CHOOSE, BT_STYLE_ANDROID));
				finish();
				break;
			case R.id.bt_style_choose_ios:
				SysServices.setSystemSettingInt(this, SETTING_KEY_BT_STYLE, BT_STYLE_IOS);
				setResult(RESULT_CODE_OK, getIntent().putExtra(ARG_STYLE_CHOOSE, BT_STYLE_IOS));
				finish();
				break;
	
			default:
				break;
		}
	}

}

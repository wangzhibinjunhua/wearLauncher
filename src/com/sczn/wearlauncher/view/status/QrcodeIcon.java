package com.sczn.wearlauncher.view.status;

import com.sczn.wearlauncher.LauncherApp;
import com.sczn.wearlauncher.activity.BtConnectActivity;
import com.sczn.wearlauncher.util.MxyLog;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;

public class QrcodeIcon extends StatusIconWithText {
	

	public QrcodeIcon(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		getIcon().setOnClickListener(showQrCode);
	}
	
	private OnClickListener showQrCode = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent i = new Intent(LauncherApp.appContext, BtConnectActivity.class);
			getContext().startActivity(i);
		}
	};
}

package com.sczn.wearlauncher.view.status;

import com.sczn.wearlauncher.services.SosService;

import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.util.AttributeSet;
import android.view.View;

public class SosIcon extends StatusIconWithText{

	public SosIcon(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		mIcon.setOnClickListener(startSos);
	}
	
	private OnClickListener startSos = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			SosService.startService(getContext().getApplicationContext());
		}
	};
}

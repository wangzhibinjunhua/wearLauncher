package com.sczn.wearlauncher.view.status;

import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.util.AttributeSet;
import android.view.View;

public class BrightnessIcon extends StatusIconWithText{

	private IBrightnessClick mBrightnessClick;
	public BrightnessIcon(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		mIcon.setOnClickListener(gotoSetting);
	}
	
	private OnClickListener gotoSetting = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if(mBrightnessClick != null){
				mBrightnessClick.onBrightnessClick();
			}
		}
	};
	
	public void setClickCb(IBrightnessClick cb){
		this.mBrightnessClick = cb;
	}
	
	public interface IBrightnessClick{
		void onBrightnessClick();
	}
}

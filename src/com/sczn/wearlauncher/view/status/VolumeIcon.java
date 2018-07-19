package com.sczn.wearlauncher.view.status;

import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.util.AttributeSet;
import android.view.View;

public class VolumeIcon extends StatusIconWithText{

	private IVolumeClick mVolumeClick;
	public VolumeIcon(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		mIcon.setOnClickListener(gotoSetting);
	}
	
	private OnClickListener gotoSetting = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if(mVolumeClick != null){
				mVolumeClick.onVolumeClick();
			}
		}
	};
	
	public void setClickCb(IVolumeClick cb){
		this.mVolumeClick = cb;
	}
	
	public interface IVolumeClick{
		void onVolumeClick();
	}
}

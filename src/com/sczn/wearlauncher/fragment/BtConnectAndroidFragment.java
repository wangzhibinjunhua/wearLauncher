package com.sczn.wearlauncher.fragment;

import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.sczn.wearlauncher.R;
import com.sczn.wearlauncher.view.ClickIcon;

public class BtConnectAndroidFragment extends absFragment implements OnClickListener{
	
	public static final String FRAGMENT_TAG_QRCODE = "btconnect_tag_qrcode";
	private ClickIcon mQrCodeImage;
	private TextView mAndroidGuide;

	@Override
	protected int getLayoutResouceId() {
		// TODO Auto-generated method stub
		return R.layout.fragment_bt_connect_android;
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		mQrCodeImage = findViewById(R.id.bt_connect_qrcode);
		mAndroidGuide = findViewById(R.id.bt_connect_android_guide);
	}

	@Override
	protected void initData() {
		// TODO Auto-generated method stub
		mQrCodeImage.setOnClickListener(this);
		mAndroidGuide.setText(Html.fromHtml(getString(R.string.bt_connecet_android_guide)));
	}
	
	private void showQrCodeFragment(){
		QrcodeFragment.getInstance().show(getChildFragmentManager(), FRAGMENT_TAG_QRCODE);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
			case R.id.bt_connect_qrcode:
				showQrCodeFragment();
				break;
	
			default:
				break;
		}
	}

}

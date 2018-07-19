package com.sczn.wearlauncher.fragment;

import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.sczn.wearlauncher.R;

public class BtConnectIosFragment extends absFragment implements OnClickListener{
	public static final String ACTION_START_ADV = "com.sczn.action.startbleadv";

	private TextView IosGuide;
	private TextView AdvStart;
	@Override
	protected int getLayoutResouceId() {
		// TODO Auto-generated method stub
		return R.layout.fragment_bt_connect_ios;
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		IosGuide = findViewById(R.id.bt_connect_ios_guide);
		AdvStart = findViewById(R.id.bt_connect_ios_adv);
	}

	@Override
	protected void initData() {
		// TODO Auto-generated method stub

		//IosGuide.setText(String.format(Html.fromHtml(getString(R.string.bt_connecet_ios_guide)).toString(), android.os.Build.MODEL));
		IosGuide.setText(Html.fromHtml(String.format(getString(R.string.bt_connecet_ios_guide), android.os.Build.MODEL)));

		AdvStart.setOnClickListener(this);
	}

	private void startGattAdv(){
		Intent i = new Intent(ACTION_START_ADV);
		if(getActivity() != null){
			getActivity().sendBroadcast(i);
		}
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
			case R.id.bt_connect_ios_adv:
				startGattAdv();
				break;
	
			default:
				break;
		}
	}

}

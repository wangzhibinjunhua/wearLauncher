package com.sczn.wearlauncher.receivers;

import com.sczn.wearlauncher.R;
import com.sczn.wearlauncher.util.MxyToast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class PowerReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		if(Intent.ACTION_POWER_CONNECTED.equals(intent.getAction())){
			showPowerToast(context, true);
			return;
		}
		if(Intent.ACTION_POWER_DISCONNECTED.equals(intent.getAction())){
			showPowerToast(context, false);
			return;
		}
	}
	
	private void showPowerToast(Context context, boolean isPower){
		final View view = LayoutInflater.from(context).inflate(R.layout.toast_power, null);
		final TextView stateText = (TextView) view.findViewById(R.id.powerState);
		final ImageView stateImage = (ImageView) view.findViewById(R.id.powerImage);
		if(isPower){
			stateText.setText(R.string.power_connect);
			stateImage.setImageResource(R.drawable.power_connected);
		}else{
			stateText.setText(R.string.power_disconnect);
			stateImage.setImageResource(R.drawable.power_disconnected);
		}
		MxyToast.showView(context, view);
	}

}

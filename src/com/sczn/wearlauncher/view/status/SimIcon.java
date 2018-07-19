package com.sczn.wearlauncher.view.status;

import com.sczn.wearlauncher.R;
import com.sczn.wearlauncher.receivers.AbsBroadcastReceiver;
import com.sczn.wearlauncher.util.SysServices;
import com.sczn.wearlauncher.view.ClickIcon;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.util.AttributeSet;

public class SimIcon extends StatusIconWithText {
	
	private static final String TAG = SimIcon.class.getSimpleName();
	
	public static final int SIM_STATE_AIRMODE = 0;
	public static final int SIM_STATE_READY = 1;
	public static final int SIM_STATE_UNREADY = 2;
	public static final int SIM_STATE_NO = 3;
	
	public static final int SIM_SIGNAL_0 = 0;
	public static final int SIM_SIGNAL_1 = 1;
	public static final int SIM_SIGNAL_2 = 2;
	public static final int SIM_SIGNAL_3 = 3;
	
	public static final int NETWORK_CLASS_UNKNOWN = 0;
    public static final int NETWORK_CLASS_2_G = 1;
    public static final int NETWORK_CLASS_3_G = 2;
    public static final int NETWORK_CLASS_4_G = 3;
	
	
	private SimReceiver mSimReceiver;
	private SimSignalListener mSimSignalListener;
	private int simState = SIM_STATE_UNREADY;
	private int simImage = SIM_SIGNAL_0;
	private TelephonyManager mTlpMgr;

	public SimIcon(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		mTlpMgr =  (TelephonyManager) getContext().getSystemService(Context.TELEPHONY_SERVICE);
		setClickable(false);
		mSimReceiver = new SimReceiver();
		mSimSignalListener = new SimSignalListener();
	}
	
	@Override
	public void startFresh() {
		// TODO Auto-generated method stub
		super.startFresh();
		mSimSignalListener.startSignalListen();
		mSimReceiver.register(getContext());
		setSimState();
	}
	@Override
	public void stopFresh() {
		// TODO Auto-generated method stub
		mSimReceiver.unRegister(getContext());
		mSimSignalListener.stopSignalListen();
		super.stopFresh();
	}

	@Override
	protected void onAttachedToWindow() {
		// TODO Auto-generated method stub
		super.onAttachedToWindow();
		//mSimSignalListener.startSignalListen();
		//mSimReceiver.register(getContext());
		setSimState();
	}
	
	@Override
	protected void onDetachedFromWindow() {
		// TODO Auto-generated method stub
		//mSimReceiver.unRegister(getContext());
		//mSimSignalListener.stopSignalListen();
		super.onDetachedFromWindow();
	}
	
	private int getNetworkClass(int networkType) {
        switch (mTlpMgr.getNetworkType()) {
            case TelephonyManager.NETWORK_TYPE_GPRS:
            case TelephonyManager.NETWORK_TYPE_EDGE:
            case TelephonyManager.NETWORK_TYPE_CDMA:
            case TelephonyManager.NETWORK_TYPE_1xRTT:
            case TelephonyManager.NETWORK_TYPE_IDEN:
                return NETWORK_CLASS_2_G;
            case TelephonyManager.NETWORK_TYPE_UMTS:
            case TelephonyManager.NETWORK_TYPE_EVDO_0:
            case TelephonyManager.NETWORK_TYPE_EVDO_A:
            case TelephonyManager.NETWORK_TYPE_HSDPA:
            case TelephonyManager.NETWORK_TYPE_HSUPA:
            case TelephonyManager.NETWORK_TYPE_HSPA:
            case TelephonyManager.NETWORK_TYPE_EVDO_B:
            case TelephonyManager.NETWORK_TYPE_EHRPD:
            case TelephonyManager.NETWORK_TYPE_HSPAP:
                return NETWORK_CLASS_3_G;
            case TelephonyManager.NETWORK_TYPE_LTE:
                return NETWORK_CLASS_4_G;
            default:
                return NETWORK_CLASS_UNKNOWN;
        }
    }
	
	private void setSimState(){
		switch (SysServices.getSimState(getContext())) {
			case TelephonyManager.SIM_STATE_READY:
				changeSimState(SIM_STATE_READY);
				break;
			case TelephonyManager.SIM_STATE_ABSENT:
				changeSimState(SIM_STATE_NO);
				break;
			case TelephonyManager.SIM_STATE_UNKNOWN:
			case TelephonyManager.SIM_STATE_NETWORK_LOCKED:
			case TelephonyManager.SIM_STATE_PIN_REQUIRED:
			case TelephonyManager.SIM_STATE_PUK_REQUIRED:
			default:
				changeSimState(SIM_STATE_UNREADY);
				break;
		}
	}
	
	private void changeSimState(int state){
		simState = state;
		switch (state) {
			case SIM_STATE_AIRMODE:
				mIcon.setImageResource(R.drawable.statu_icon_sim_no);
				break;
			case SIM_STATE_NO:
				mIcon.setImageResource(R.drawable.statu_icon_sim_no);
				break;
			case SIM_STATE_UNREADY:
				mIcon.setImageResource(R.drawable.statu_icon_sim_0);
				break;
			default:
				break;
		}
	}
	
	private void changeSimSignal(int signal){
		changeSimSignal(NETWORK_CLASS_3_G == getNetworkClass(ConnectivityManager.TYPE_MOBILE), signal);
	}
	private void changeSimSignal(boolean is3G, int signal){

		if(simState != SIM_STATE_READY){
			return;
		}
		switch(signal){
			case SIM_SIGNAL_3:
				mIcon.setImageResource(is3G ?R.drawable.statu_icon_sim3_3 : R.drawable.statu_icon_sim2_3);
				break;
			case SIM_SIGNAL_2:
				mIcon.setImageResource(is3G ?R.drawable.statu_icon_sim3_2 : R.drawable.statu_icon_sim2_2);
				break;
			case SIM_SIGNAL_1:
				mIcon.setImageResource(is3G ?R.drawable.statu_icon_sim3_1 : R.drawable.statu_icon_sim2_1);
				break;
			case SIM_SIGNAL_0:
			default:
				mIcon.setImageResource(is3G ?R.drawable.statu_icon_sim3_0 : R.drawable.statu_icon_sim2_0);
				break;
		}
	
	}
	
	private class SimReceiver extends AbsBroadcastReceiver{
		
		private static final String SIM_STATE_ACTION = "android.intent.action.SIM_STATE_CHANGED";

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			if(Intent.ACTION_AIRPLANE_MODE_CHANGED.equals(intent.getAction())){
				if(intent.getBooleanExtra("state", false)){
					changeSimState(SIM_STATE_AIRMODE);
				}
				return;
			}
			
			if(SIM_STATE_ACTION.equals(intent.getAction())){
				setSimState();
				return;
			}
		}
		@Override
		public IntentFilter getIntentFilter() {
			// TODO Auto-generated method stub
			IntentFilter filter = new IntentFilter(Intent.ACTION_AIRPLANE_MODE_CHANGED);
			filter.addAction(SIM_STATE_ACTION);
			return filter;
		}
		
	}
	
	private class SimSignalListener extends PhoneStateListener{
		
		public void startSignalListen(){
			SysServices.getTlMgr(getContext()).listen(this, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);
		}
		
		public void stopSignalListen(){
			SysServices.getTlMgr(getContext()).listen(this, 0);
		}

		@Override
		public void onSignalStrengthsChanged(SignalStrength signalStrength) {
			// TODO Auto-generated method stub
			super.onSignalStrengthsChanged(signalStrength);
			changeSimSignal(signalStrength.isGsm()?getGsmLevel(signalStrength) : getLevel(signalStrength));
		}
		
		private int getGsmLevel(SignalStrength signalStrength) {
	        int level;

	        // ASU ranges from 0 to 31 - TS 27.007 Sec 8.5
	        // asu = 0 (-113dB or less) is very weak
	        // signal, its better to show 0 bars to the user in such cases.
	        // asu = 99 is a special case, where the signal strength is unknown.
	        int asu = signalStrength.getGsmSignalStrength();
	        if (asu <= 2 || asu == 99) level = SIM_SIGNAL_0;
	        else if (asu >= 12) level = SIM_SIGNAL_3;
	        else if (asu >= 6)  level = SIM_SIGNAL_2;
	        else level = SIM_SIGNAL_1;
	        return level;
	    }
		
		private int getLevel(SignalStrength signalStrength) {
	        int level;

            int cdmaLevel = getCdmaLevel(signalStrength);
            int evdoLevel = getEvdoLevel(signalStrength);
            if (evdoLevel == SIM_SIGNAL_0) {
                /* We don't know evdo, use cdma */
                level = cdmaLevel;
            } else if (cdmaLevel == SIM_SIGNAL_0) {
                /* We don't know cdma, use evdo */
                level = evdoLevel;
            } else {
                /* We know both, use the lowest level */
                level = cdmaLevel < evdoLevel ? cdmaLevel : evdoLevel;
            }

	        return level;
	    }
		
		private int getCdmaLevel(SignalStrength signalStrength) {
	        final int cdmaDbm = signalStrength.getCdmaDbm();
	        final int cdmaEcio = signalStrength.getCdmaEcio();
	        int levelDbm;
	        int levelEcio;

	        if (cdmaDbm >= -75) levelDbm = SIM_SIGNAL_3;
	        else if (cdmaDbm >= -90) levelDbm = SIM_SIGNAL_2;
	        else if (cdmaDbm >= -100) levelDbm = SIM_SIGNAL_1;
	        else levelDbm = SIM_SIGNAL_0;

	        // Ec/Io are in dB*10
	        if (cdmaEcio >= -90) levelEcio = SIM_SIGNAL_3;
	        else if (cdmaEcio >= -120) levelEcio = SIM_SIGNAL_2;
	        else if (cdmaEcio >= -150) levelEcio = SIM_SIGNAL_1;
	        else levelEcio = SIM_SIGNAL_0;

	        int level = (levelDbm < levelEcio) ? levelDbm : levelEcio;
	        return level;
	    }
		
		private int getEvdoLevel(SignalStrength signalStrength) {
	        int evdoDbm = signalStrength.getEvdoDbm();
	        int evdoSnr = signalStrength.getEvdoSnr();
	        int levelEvdoDbm;
	        int levelEvdoSnr;

	        if (evdoDbm >= -65) levelEvdoDbm = SIM_SIGNAL_3;
	        else if (evdoDbm >= -85) levelEvdoDbm = SIM_SIGNAL_2;
	        else if (evdoDbm >= -105) levelEvdoDbm = SIM_SIGNAL_1;
	        else levelEvdoDbm = SIM_SIGNAL_0;

	        if (evdoSnr >= 7) levelEvdoSnr = SIM_SIGNAL_3;
	        else if (evdoSnr >= 4) levelEvdoSnr = SIM_SIGNAL_2;
	        else if (evdoSnr >= 1) levelEvdoSnr = SIM_SIGNAL_1;
	        else levelEvdoSnr = SIM_SIGNAL_0;

	        int level = (levelEvdoDbm < levelEvdoSnr) ? levelEvdoDbm : levelEvdoSnr;
	        return level;
	    }
		
	}
}

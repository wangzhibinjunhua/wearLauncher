package com.sczn.wearlauncher.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;

import com.sczn.wearlauncher.LauncherApp;
import com.sczn.wearlauncher.R;
import com.sczn.wearlauncher.receivers.AbsBroadcastReceiver;

public class NetWorkUtil {
	public static final int SIM_STATE_UNVALUABLE = 0;
	public static final int SIM_STATE_VALUABLE = 1;
	public static final int SIM_STATE_UNKNOW = 2;
	
	public static final int SIM_SIGNAL_0 = 0;
	public static final int SIM_SIGNAL_1 = 1;
	public static final int SIM_SIGNAL_2 = 2;
	public static final int SIM_SIGNAL_3 = 3;
	
	public static final int NETWORK_CLASS_UNKNOWN = 0;
    public static final int NETWORK_CLASS_2_G = 1;
    public static final int NETWORK_CLASS_3_G = 2;
    public static final int NETWORK_CLASS_4_G = 3;
    
    public static NetWorkUtil getInstanse(){
    	return NetWorkUtilHold.mInstanse;
    }
    private static class NetWorkUtilHold{
    	private static final NetWorkUtil mInstanse = new NetWorkUtil();
    }
    private NetWorkUtil(){
    	mCnMgr = (ConnectivityManager) LauncherApp.appContext.getSystemService(Context.CONNECTIVITY_SERVICE);
    	mTlpMgr =  (TelephonyManager) LauncherApp.appContext.getSystemService(Context.TELEPHONY_SERVICE);
    }

	private ConnectivityManager mCnMgr;
	private TelephonyManager mTlpMgr;
	private int simState = SIM_STATE_UNVALUABLE;
	
	private void setSimState(){
		switch (SysServices.getSimState(LauncherApp.appContext)) {
			case TelephonyManager.SIM_STATE_READY:
				changeSimState(SIM_STATE_VALUABLE);
			case TelephonyManager.SIM_STATE_ABSENT:
				changeSimState(SIM_STATE_UNVALUABLE);
				break;
			case TelephonyManager.SIM_STATE_UNKNOWN:
			case TelephonyManager.SIM_STATE_NETWORK_LOCKED:
			case TelephonyManager.SIM_STATE_PIN_REQUIRED:
			case TelephonyManager.SIM_STATE_PUK_REQUIRED:
			default:
				changeSimState(SIM_STATE_UNKNOW);
				break;
		}
	}
	
	private void changeSimState(int state){
		this.simState = state;
		
	}
	
	private void changeSimSignal(int signal){
		if(simState != SIM_STATE_VALUABLE){
			return;
		}
		
	}
	
	
	public void setMobileDataState(boolean enabled){
		try {
			final Class<?> cnMgrClass = Class.forName(mCnMgr.getClass().getSimpleName());
			final Field serviceFeild = cnMgrClass.getDeclaredField("mService");
			serviceFeild.setAccessible(true);
			final Object ICnMgr = serviceFeild.get(mCnMgr);
			final Class<?> ICnMgrClass = Class.forName(ICnMgr.getClass().getSimpleName());
			
			Method setMobileDataEnabledMethod = ICnMgrClass.getDeclaredMethod("setMobileDataEnabled", Boolean.TYPE);
			setMobileDataEnabledMethod.setAccessible(true);
			setMobileDataEnabledMethod.invoke(ICnMgr, enabled);
		} catch (Exception e) {
			// TODO: handle exception
			MxyLog.e(this, e.toString());
			e.printStackTrace();
		}
	}
	
	public  boolean getMobileDataState(Context pContext, Object[] arg){    
        try   
        {      
            final Class ownerClass = mCnMgr.getClass();    
            Class[] argsClass = null;    
            if (arg != null) {    
                argsClass = new Class[1];    
                argsClass[0] = arg.getClass();    
            }    
            Method method = ownerClass.getMethod("getMobileDataEnabled", argsClass);    
            Boolean isOpen = (Boolean) method.invoke(mCnMgr, arg);    
            return isOpen;    
        } catch (Exception e) {    
            return false;    
        }    
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
	
	private class SimReceiver extends AbsBroadcastReceiver{
		
		private static final String SIM_STATE_ACTION = "android.intent.action.SIM_STATE_CHANGED";

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			if(Intent.ACTION_AIRPLANE_MODE_CHANGED.equals(intent.getAction())){
				if(intent.getBooleanExtra("state", false)){
					changeSimState(SIM_STATE_UNVALUABLE);
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
			SysServices.getTlMgr(LauncherApp.appContext).listen(this, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);
		}
		
		public void stopSignalListen(){
			SysServices.getTlMgr(LauncherApp.appContext).listen(this, 0);
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

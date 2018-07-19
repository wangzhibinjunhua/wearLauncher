package com.sczn.wearlauncher.view.status;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import com.sczn.wearlauncher.LauncherApp;
import com.sczn.wearlauncher.R;
import com.sczn.wearlauncher.services.SosService;
import com.sczn.wearlauncher.util.MxyLog;
import com.sczn.wearlauncher.util.MxyToast;
import com.sczn.wearlauncher.util.SysServices;
import com.sczn.wearlauncher.view.ClickIcon;

import android.content.Context;
import android.net.ConnectivityManager;
import android.telephony.TelephonyManager;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;

public class DataIcon extends ClickIcon{

	private ConnectivityManager mCnMgr;
	private Context mContext;
	
	public DataIcon(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		mContext = getContext();
		mCnMgr = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
		
		initView();
	}

	private void initView() {
		// TODO Auto-generated method stub
		setOnClickListener(onDataIcon);
		
	}
	
	@Override
	public void startFresh() {
		// TODO Auto-generated method stub
		super.startFresh();
		freshState();
	}

	
	private void freshState() {
		// TODO Auto-generated method stub
		if(getMobileDataState(mContext, null)){
			setImageResource(R.drawable.statu_icon_data_off);
		}else{
			setImageResource(R.drawable.statu_icon_data_on);
		}
	}


	private OnClickListener onDataIcon = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if(!canOpen()){
				MxyToast.showShort(mContext, R.string.sim_unvaluable);
				return;
			}
			if(getMobileDataState(mContext, null)){
				setMobileDataState(false);
			}else{
				setMobileDataState(true);
			}
		}
	};
	
	private boolean canOpen(){
		switch (SysServices.getSimState(LauncherApp.appContext)) {
			case TelephonyManager.SIM_STATE_READY:
				return true;
			case TelephonyManager.SIM_STATE_ABSENT:
			case TelephonyManager.SIM_STATE_UNKNOWN:
			case TelephonyManager.SIM_STATE_NETWORK_LOCKED:
			case TelephonyManager.SIM_STATE_PIN_REQUIRED:
			case TelephonyManager.SIM_STATE_PUK_REQUIRED:
			default:
				return false;
		}
	}
	
	private void setMobileDataState(boolean enabled){
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
	
	private  boolean getMobileDataState(Context pContext, Object[] arg){    
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
}

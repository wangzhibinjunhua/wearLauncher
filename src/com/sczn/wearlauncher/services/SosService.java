package com.sczn.wearlauncher.services;

import java.lang.ref.WeakReference;
import java.lang.reflect.Method;
import java.util.ArrayList;

import com.android.internal.telephony.ITelephony;
import com.sczn.wearlauncher.R;
import com.sczn.wearlauncher.receivers.AbsBroadcastReceiver;
import com.sczn.wearlauncher.receivers.SosReceiver;
import com.sczn.wearlauncher.util.MxyLog;
import com.sczn.wearlauncher.util.MxyToast;
import com.sczn.wearlauncher.util.SharedPreferencesUtils;
import com.sczn.wearlauncher.util.SysServices;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

public class SosService extends Service {
	private static final String TAG = SosService.class.getSimpleName();
	
	private static final int SOS_CALL_WAITING_DURATION = 30*1000;
	public static final String MAIN_ACTION = "com.sczn.action.service.SosService";
	
	private static final int SOS_IDLE = -1;
	private static final int SOS_NUM_1= 0;
	private static final int SOS_NUM_2 = SOS_NUM_1 + 1;
	private static final int SOS_NUM_3 = SOS_NUM_2 + 1;
	
	private SosCallReceiver mSosCallReceiver;
	private SosStateListenr mSosStateListenr;
	private SosHandle mSosHandle;
	private int currSosNumber = SOS_IDLE;
	private ArrayList<String> sosNumbers;
	
	public static void startService(Context context){
		Intent i = new Intent(MAIN_ACTION);
		context.startService(i);
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		//MxyLog.d(TAG, "onStartCommand--startId=" + startId);
		initSosNumber();
		if(sosNumbers.size() == 0){
			MxyToast.showShort(this, R.string.sos_no_number);
		}
		if(SOS_IDLE != currSosNumber){
			currSosNumber = SOS_NUM_1;
			return super.onStartCommand(intent, flags, startId);
		}
		currSosNumber = SOS_NUM_1;
		endCall();
		doSosCall();
		return super.onStartCommand(intent, flags, startId);
	}
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		MxyLog.d(TAG, "onCreate");
		mSosHandle = new SosHandle(this);
		mSosCallReceiver = new SosCallReceiver();
		mSosStateListenr = new SosStateListenr();
		mSosCallReceiver.register(this);
		mSosStateListenr.startStateListen(this);
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		MxyLog.d(TAG, "onDestroy");
		mSosCallReceiver.unRegister(this);
		mSosStateListenr.stopStateListen(this);
		super.onDestroy();
	}
	
	private synchronized void initSosNumber(){
		if(sosNumbers == null){
			sosNumbers = new ArrayList<String>();
		}
		sosNumbers.clear();
		final String firstNumber = (String) SharedPreferencesUtils.getParam(this, SosReceiver.SOS_NUM_KEY_ONE, "");
		final String secondNumber = (String) SharedPreferencesUtils.getParam(this, SosReceiver.SOS_NUM_KEY_TOW, "");
		final String thirdNumber = (String) SharedPreferencesUtils.getParam(this, SosReceiver.SOS_NUM_KEY_THREE, "");
		if(!"".equals(firstNumber)){
			sosNumbers.add(firstNumber);
		}
		if(!"".equals(secondNumber)){
			sosNumbers.add(secondNumber);
		}
		if(!"".equals(thirdNumber)){
			sosNumbers.add(thirdNumber);
		}
	}
	
	private void endCall(){
		final TelephonyManager tm = (TelephonyManager)getSystemService(Service.TELEPHONY_SERVICE);
		if(tm == null){
			return;
		}
		Class<TelephonyManager> c = TelephonyManager.class;
        Method getITelephoneMethod = null;
        try{
            getITelephoneMethod = c.getDeclaredMethod("getITelephony",(Class[]) null);
            getITelephoneMethod.setAccessible(true);
        }catch (Exception e){
            e.printStackTrace();
        }

        try {
            final ITelephony iTelephony= (ITelephony) getITelephoneMethod.invoke(tm,(Object[])null);
            iTelephony.endCall();
        }catch (Exception e) {
            e.printStackTrace();
            MxyLog.e(TAG, e.toString());
        }
    }
	
	private void doSosCall(){
		if(currSosNumber < 0 || currSosNumber >= sosNumbers.size()){
			currSosNumber = SOS_IDLE;
			stopSelf();
			return;
		}
		String number = sosNumbers.get(currSosNumber);
		currSosNumber ++;
		
		if("null".equals(number)){
			if(currSosNumber < 0 || currSosNumber >= sosNumbers.size()){
				currSosNumber = SOS_IDLE;
				stopSelf();
				return;
			}
			number = sosNumbers.get(currSosNumber);
			currSosNumber ++;
		}
		
		if("null".equals(number)){
			if(currSosNumber < 0 || currSosNumber >= sosNumbers.size()){
				currSosNumber = SOS_IDLE;
				stopSelf();
				return;
			}
			number = sosNumbers.get(currSosNumber);
			currSosNumber ++;
		}
		
		if("null".equals(number)){
			if(currSosNumber < 0 || currSosNumber >= sosNumbers.size()){
				currSosNumber = SOS_IDLE;
				stopSelf();
				return;
			}
			number = sosNumbers.get(currSosNumber);
			currSosNumber ++;
		}
		
		if("null".equals(number)){
			stopSelf();
			return;
		}
		
		
		Intent i = new Intent(Intent.ACTION_CALL);
		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		i.setData(Uri.parse("tel:"+number));
		
		mSosHandle.removeMessages(SosHandle.MSG_END_CALL);
		mSosHandle.sendEmptyMessageDelayed(SosHandle.MSG_END_CALL, SOS_CALL_WAITING_DURATION);
		startActivity(i);
	}
	
	private class SosCallReceiver extends AbsBroadcastReceiver{
		private final static String ACTION_HANG_UP = "cn.kct.action.broadcast.call.disconneced";
		private final static String ACTION_HANG_ON = "cn.kct.action.broadcast";
		private final static String ACTION_HANG_DESTROY = "cn.kct.action.broadcast.call.destroy";
		private final static String EXTRA_HANG_ON_CALL = "call";
		private final static String HANG_ON_CALL_OUTGOING = "outingcall -- incall";
		private final static String HANG_ON_CALL= "state--incall";

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			MxyLog.d(TAG, "intent.getAction()" + intent.getAction());
			if(Intent.ACTION_NEW_OUTGOING_CALL.equals(intent.getAction())){
				final String number = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
				MxyLog.i(TAG, "ACTION_NEW_OUTGOING_CALL--number=" + number);
				
				return;
			}
			if(ACTION_HANG_ON.equals(intent.getAction())){
				currSosNumber = SOS_IDLE;
				mSosHandle.removeMessages(SosHandle.MSG_END_CALL);
			}
			if(ACTION_HANG_UP.equals(intent.getAction())){
				//MxyLog.d(TAG, "ACTION_HANG_UP" + intent.getStringExtra(EXTRA_HANG_ON_CALL));
				return;
			}
			if(ACTION_HANG_DESTROY.equals(intent.getAction())){
				doSosCall();
			}
		}
		@Override
		public IntentFilter getIntentFilter() {
			// TODO Auto-generated method stub
			IntentFilter filter = new IntentFilter();
			filter.addAction(Intent.ACTION_NEW_OUTGOING_CALL);
			filter.addAction(ACTION_HANG_ON);
			filter.addAction(ACTION_HANG_UP);
			filter.addAction(ACTION_HANG_DESTROY);
			return filter;
		}
		
	}
	
	private class SosStateListenr extends PhoneStateListener{
		public void startStateListen(Context context){
			SysServices.getTlMgr(context).listen(this, PhoneStateListener.LISTEN_CALL_STATE);
		}
		
		public void stopStateListen(Context context){
			SysServices.getTlMgr(context).listen(this, 0);
		}

		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
			// TODO Auto-generated method stub
			super.onCallStateChanged(state, incomingNumber);
			MxyLog.d(TAG, "onCallStateChanged" + "--state=" + state + "--incomingNumber=" + incomingNumber);
		}
		
		
	}

	private static class SosHandle extends Handler{
		private final static int MSG_END_CALL = 0;
		private static WeakReference<SosService> mSosServices;
		public SosHandle(SosService context){
			this.mSosServices = new WeakReference<SosService>(context);
		}
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			final SosService service = mSosServices.get();
			if(service == null){
				return;
			}
			switch (msg.what) {
			case MSG_END_CALL:
				//service.doSosCall();
				service.endCall();
				break;

			default:
				break;
			}
		}
		
		
	}
}

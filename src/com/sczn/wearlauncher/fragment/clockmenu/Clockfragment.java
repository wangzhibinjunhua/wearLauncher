package com.sczn.wearlauncher.fragment.clockmenu;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import com.sczn.wearlauncher.R;
import com.sczn.wearlauncher.activity.ClockSkinChooseActivity;
import com.sczn.wearlauncher.activity.WearLauncherActivity;
import com.sczn.wearlauncher.activity.NotificationActivity;
import com.sczn.wearlauncher.fragment.absFragment;
import com.sczn.wearlauncher.model.ClockSkin;
import com.sczn.wearlauncher.model.ClockSkinParse;
import com.sczn.wearlauncher.receivers.AbsBroadcastReceiver;
import com.sczn.wearlauncher.util.MxyLog;
import com.sczn.wearlauncher.util.SharedPreferencesUtils;
import com.sczn.wearlauncher.view.menu.ClockView;


public class Clockfragment extends absFragment implements OnLongClickListener,OnClickListener{
	
	private final static String TAG = "Clockfragment";
	
	public static final String FRAGMENT_TAG_NOTIFICATION = "fragment_notification";
	public static final int REQUEST_CODE_CHOOSE_CLOCK = 1;
	public static final String RESULT_EXTRA_CLOCK_INDEX = "clock_index";
	
	public static final String ARG_CLOCK_SCALE = "clock_scale";
	public static final String ARG_CLOCK_TYPE = "clock_type";
	public static final int CLOCK_SCALE_DEFAULT = 400;
	public static final int INVALUED_INDEX = -1;

	public static Clockfragment newInstance(int clockSize, boolean isMainClock){	

		Clockfragment fragment = new Clockfragment();
		Bundle bdl = new Bundle();
		bdl.putInt(ARG_CLOCK_SCALE, clockSize);
		bdl.putBoolean(ARG_CLOCK_TYPE, isMainClock);
		fragment.setArguments(bdl);
		return fragment;
	}	

	private ClockSkinLoader mClockSkinLoader;
	private ClockSkinParse mClockSkinParse;
	private ClockView mClockView;
	private int mClockSize = CLOCK_SCALE_DEFAULT;
	
	private int mClockIndex = INVALUED_INDEX;
	private boolean isMainClock = true;
	
	private ClockChangeReceiver mClockChangeReceiver;
	protected WearLauncherActivity mWearLauncherActivity;
	
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		if(activity instanceof WearLauncherActivity){
			mWearLauncherActivity = (WearLauncherActivity) activity;
		}
	}
	
	@Override
	public void onDetach() {
		// TODO Auto-generated method stub
		mWearLauncherActivity = null;
		super.onDetach();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Bundle bdl = getArguments();
		if(bdl != null){
			mClockSize = bdl.getInt(ARG_CLOCK_SCALE, CLOCK_SCALE_DEFAULT);
			isMainClock = bdl.getBoolean(ARG_CLOCK_TYPE, true);
		}
		if(CLOCK_SCALE_DEFAULT != mClockSize){
			mClockSkinParse = new ClockSkinParse(mClockSize, mClockSize);
		}else{
			mClockSkinParse = new ClockSkinParse();
		}
		
		mClockSkinLoader = new ClockSkinLoader();
		mClockChangeReceiver = new ClockChangeReceiver();
		mClockChangeReceiver.register(getActivity());
	}
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		mClockChangeReceiver.unRegister(getActivity());
		super.onDestroy();
	}
	
	@Override
	protected int getLayoutResouceId() {
		// TODO Auto-generated method stub
		return R.layout.fragment_clockmenu_clock;
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		mClockView = findViewById(R.id.cloct_main);
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);
		startGetClockSkin(SharedPreferencesUtils.getIntParam(mActivity,"BT_CLOCK",0));
	}
	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		stopGetClockSkin();
		super.onDestroyView();
	}
	@Override
	protected void initData() {
		// TODO Auto-generated method stub
		if(isMainClock){
			//mClockView.setOnClickListener(this);
			mClockView.setOnLongClickListener(this);
		}
	}
	
	private void ensureClock(){
		final int index = SharedPreferencesUtils.getIntParam(mActivity,"BT_CLOCK",0);
		MxyLog.d(TAG, "ensureClock" + "--index=" + index + "--mClockIndex=" + mClockIndex);
		if(index != mClockIndex){
			startGetClockSkin(index);
			mClockIndex = index;
		}
	}
	@Override
	protected void startFreshData() {
		// TODO Auto-generated method stub
		mClockView.startDraw();
		ensureClock();
		if(isMainClock && mWearLauncherActivity != null){
			mWearLauncherActivity.setInMainClock(true);
		}
	}

	@Override
	protected void endFreshData() {
		// TODO Auto-generated method stub
		if(isMainClock && mWearLauncherActivity != null){
			mWearLauncherActivity.setInMainClock(false);
		}
		mClockView.stopDraw();
	}
	
	private void gotoNotification() {
		// TODO Auto-generated method stub
		Intent i= new Intent(getActivity().getApplicationContext(), NotificationActivity.class);
		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(i);
		
	}
	private void gotoChangeClock() {
		// TODO Auto-generated method stub
		 Intent intent = new Intent(getActivity(), ClockSkinChooseActivity.class);
         startActivityForResult(intent, REQUEST_CODE_CHOOSE_CLOCK);
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
			case R.id.cloct_main:
				gotoNotification();
				break;
	
			default:
				break;
		}
	}
	
	@Override
	public boolean onLongClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
			case R.id.cloct_main:
				gotoChangeClock();
				return true;
	
			default:
				break;
		}
		return false;
	}
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
			case REQUEST_CODE_CHOOSE_CLOCK:
				if(ClockSkinChooseActivity.RESULT_CODE_CLOCK_CHOOSE == resultCode){
					final int index = data.getIntExtra(
							ClockSkinChooseActivity.RESULT_EXTRA_CLOCK_INDEX, 0);
					//startGetClockSkin(index);
				} 
				break;
	
			default:
				break;
		}
	}
	
	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		// TODO Auto-generated method stub
		//MxyLog.i(TAG, "setUserVisibleHint" + isVisibleToUser);
		super.setUserVisibleHint(isVisibleToUser);
	}
	private void setClockSkin(ClockSkin clockSkin){
		if(mClockView == null){
			return;
		}
		
		if(clockSkin == null){
			MxyLog.d(this, "clockSkin == null");
			startGetClockSkin(0);
		}else{
			MxyLog.d(this, "clockSkin =" + clockSkin.toString());
			mClockView.setClockSkin(clockSkin);
		}
	}
	
	private void startGetClockSkin(int position){
		MxyLog.i(TAG, "startGetClockSkin--position=" + position);
		if(mClockSkinLoader != null){
			final ClockSkinLoader oldTask = mClockSkinLoader;
			oldTask.cancel(true);
			mClockSkinLoader = null;
		}
		mClockSkinLoader = new ClockSkinLoader();
		mClockSkinLoader.execute(mClockSize, position);
	
	}
	private void stopGetClockSkin(){

		if(mClockSkinLoader != null){
			final ClockSkinLoader oldTask = mClockSkinLoader;
			oldTask.cancel(true);
			mClockSkinLoader = null;
		}
	
	}

	private class ClockSkinLoader extends AsyncTask<Integer, Void, ClockSkin>{
		
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}

		@Override
		protected ClockSkin doInBackground(Integer... params) {
			// TODO Auto-generated method stub
			ClockSkinParse parser = new ClockSkinParse(params[0], params[0]);
			ClockSkin clockSkin = null;
			try{
	            clockSkin = parser.getChildSkinByPosition(getActivity(), params[1]);
	            if(!isCancelled()){
	            	mClockIndex = params[1];
	            }
	        }catch (Exception e){
	        	MxyLog.e(TAG, "ClockSkinLoader--e=" + e.toString());
	            if(!isCancelled()){
	            	//mClockIndex = 0;
	            	//clockSkin = parser.getChildSkinByPosition(getActivity(), 0);
	            	return null;
	            }
	        }
			return clockSkin;
		}

		@Override
		protected void onPostExecute(ClockSkin result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if(!isCancelled()){
				setClockSkin(result);
			}
		}
	}
	
	private class ClockChangeReceiver extends AbsBroadcastReceiver{
		private final String ACTION_BDCAST_WATCH_CHANGED = "com.sczn.broadcast.watchchanged";
		private final String WATCH_DIAL_CHANGED_EXTRO = "dial";
	    private final String ACTION_BDCAST_CALLINGSTART = "com.sczn.broadcast.phonecallstart";
	    private final String ACTION_BDCAST_CALLINGEND="com.sczn.broadcast.phonecallend";

		@Override
		public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(action.equals(ACTION_BDCAST_WATCH_CHANGED)){
                String s = intent.getStringExtra(WATCH_DIAL_CHANGED_EXTRO);
                if (s == null || s.equals("") || s.equals("null")) {
                   return;
                }
                SharedPreferencesUtils.setParam(context, "BT_CLOCK", Integer.valueOf(s.trim()));
                ensureClock();
                return;
            }
            if(action.equals(ACTION_BDCAST_CALLINGSTART)){
                getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                return;
            }
            if(action.equals(ACTION_BDCAST_CALLINGEND)){
                getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                return;
            }
            if(action.equals(Intent.ACTION_SCREEN_ON)){
                if(isMainClock && mClockView != null){
                	mClockView.startDraw();
                }
                return;
            }
        }
		@Override
		public IntentFilter getIntentFilter() {
			// TODO Auto-generated method stub
			IntentFilter filter = new IntentFilter();
			filter.addAction(ACTION_BDCAST_WATCH_CHANGED);
			filter.addAction(ACTION_BDCAST_CALLINGSTART);
			filter.addAction(ACTION_BDCAST_CALLINGEND);
			
			filter.addAction(Intent.ACTION_SCREEN_ON);
			return filter;
		}
		
	}
}

package com.sczn.wearlauncher.fragment;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.service.notification.StatusBarNotification;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.sczn.wearlauncher.R;
import com.sczn.wearlauncher.adapter.NotificationMainAdapter;
import com.sczn.wearlauncher.adapter.NotificationMainAdapter.INotificationItemClick;
import com.sczn.wearlauncher.fragment.NotificationDetailFragment.ICloseDetail;
import com.sczn.wearlauncher.fragment.clockmenu.MenuChildrenFragment;
import com.sczn.wearlauncher.fragment.clockmenu.MenuContainFragment;
import com.sczn.wearlauncher.model.PhoneMessage;
import com.sczn.wearlauncher.util.MxyLog;
import com.sczn.wearlauncher.util.NotificationUtil;
import com.sczn.wearlauncher.util.SysServices;
import com.sczn.wearlauncher.util.NotificationUtil.INotificationUpdate;
import com.sczn.wearlauncher.util.NotificationUtil.MyNotification;
import com.sczn.wearlauncher.util.NotificationUtil.PhonePkgNotification;
import com.sczn.wearlauncher.view.MyRecyclerView;

public class NotificationMainFragment extends absFragment implements INotificationUpdate,INotificationItemClick
							,ICloseDetail{
	private static final String TAG = NotificationMainFragment.class.getSimpleName();
	
	public static NotificationMainFragment newInstance(boolean isApp){
		NotificationMainFragment fragment = new NotificationMainFragment();
		return fragment;
	}
	
	private static final String FRAGMENT_TAG_DETAIL = "fragment_tag_notification_detail";
	
	private TextView mNotificationClear;
	private MyRecyclerView mNotificationRv;
	private NotificationMainAdapter mAdapter;
	private NotificationUtil mNotificationUtil;
	
	private NotificationHandler mNotificationHandler;
	private boolean needFresh;
	private boolean canFresh;
	
	private NotificationDetailFragment mNotificationDetailFragment;
	
	private boolean isEmptyed = false;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mNotificationUtil = NotificationUtil.getInstance();
	}
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		mNotificationUtil = null;
		super.onDestroy();
	}

	@Override
	protected int getLayoutResouceId() {
		// TODO Auto-generated method stub
		return R.layout.fragment_notification_main;
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		mNotificationClear = findViewById(R.id.notification_clear);
		mNotificationRv = findViewById(R.id.notification_main_list);
	}

	@Override
	protected void initData() {
		// TODO Auto-generated method stub
		mAdapter = new NotificationMainAdapter(getActivity(),
				mNotificationUtil.getNotificationList(),this);
		mNotificationRv.setLayoutManager(new LinearLayoutManager(getActivity()));
		mNotificationRv.setAdapter(mAdapter);
		mNotificationRv.setEmpty(findViewById(R.id.notification_empty));
		mNotificationClear.setOnClickListener(onClearClick);
		needFresh = true;
	}
	
	

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);
		mNotificationHandler = new NotificationHandler(this);
		mNotificationUtil.setListen(this);
	}

	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		mNotificationUtil.removeListen(this);
		mNotificationHandler.removeCallbacksAndMessages(null);
		mNotificationHandler = null;
		super.onDestroyView();
	}
	
	@Override
	protected void startFreshData() {
		// TODO Auto-generated method stub
		super.startFreshData();
		canFresh = true;
		if(needFresh){
			changeClearState(false);
			freshData();
			needFresh = false;
		}
	}
	
	@Override
	protected void endFreshData() {
		// TODO Auto-generated method stub
		super.endFreshData();
		canFresh = false;
	}

	private void freshData(){
		MxyLog.d(TAG, "freshData" + NotificationUtil.getInstance().getNotificationList().size());
		if(mAdapter != null){
			
			mAdapter.notifyDataSetChanged();
		}
	}
	
	private void notificationAction(){
		if(getActivity() == null){
			return;
		}
		Vibrator vibrator = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
        NotificationManager notificationManager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = new Notification();
        notification.defaults = Notification.DEFAULT_SOUND;
        
        final int modeState = SysServices.getRingMode(getActivity());
		switch (modeState) {
			case AudioManager.RINGER_MODE_SILENT:
				break;
			case AudioManager.RINGER_MODE_VIBRATE:
				vibrator.vibrate(1000);
				break;
			case AudioManager.RINGER_MODE_NORMAL:
			default:
				notificationManager.notify(3,notification);
				break;
		}
	}
	
	@Override
	public void removePosition(int position) {
		// TODO Auto-generated method stub
		MxyLog.d(TAG, "removePosition" + "position=" + position + "--canFresh=" + canFresh);
		if(!canFresh){
			needFresh = true;
			return;
		}
		Message message = Message.obtain();
		message.what = NotificationHandler.MSG_REMOVE;
		message.arg1 = position;
		setMessage(message);
		//adapterNotifyRemoved(position);
	}

	@Override
	public void addNew(int position) {
		// TODO Auto-generated method stub
		notificationAction();
		if(!canFresh){
			needFresh = true;
			return;
		}
		Message message = Message.obtain();
		message.what = NotificationHandler.MSG_ADD;
		message.arg1 = position;
		setMessage(message);
	}

	@Override
	public void addNewDetail(String pkgName, int position) {
		// TODO Auto-generated method stub
		notificationAction();
		Message message = Message.obtain();
		message.what = NotificationHandler.MSG_ADD_DETAIL;
		message.arg1 = position;
		message.obj = pkgName;
		setMessage(message);
	}

	@Override
	public void freshAll() {
		// TODO Auto-generated method stub
		if(!canFresh){
			needFresh = true;
			return;
		}
		Message message = Message.obtain();
		message.what = NotificationHandler.MSG_FRESH_ALL;
		setMessage(message);
	}
	
	private void adapterNotifyAdd(int position){
		if(mAdapter == null){
			return;
		}
		mAdapter.notifyItemInserted(position);
		changeClearState(false);
	}
	private void adapterNotifyRemoved(int position){
		if(mAdapter == null || (position > mAdapter.getItemCount())){
			return;
		}
		mAdapter.notifyItemRemoved(position);
	}
	
	private void setMessage(Message message){
		if(mNotificationHandler != null){
			mNotificationHandler.sendMessage(message);
		}
	}
	
	private void onWatchNotificationClick(StatusBarNotification notification, int position){
		final String packageName = notification.getPackageName();
		final boolean isUsbPackageName = packageName.equals("android") || packageName.equals("com.android.systemui");
        if (!isUsbPackageName) {
        	mNotificationUtil.removeWatchNotification(notification);
        }
        
		PendingIntent mPendingIntent =  notification.getNotification().contentIntent;
		try {
            if(!notification.getPackageName().equals("com.android.bluetooth")) {
                if(mPendingIntent != null)
                	mPendingIntent.send();
            }

        } catch (PendingIntent.CanceledException e) {
            e.printStackTrace();
        }

	}
	
	private void onPhoneNotificationClick(PhonePkgNotification notifications, int position){
		showDetailFragment(notifications);
	}
	
	@Override
	public void onNotificationClick(View view,int position) {
		// TODO Auto-generated method stub
		if(view.getTag() == null || !(view.getTag() instanceof MyNotification)){
			return;
		}
		final MyNotification notification = (MyNotification) view.getTag();
		
		//if(!notification.equals(mNotificationUtil.getNotificationList().get(position))){
		//	return;
		//}
		
		if(notification.isWatch()){
			onWatchNotificationClick(notification.getWatchNotification(), position);
		}else{
			onPhoneNotificationClick(notification.getPhoneNotifications(), position);
		}
	}

	private OnClickListener  onClearClick = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if(isEmptyed){
				getActivity().onBackPressed();
			}else{
				NotificationUtil.getInstance().clearNotificationList();
				changeClearState(true);
			}
		}
	};
	
	private void changeClearState(boolean isEmpty){
		MxyLog.d(TAG, "changeClearState=" + isEmpty);
		if(isEmpty){
			mNotificationClear.setText(R.string.notification_closed);
			isEmptyed = true;
		}else{
			mNotificationClear.setText(R.string.notification_clear);
			isEmptyed = false;
		}
	}
	
	private void showDetailFragment(PhonePkgNotification notification){

		// TODO Auto-generated method stub
		if(mNotificationDetailFragment != null){
			if(mNotificationDetailFragment.isAdded()){
				mNotificationDetailFragment.dismissAllowingStateLoss();
			}
			mNotificationDetailFragment = null;
		}
		mNotificationDetailFragment = NotificationDetailFragment.newInstance(notification);
		mNotificationDetailFragment.setCloseListen(this);
		mNotificationDetailFragment.show(getChildFragmentManager(), FRAGMENT_TAG_DETAIL);
	
	}
	private void freshDetailFragment(String pkgName){
		if(mNotificationDetailFragment != null){
			if(mNotificationDetailFragment.isAdded()){
				if(pkgName.equals(mNotificationDetailFragment.getDetailPkgName())){
					mNotificationDetailFragment.freshDetail();
				}
			}
		}
	}
	
	private static class NotificationHandler extends Handler{
		

		private static final int MSG_FRESH_ALL = 0;
		private static final int MSG_REMOVE = 1;
		private static final int MSG_ADD = 2;
		private static final int MSG_ADD_DETAIL = 3;
		
		private static  WeakReference<NotificationMainFragment> mFragment;
		
		private NotificationHandler(NotificationMainFragment fragment) {  
			mFragment = new WeakReference<NotificationMainFragment>(fragment);  
		}
		
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			NotificationMainFragment fragment = mFragment.get();
			if(fragment == null){
				return;
			}
			MxyLog.d(TAG, "handleMessage --msg.what=" + msg.what);
			switch (msg.what) {
				case MSG_FRESH_ALL:
					fragment.freshData();
					break;
				case MSG_REMOVE:
					fragment.adapterNotifyRemoved(msg.arg1);	
					break;
				case MSG_ADD:
					fragment.adapterNotifyAdd(msg.arg1);
					break;
				case MSG_ADD_DETAIL:
					fragment.freshDetailFragment((String) msg.obj);
					break;
				default:
					return;
			}
		}
		
	}

	@Override
	public void closeWithClear(PhonePkgNotification notificationDetails) {
		// TODO Auto-generated method stub
		mNotificationUtil.removeNotificationDetails(notificationDetails);
		if(mNotificationDetailFragment != null){
			if(mNotificationDetailFragment.isAdded()){
				mNotificationDetailFragment.dismissAllowingStateLoss();
			}
			mNotificationDetailFragment = null;
		}
	}
}

package com.sczn.wearlauncher.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Observable;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Parcel;
import android.os.Parcelable;
import android.service.notification.StatusBarNotification;

import com.google.gson.Gson;
import com.sczn.wearlauncher.model.PhoneMessage;
import com.sczn.wearlauncher.receivers.AbsBroadcastReceiver;

public class NotificationUtil extends Observable implements Parcelable{
	private static final String TAG = NotificationUtil.class.getSimpleName();
	
	private static class Holder{
		private static NotificationUtil instance = new NotificationUtil();
	}
	public static NotificationUtil getInstance(){
		return Holder.instance;
	}
	
	public static void startMgr(Context context){
		getInstance().registerReceiver(context);
	}
	public static void stopMgr(Context context){
		getInstance().unRegisterReceiver(context);
	}

	private NotificationReceiver mNotificationReceiver;
	private ArrayList<MyNotification> mNotificationList;
	
	private INotificationUpdate mListen;
	
	private NotificationUtil(){
		mNotificationReceiver = new NotificationReceiver();
		mNotificationList = new ArrayList<MyNotification>();
	}
	
	public void setListen(INotificationUpdate listen){
		mListen = listen;
	}
	public void removeListen(INotificationUpdate listen){
		mListen = null;
	}
	
	public void registerReceiver(Context context){
		mNotificationReceiver.register(context);
	}
	public void unRegisterReceiver(Context context){
		mNotificationReceiver.unRegister(context);
	}

	public synchronized void addWatchNotification(StatusBarNotification notification){	
		MxyLog.d(TAG, "addWatchNotification--" + notification.getPackageName());
		final int id = notification.getId();
		
		final int size = mNotificationList.size();
		for(int i=0; i < size; i++){
			final MyNotification mynotification = mNotificationList.get(i);
			if(mynotification.isWatch() && id == mynotification.getWatchNotification().getId()){
				removeNotification(i);
				addNewNotification(new MyNotification(notification));
				return;
			}
		}
	
		addNewNotification(new MyNotification(notification));
	}
	
	public synchronized void removeWatchNotification(StatusBarNotification notification){

		final int id = notification.getId();

		final int size = mNotificationList.size();
		for(int i=0; i < size; i++){
			final MyNotification mynotification = mNotificationList.get(i);
			if(mynotification.isWatch() && id == mynotification.getWatchNotification().getId()){
				removeNotification(i);
				return;
			}
		}
	
	}
	
	public synchronized void removeNotificationDetails(PhonePkgNotification notificationDetails){
		final int size = mNotificationList.size();
		for(int i=0; i < size; i++){
			final MyNotification mynotification = mNotificationList.get(i);
			if(!mynotification.isWatcher && mynotification.getPhoneNotifications().equals(notificationDetails)){
				removeNotification(i);
				return;
			}
		}
	}
	
	public synchronized void addPhoneNotification(String message){
		
		PhoneMessage notification = null;
		 try {
            Gson gson = new Gson();
            notification = gson.fromJson(message, PhoneMessage.class);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }finally{
        	if(notification == null){
        		return;
        	}
        }
		final int size = mNotificationList.size();
		final String pkgName = notification.getPackageName();
		for(int i=0; i < size; i++){
			final MyNotification mynotification = mNotificationList.get(i);
			if(!mynotification.isWatch()){
				if(pkgName.equals(mynotification.getPhoneNotifications().getPkgName())){
					
					if(i == 0){
						mynotification.addPhoneNotification(notification);
						if(mListen != null){
							mListen.addNewDetail(pkgName, 0);
						}
						return;
					}

					removeNotification(i);
					mynotification.addPhoneNotification(notification);
					if(mListen != null){
						mListen.addNewDetail(pkgName, 0);
					}
					addNewNotification(mynotification);
					return;
				}
			}
		}

		addNewNotification(new MyNotification(new PhonePkgNotification(notification)));
	}
	
	private void addNewNotification(MyNotification notification){
		//MxyLog.d(TAG, "addNewNotification" + "notification=" + notification.toString());
		final int oldSize = mNotificationList.size();
		mNotificationList.add(0, notification);
		if(mListen != null){
			if(oldSize == 0){
				mListen.freshAll();
			}else{
				mListen.addNew(0);
			}
		}
		setChanged();
		notifyObservers();
	}
	private void removeNotification(int i){
		MxyLog.d(TAG, "removeNotification" + "position=" + i);
		mNotificationList.remove(i);
		if(mListen != null){
			mListen.removePosition(i);
		}
		setChanged();
		notifyObservers();
	}
	
	public ArrayList<MyNotification> getNotificationList(){
		if(mNotificationList == null){
			mNotificationList = new ArrayList<NotificationUtil.MyNotification>();
		}
		return mNotificationList;
	}
	
	public void clearNotificationList(){
		ArrayList<MyNotification> tmpList = new ArrayList<NotificationUtil.MyNotification>();
		for(MyNotification notification : mNotificationList){
			if(notification.isWatcher && !notification.getWatchNotification().isClearable()){
				tmpList.add(notification);
			}
		}
		mNotificationList.clear();
		mNotificationList.addAll(tmpList);
		if(mListen != null){
			mListen.freshAll();
		}
		setChanged();
		notifyObservers();
	}
	
	public void removeUsbNotification() {
		// TODO Auto-generated method stub
		final ArrayList<MyNotification> tepList = new ArrayList<MyNotification>(mNotificationList);
		
		
		for (Iterator iterator = tepList.iterator(); iterator.hasNext(); ) {
			final MyNotification notification = (MyNotification) iterator.next();
			if(notification.isWatcher){
				String packageName = notification.getWatchNotification().getPackageName();
				if( packageName.equals("android") || packageName.equals("com.android.systemui")){
					iterator.remove();
				}
			}
        }
		mNotificationList.clear();
		mNotificationList.addAll(tepList);
		if(mListen != null){
			mListen.freshAll();
		}
		setChanged();
		notifyObservers();
	}
	
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
	}

	private class NotificationReceiver extends AbsBroadcastReceiver{
		
		public static final String ACTION_PHONE_NOTIFICATION = "NotificTionMessage_from_telephone";
		public static final String ACTION_USB_CONNECTED = "android.hardware.usb.action.USB_STATE";
		public static final String ACTION_WATCH_NOTIFICATION = "com.sczn.s001_launcher.NOTIFICATION_LISTENER";
		public static final String EXTRA_NOTIFICATION = "s001";

		@Override
		public void register(Context context) {
			// TODO Auto-generated method stub
			context.registerReceiver(this, getIntentFilter());
		}
		
		@Override
		public void unRegister(Context context) {
			// TODO Auto-generated method stub
			context.unregisterReceiver(this);
		}
		
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			//MxyLog.d(TAG, "onReceive=" + intent.getAction());
			if(ACTION_PHONE_NOTIFICATION.equals(intent.getAction())){
				final String message = intent.getStringExtra("NotificTionMessage");
				if (message != null) {
					addPhoneNotification(message);
	            }
				return;
			}
			
			if(ACTION_WATCH_NOTIFICATION.equals(intent.getAction())){
				final StatusBarNotification notification = intent.getParcelableExtra(EXTRA_NOTIFICATION);
				if(notification != null){
					addWatchNotification(notification);
				}
				return;
			}
			
			if(ACTION_USB_CONNECTED.equals(intent.getAction())){
				final boolean connected = intent.getExtras().getBoolean("connected");
				if(!connected){
					removeUsbNotification();
				}
				return;
			}
		}
		@Override
		public IntentFilter getIntentFilter() {
			// TODO Auto-generated method stub
			IntentFilter filter = new IntentFilter();
			filter.addAction(ACTION_PHONE_NOTIFICATION);
			filter.addAction(ACTION_USB_CONNECTED);
			filter.addAction(ACTION_WATCH_NOTIFICATION);
			return filter;
		}
		
	}
	
	public class MyNotification{
		private boolean isWatcher;
		private StatusBarNotification watchNotification;

		private PhonePkgNotification phonePkgNotificastion;
		
		public MyNotification(){
			this(true, null, null);
		}
		
		public MyNotification(StatusBarNotification notification){
			this(true, notification, null);
		}

		public MyNotification(PhonePkgNotification notification){
			this(false, null, notification);
		}
		private MyNotification(boolean isWatcher, 
				StatusBarNotification watchNotification,
				PhonePkgNotification phoneNotification){
			this.isWatcher = isWatcher;
			this.watchNotification = watchNotification;
			this.phonePkgNotificastion = phoneNotification;
		}
		
		public boolean isWatch(){
			return isWatcher;
		}
		public StatusBarNotification getWatchNotification() {
			return watchNotification;
		}

		public PhonePkgNotification getPhoneNotifications(){
			return phonePkgNotificastion;
		}
		public void addPhoneNotification(PhoneMessage message){
			if(phonePkgNotificastion == null){
				phonePkgNotificastion = new PhonePkgNotification(message);
			}else{
				phonePkgNotificastion.addMessage(message);
			}
		}
		
	}
	
	public class PhonePkgNotification implements Serializable{

		private static final long serialVersionUID = 1L;
		private String AppName;
		private String pkgName;
		private ArrayList<PhoneMessage> phoneMessages;

		public PhonePkgNotification(PhoneMessage message) {
			this.pkgName = message.getPackageName();
			this.AppName = message.getAppName();
			phoneMessages = new ArrayList<PhoneMessage>();
			phoneMessages.add(0, message);
		}
		
		public void addMessage(PhoneMessage message){
			phoneMessages.add(0, message);
		}

		public ArrayList<PhoneMessage> getMessages(){
			return phoneMessages;
		}
		
		public String getPkgName() {
			return pkgName;
		}
		
		public String getAppName(){
			return AppName;
		}
	}

	public interface INotificationUpdate{
		public void removePosition(int position);
		public void addNew(int position);
		public void addNewDetail(String pkgName, int position);
		public void freshAll();
	}
}

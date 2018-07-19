package com.sczn.wearlauncher.adapter;

import java.util.ArrayList;

import com.sczn.wearlauncher.R;
import com.sczn.wearlauncher.model.PhoneMessage;
import com.sczn.wearlauncher.util.DateFormatUtil;
import com.sczn.wearlauncher.util.SysServices;
import com.sczn.wearlauncher.util.NotificationUtil.MyNotification;
import com.sczn.wearlauncher.util.NotificationUtil.PhonePkgNotification;
import com.sczn.wearlauncher.view.ScrollerTextView;

import android.content.Context;
import android.service.notification.StatusBarNotification;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class NotificationMainAdapter extends Adapter<NotificationMainAdapter.NotificaionMainHolder> {
	private static final String TAG = NotificationMainAdapter.class.getSimpleName();
	
	public static final int ITEM_TYPE_WATCH = 0;
	public static final int ITEM_TYPE_PHONE = 1;
	private Context mContext;
	private ArrayList<MyNotification> mNotificationList;
	private INotificationItemClick mINotificationItemClick;
	
	public NotificationMainAdapter(Context mContext,
			ArrayList<MyNotification> mNotificationList,
			INotificationItemClick mINotificationItemClick) {
		super();
		this.mContext = mContext;
		this.mNotificationList = mNotificationList;
		this.mINotificationItemClick = mINotificationItemClick;
	}


	@Override
	public int getItemCount() {
		// TODO Auto-generated method stub
		return mNotificationList.size();
	}
	
	@Override
	public int getItemViewType(int position) {
		// TODO Auto-generated method stub
		if(mNotificationList.get(position).isWatch()){
			return ITEM_TYPE_WATCH;
		}else{
			return ITEM_TYPE_PHONE;
		}
	}
	

	@Override
	public void onBindViewHolder(NotificaionMainHolder arg0, int arg1) {
		// TODO Auto-generated method stub
		if(arg1 > mNotificationList.size()){
			return;
		}
		MyNotification item = mNotificationList.get(arg1);
		if(item.isWatch()){
			final StatusBarNotification notification = item.getWatchNotification();
			arg0.mIcon.setImageResource(notification.getNotification().icon);
			arg0.mTime.setText(DateFormatUtil.getTimeString(DateFormatUtil.HM,
					notification.getPostTime()));
			arg0.mMessage.setText(notification.getNotification().tickerText);
		}else{
	
			final PhoneMessage message = item.getPhoneNotifications().getMessages().get(0);
			//arg0.mIcon.setImageDrawable(SysServices.getAppIcon(mContext, message.getPackageName()));
			arg0.mAppName.setText(message.getAppName());
			arg0.mTime.setText(message.getTime());
			arg0.mMessage.setText(message.getTickerText());
		}
		arg0.mMessage.setTag(item);
		arg0.mMessage.setOnClickListener(new OnItemClick(arg1));
	}

	@Override
	public NotificaionMainHolder onCreateViewHolder(ViewGroup arg0, int arg1) {
		// TODO Auto-generated method stub
		final View view = LayoutInflater.from(arg0.getContext())
				.inflate(R.layout.item_notification_main, arg0, false);
		switch (arg1) {
			case ITEM_TYPE_PHONE:
				view.findViewById(R.id.notification_main_icon).setVisibility(View.GONE);
				break;
	
			default:
				break;
		}
		return new NotificaionMainHolder(view);
	}
	
	public class NotificaionMainHolder extends ViewHolder{

		private ImageView mIcon;
		private TextView mAppName;
		private TextView mTime;
		private ScrollerTextView  mMessage;

		public NotificaionMainHolder(View arg0) {
			super(arg0);
			mIcon = (ImageView) arg0.findViewById(R.id.notification_main_icon);
			mTime = (TextView) arg0.findViewById(R.id.notification_main_time);
			mAppName = (TextView) arg0.findViewById(R.id.notification_main_app);
			mMessage = (ScrollerTextView ) arg0.findViewById(R.id.notification_main_message);
		}	
	}

	private class OnItemClick implements OnClickListener{
		
		private final int position;

		public OnItemClick(int position) {
			super();
			this.position = position;
		}

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if(mINotificationItemClick != null){
				mINotificationItemClick.onNotificationClick(v,position);
			}
		}
		
	}
	public interface INotificationItemClick{
		public void onNotificationClick(View view,int position);
	}
}

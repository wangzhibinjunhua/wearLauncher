package com.sczn.wearlauncher.adapter;

import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.sczn.wearlauncher.R;
import com.sczn.wearlauncher.model.PhoneMessage;
import com.sczn.wearlauncher.util.NotificationUtil.PhonePkgNotification;
import com.sczn.wearlauncher.view.ScrollerTextView;

public class NotificationDetailAdapter extends Adapter<NotificationDetailAdapter.NotificaionDetailHolder>{

	private PhonePkgNotification mNotificationDetail;
	public NotificationDetailAdapter(PhonePkgNotification notification){
		this.mNotificationDetail = notification;
		
	}
	@Override
	public int getItemCount() {
		// TODO Auto-generated method stub
		if(mNotificationDetail == null){
			return 0;
		}
		return mNotificationDetail.getMessages().size();
	}

	@Override
	public void onBindViewHolder(NotificaionDetailHolder arg0, int arg1) {
		// TODO Auto-generated method stub
		final PhoneMessage message = mNotificationDetail.getMessages().get(arg1);
		
		arg0.mTime.setText(message.getTime());
		arg0.mMessage.setText(message.getTickerText());
	}

	@Override
	public NotificaionDetailHolder onCreateViewHolder(ViewGroup arg0, int arg1) {
		// TODO Auto-generated method stub
		final View view = LayoutInflater.from(arg0.getContext())
				.inflate(R.layout.item_notification_detail, arg0, false);
		return new NotificaionDetailHolder(view);
	}
	
	public class NotificaionDetailHolder extends ViewHolder{

		private TextView mTime;
		private ScrollerTextView  mMessage;

		public NotificaionDetailHolder(View arg0) {
			super(arg0);
			mTime = (TextView) arg0.findViewById(R.id.notification_detail_time);
			mMessage = (ScrollerTextView ) arg0.findViewById(R.id.notification_detail_message);
		}	
	}
}

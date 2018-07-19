package com.sczn.wearlauncher.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.sczn.wearlauncher.R;
import com.sczn.wearlauncher.adapter.NotificationDetailAdapter;
import com.sczn.wearlauncher.util.NotificationUtil.PhonePkgNotification;
import com.sczn.wearlauncher.view.MyRecyclerView;
import com.sczn.wearlauncher.view.ScrollerTextView;

public class NotificationDetailFragment extends absDialogFragment implements OnClickListener{
	private static final String TAG = NotificationDetailFragment.class.getSimpleName();
	
	private static final String ARG_NOTIFICATIONS = "notification_detail";
	public static NotificationDetailFragment newInstance(PhonePkgNotification notification){
		
		final NotificationDetailFragment fragment = new NotificationDetailFragment();
		Bundle arg = new Bundle();
		arg.putSerializable(ARG_NOTIFICATIONS, notification);
		fragment.setArguments(arg);
		return fragment;
	}

	private PhonePkgNotification notificationDetails;
	
	private ScrollerTextView mPkgName;
	private MyRecyclerView mNotifications;
	private TextView mNotificationsClear;
	
	private NotificationDetailAdapter mNotificationDetailAdapter;
	public ICloseDetail mCloseDetail;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		if(getArguments() != null){
			notificationDetails = (PhonePkgNotification) getArguments().get(ARG_NOTIFICATIONS);
		}
		
		if(notificationDetails == null){
			dismissAllowingStateLoss();
		}
	}
	
	public void setCloseListen(ICloseDetail listen){
		this.mCloseDetail = listen;
	}
	
	public String getDetailPkgName(){
		return notificationDetails.getPkgName();
	}
	
	public void freshDetail(){
		if(mNotificationDetailAdapter != null){
			mNotificationDetailAdapter.notifyItemInserted(0);
		}
	}

	@Override
	protected int getLayoutResouceId() {
		// TODO Auto-generated method stub
		return R.layout.fragment_notification_detail;
	}

	@Override
	protected void creatView() {
		// TODO Auto-generated method stub
		initView();
		initData();
	}

	private void initView() {
		// TODO Auto-generated method stub
		mPkgName = findViewById(R.id.notification_detail_title);
		mNotifications = findViewById(R.id.notification_detail_list);
		mNotificationsClear = findViewById(R.id.notification_detail_clear);
	}

	private void initData() {
		// TODO Auto-generated method stub
		if(notificationDetails == null){
			return;
		}
		mPkgName.setText(notificationDetails.getAppName());
		mNotificationsClear.setOnClickListener(this);
		mNotificationDetailAdapter = new NotificationDetailAdapter(notificationDetails);
		mNotifications.setLayoutManager(new LinearLayoutManager(getActivity()));
		mNotifications.setAdapter(mNotificationDetailAdapter);
	}

	@Override
	protected void destorytView() {
		// TODO Auto-generated method stub

	}
	
	private void finishWithClean(){
		if(mCloseDetail != null){
			mCloseDetail.closeWithClear(notificationDetails);
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
			case R.id.notification_detail_clear:
				finishWithClean();
				break;
	
			default:
				break;
		}
	}
	
	public interface ICloseDetail{
		public void closeWithClear(PhonePkgNotification notificationDetails);
	}

}

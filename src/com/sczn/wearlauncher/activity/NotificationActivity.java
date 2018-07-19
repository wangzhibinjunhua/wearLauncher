package com.sczn.wearlauncher.activity;

import com.sczn.wearlauncher.R;
import com.sczn.wearlauncher.fragment.NotificationMainFragment;

import android.app.FragmentTransaction;
import android.os.Bundle;

public class NotificationActivity extends AbsActivity {
	private static final String TAG = NotificationActivity.class.getSimpleName();
	
	private static final String FRAGMENT_TAG_NOTIFICATION = "fragment_main_notification";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_notification);
		initView();
	}

	private void initView() {
		// TODO Auto-generated method stub
		final NotificationMainFragment fragment = (NotificationMainFragment) getFragmentManager()
				.findFragmentByTag(FRAGMENT_TAG_NOTIFICATION);
		if(fragment == null){
			FragmentTransaction ft = getFragmentManager().beginTransaction();
			ft.add(R.id.notification_fragment, new NotificationMainFragment(),
					FRAGMENT_TAG_NOTIFICATION);
			ft.commit();
		}
	}


	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
	
}

package com.sczn.wearlauncher.fragment.card;

import com.sczn.wearlauncher.R;
import com.sczn.wearlauncher.activity.card.SleepActivity;
import com.sczn.wearlauncher.activity.card.StepCountActivity;
import com.sczn.wearlauncher.fragment.absFragment;
import com.sczn.wearlauncher.util.MxyLog;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class StepFragment extends absFragment {

	private TextView mStep;
	private TextView mDis;
	private TextView mCal;
	private TextView mTarget;
	@Override
	protected int getLayoutResouceId() {
		// TODO Auto-generated method stub
		return R.layout.fragment_card_step;
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		mStep = findViewById(R.id.step_count);
		mTarget = findViewById(R.id.step_target);
		mDis = findViewById(R.id.step_dis);
		mCal = findViewById(R.id.step_cal);
	}

	@Override
	protected void initData() {
		// TODO Auto-generated method stub
		mStep.setText("0");
		mTarget.setText("10000");
		mDis.setText(getString(R.string.step_dis, 0.0));
		mCal.setText(getString(R.string.step_cal, 0.0));
		
		
		getView().setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				try {
					Intent i = new Intent(getActivity(), StepCountActivity.class);
					i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					getActivity().startActivity(i);
				} catch (Exception e) {
					// TODO: handle exception
					MxyLog.e(this, "gotoSleepActivity--" + e.toString());
				}
			}
		});
	}

}

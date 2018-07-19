package com.sczn.wearlauncher.activity;

import com.sczn.wearlauncher.R;
import com.sczn.wearlauncher.adapter.ClockSkinChooseAdapter;
import com.sczn.wearlauncher.adapter.ClockSkinChooseAdapter.IClockSkinChooseClick;
import com.sczn.wearlauncher.util.ClockSkinUtil;
import com.sczn.wearlauncher.util.MxyLog;
import com.sczn.wearlauncher.util.SharedPreferencesUtils;
import com.sczn.wearlauncher.view.PagerRecylerView;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.TextView;

public class ClockSkinChooseActivity extends AbsActivity implements IClockSkinChooseClick{
	private static final String TAG = ClockSkinChooseActivity.class.getSimpleName();
	public static final String RESULT_EXTRA_CLOCK_INDEX = "clock_index";
    public static final int RESULT_CODE_CLOCK_CHOOSE = 2;
	private PagerRecylerView clockChooseRecyclerView;
	private ClockSkinChooseAdapter clockSkinChooseAdapter;
	private TextView mEmptyText;
	private ClockSkinTask mClockSkinTask;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		clockSkinChooseAdapter = new ClockSkinChooseAdapter(this);
		setContentView(R.layout.activity_clock_skin_choose);
		initView();
		initData();
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		if(mClockSkinTask != null){
			mClockSkinTask.cancel(true);
		}
		super.onDestroy();
	}

	private void initView() {
		// TODO Auto-generated method stub
		clockChooseRecyclerView = (PagerRecylerView) findViewById(R.id.clock_skin_choose_recyleview);
		clockChooseRecyclerView.initLayoutManager(LinearLayoutManager.HORIZONTAL, false);
		clockChooseRecyclerView.setFlingVelocity(2);
		mEmptyText = (TextView) findViewById(R.id.ckock_skin_choose_empty);
		//clockChooseRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
		//clockChooseRecyclerView.setEmpty(mEmptyText);
	}

	private void initData() {
		// TODO Auto-generated method stub
		clockSkinChooseAdapter.setOnItemClickListen(this);
		clockChooseRecyclerView.setAdapter(clockSkinChooseAdapter);
		mClockSkinTask = new ClockSkinTask();
		mClockSkinTask.execute(this);
	}
	
	private void setClockSkinFiles(String[] files){
		if(files == null || files.length == 0){
			if(mEmptyText != null){
				mEmptyText.setText(getString(R.string.clcock_skin_loader_error));
			}
			return;
		}
		if(clockSkinChooseAdapter != null){
			clockSkinChooseAdapter.setClockSkinFiles(files);
		}
		if(clockChooseRecyclerView != null){
			//clockChooseRecyclerView.scrollToPosition(SharedPreferencesUtils.getIntParam(this,"BT_CLOCK",0));
			//clockChooseRecyclerView.scollerToPositionWithOffset(SharedPreferencesUtils.getIntParam(this,"BT_CLOCK",0), 50);
			clockChooseRecyclerView.setInitPosition(SharedPreferencesUtils.getIntParam(this,"BT_CLOCK",0));
			//MxyLog.d(TAG, "clockChooseRecyclerView.getMeasuredWidth()=" + clockChooseRecyclerView.getMeasuredWidth());
		}
	}
	
	private class ClockSkinTask extends AsyncTask<Context, Void, String[]>{
		
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}
		
		@Override
		protected String[] doInBackground(Context... params) {
			// TODO Auto-generated method stub
			return ClockSkinUtil.getAllClockSkins(params[0]);
		}

		@Override
		protected void onPostExecute(String[] result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if(!isCancelled()){
				setClockSkinFiles(result);
			}
		}

	}

	@Override
	public void onItemClick(View view, int position) {
		// TODO Auto-generated method stub
		SharedPreferencesUtils.setParam(this, "BT_CLOCK", position);
		Intent intent = new Intent();
        intent.putExtra(RESULT_EXTRA_CLOCK_INDEX, position);
        this.setResult(RESULT_CODE_CLOCK_CHOOSE,intent);
        finish();
	}
}

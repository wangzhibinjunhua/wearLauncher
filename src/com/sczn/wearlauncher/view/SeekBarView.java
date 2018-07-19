package com.sczn.wearlauncher.view;

import com.sczn.wearlauncher.R;
import com.sczn.wearlauncher.util.MxyLog;
import com.sczn.wearlauncher.util.SysServices;

import android.content.Context;
import android.provider.Settings;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class SeekBarView extends RelativeLayout implements OnClickListener
				, OnSeekBarChangeListener{
	private  static final String TAG = SeekBarView.class.getSimpleName();

	private SeekBar mSeekBar;
	private TextView mSeekBarTitle;
	private TextView mSeekBarLevel;
	private ImageView mSeekBarDown;
	private ImageView mSeekBarUp;
	private ISeekBarStopTouch mSeekBarStopTouch;
	
	private int step = 1;
	public SeekBarView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}
	
	public void setListen(ISeekBarStopTouch listen){
		this.mSeekBarStopTouch = listen;
	}

	@Override
	protected void onFinishInflate() {
		// TODO Auto-generated method stub
		super.onFinishInflate();
		initView();
		initData();
	}

	private void initView() {
		// TODO Auto-generated method stub
		mSeekBar = (SeekBar) SeekBarView.this.findViewById(R.id.seekbar_bar);
		//MxyLog.d(TAG, "mSeekBar=" + mSeekBar);
		mSeekBarTitle = (TextView) findViewById(R.id.seekbar_title);
		mSeekBarLevel = (TextView) findViewById(R.id.seekbar_level);
		mSeekBarDown = (ImageView) findViewById(R.id.seekbar_down);
		mSeekBarUp = (ImageView) findViewById(R.id.seekbar_up);
	}
	
	private void initData() {
		// TODO Auto-generated method stub
		mSeekBarUp.setOnClickListener(this);
		mSeekBarDown.setOnClickListener(this);
		mSeekBar.setOnSeekBarChangeListener(this);
	}

	public void setTitle(String title){
		if(mSeekBarTitle != null){
			mSeekBarTitle.setText(title);
		}
	}
	public void setTitle(int titleId){
		if(mSeekBarTitle != null){
			mSeekBarTitle.setText(titleId);
		}
	}
	public void setSeekBarProgress(int progess){	
		mSeekBar.setProgress(progess);
	}
	
	private void setLevel(int level){
		mSeekBarLevel.setText(String.valueOf(level));
		mSeekBarLevel.scrollTo((mSeekBar.getRight() 
				- mSeekBar.getLeft())*level/mSeekBar.getMax(), 0);
	}
	
	public void setMax(int value){
		mSeekBar.setMax(value);

	}
	public void setEnabled(boolean enabled){
		mSeekBar.setEnabled(enabled);
		mSeekBarUp.setEnabled(enabled);
		mSeekBarDown.setEnabled(enabled);
	}
	
	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		// TODO Auto-generated method stub
		if(!mSeekBar.equals(seekBar)){
			return;
		}
		//MxyLog.i(TAG, "onProgressChanged: progress=" + progress + "fromUser=" + fromUser + "--seekBar=" + seekBar);
		
		setLevel(progress);
	}
	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub
		MxyLog.i(TAG, "onStopTrackingTouch: progress=" + seekBar.getProgress());
		if(mSeekBarStopTouch != null){
			mSeekBarStopTouch.onSeekBarStopTouch(getId(), seekBar.getProgress());
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
			case R.id.seekbar_up:
				mSeekBar.setProgress(Math.min(mSeekBar.getProgress() + step, mSeekBar.getMax()));
				break;
			case R.id.seekbar_down:
				mSeekBar.setProgress(Math.max(mSeekBar.getProgress() - step, 0));
				break;
			default:
				break;
		}
	}
	
	public interface ISeekBarStopTouch{
		public void onSeekBarStopTouch(int id, int progess);
	}

}

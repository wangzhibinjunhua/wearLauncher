package com.sczn.wearlauncher.fragment.status;

import android.media.AudioManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.sczn.wearlauncher.R;
import com.sczn.wearlauncher.fragment.absDialogFragment;
import com.sczn.wearlauncher.util.MxyLog;
import com.sczn.wearlauncher.util.SysServices;
import com.sczn.wearlauncher.view.SeekBarView;
import com.sczn.wearlauncher.view.SeekBarView.ISeekBarStopTouch;

public class VolumeFragment extends absDialogFragment implements OnClickListener,ISeekBarStopTouch{
	private static final String TAG = VolumeFragment.class.getSimpleName();
	private static VolumeFragment instance;
	public static VolumeFragment getInstance(){
		/*
		if(instance == null){
			synchronized (VolumeFragment.class) {
				instance = new VolumeFragment();
			}
		}
		return instance;*/
		return new VolumeFragment();
	}
	public VolumeFragment(){
		super();
	}

	private  AudioManager mAudioManager;
	private TextView mTextDone;
	private SeekBarView mMultiMediaSeekBar;
	private SeekBarView mRingSeekBar;
	private SeekBarView mAlarmSeekBar;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mAudioManager = SysServices.getaudioMgr(getActivity());
	}
	@Override
	protected int getLayoutResouceId() {
		// TODO Auto-generated method stub
		return R.layout.fragment_status_volume;
	}

	@Override
	protected void creatView() {
		// TODO Auto-generated method stub
		
		initView();
		initData();
	}
	private void initView() {
		// TODO Auto-generated method stub
		mTextDone = findViewById(R.id.volume_done);
		mMultiMediaSeekBar = findViewById(R.id.volume_multimedia);
		mRingSeekBar = findViewById(R.id.volume_ring);
		mAlarmSeekBar = findViewById(R.id.volume_alarm);
		
		mMultiMediaSeekBar.setTitle(R.string.volume_mulrimedia);
		mRingSeekBar.setTitle(R.string.volume_ring);
		mAlarmSeekBar.setTitle(R.string.volume_alarm);
		
		//MxyLog.d(TAG, "mMultiMediaSeekBar=" + mMultiMediaSeekBar.getId() + "--mRingSeekBar=" + mRingSeekBar.getId() + "--mAlarmSeekBar=" + mAlarmSeekBar.getId());
	}
	private void initData() {
		// TODO Auto-generated method stub
		mTextDone.setOnClickListener(this);

		mMultiMediaSeekBar.setMax(mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
		mRingSeekBar.setMax(mAudioManager.getStreamMaxVolume(AudioManager.STREAM_RING));
		mAlarmSeekBar.setMax(mAudioManager.getStreamMaxVolume(AudioManager.STREAM_ALARM));
		mMultiMediaSeekBar.setSeekBarProgress(mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC));
		mRingSeekBar.setSeekBarProgress(mAudioManager.getStreamVolume(AudioManager.STREAM_RING));
		mAlarmSeekBar.setSeekBarProgress(mAudioManager.getStreamVolume(AudioManager.STREAM_ALARM));
		
		//MxyLog.d(TAG, "STREAM_RING=" + mAudioManager.getStreamVolume(AudioManager.STREAM_RING));
		
		mMultiMediaSeekBar.setListen(this);
		mRingSeekBar.setListen(this);
		mAlarmSeekBar.setListen(this);
	}
	@Override
	protected void destorytView() {
		// TODO Auto-generated method stub

	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
			case R.id.volume_done:
				dismiss();
				break;
			default:
				break;
	}
	}
	@Override
	public void onSeekBarStopTouch(int id, int progess) {
		// TODO Auto-generated method stub
		MxyLog.d(TAG, "id=" + id + "--progess=" + progess);
		switch (id) {
			case R.id.volume_multimedia:
				mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 
						progess, AudioManager.FLAG_ALLOW_RINGER_MODES);
				mAudioManager.setStreamVolume(AudioManager.STREAM_SYSTEM, 
						progess, AudioManager.FLAG_ALLOW_RINGER_MODES);
				mAudioManager.setStreamVolume(AudioManager.STREAM_VOICE_CALL, 
						(progess + 1)/2, AudioManager.FLAG_ALLOW_RINGER_MODES);
				break;
			case R.id.volume_ring:
				mAudioManager.setStreamVolume(AudioManager.STREAM_RING, 
						progess, AudioManager.FLAG_ALLOW_RINGER_MODES);
				mAudioManager.setStreamVolume(AudioManager.STREAM_NOTIFICATION, 
						progess, AudioManager.FLAG_ALLOW_RINGER_MODES);
				break;
			case R.id.volume_alarm:
				mAudioManager.setStreamVolume(AudioManager.STREAM_ALARM, 
						progess, AudioManager.FLAG_ALLOW_RINGER_MODES);
				break;
			default:
				break;
		}
	}

}

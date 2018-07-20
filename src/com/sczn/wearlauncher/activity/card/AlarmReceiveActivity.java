package com.sczn.wearlauncher.activity.card;

import java.util.ArrayList;
import java.util.Calendar;

import com.sczn.wearlauncher.R;
import com.sczn.wearlauncher.util.MxyLog;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.Vibrator;
import android.view.Window;

import com.sczn.wearlauncher.card.healthalarm.UtilHealthAlarm;
import com.sczn.wearlauncher.card.healthalarm.UtilHealthAlarm.HealthAlarmReceiver;
public class AlarmReceiveActivity extends Activity{
	 private PowerManager.WakeLock mWakeLock;
	 protected MediaPlayer mMediaPlayer = null;
	 private Vibrator mVibrator;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE); // hide title
		mMediaPlayer =new MediaPlayer();
	    startMedia();
	    startVibrator();
	    createDialog();
	}
	@Override
	protected void onResume() {
	    super.onResume();
	    // 唤醒屏幕
	    acquireWakeLock();
	}
	@Override
	protected void onPause() {
	    super.onPause();
	    releaseWakeLock();
	}
	
	/**
	 * 唤醒屏幕
	 */
	private void acquireWakeLock() {
	    if (mWakeLock == null) {
	        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
	        mWakeLock = pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP
	                | PowerManager.SCREEN_DIM_WAKE_LOCK, this.getClass()
	                .getCanonicalName());
	        mWakeLock.acquire();
	    }
	}
	
	/**
	 * 释放锁屏
	 */
	private void releaseWakeLock() {
	    if (mWakeLock != null && mWakeLock.isHeld()) {
	    	mWakeLock.release();
	    	mWakeLock = null;
	    }
	}
	
	/**
	 * 开始播放铃声
	 */
	private void startMedia() {
	    try {
		    	mMediaPlayer.setDataSource(this,
		                RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)); //铃声类型为默认闹钟铃声
		    	mMediaPlayer.prepare();
		    	mMediaPlayer.start();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
	
	/**
	 * 震动
	 */
	private void startVibrator() {
	    /**
	     * 想设置震动大小可以通过改变pattern来设定，如果开启时间太短，震动效果可能感觉不到
	     * 
	     */
		mVibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
	    long[] pattern = { 500, 1000, 500, 1000 }; // 停止 开启 停止 开启
	    mVibrator.vibrate(pattern, 0);
	}
	
	private void createDialog() {
        new AlertDialog.Builder(this)
                .setIcon(R.drawable.drink_little)
                .setTitle(getString(R.string.alarm_sit_long))
                .setMessage(getString(R.string.alarm_sit_long_des))
           /*     .setPositiveButton(getString(R.string.alarm_delay), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        tenMRemind();
                        mMediaPlayer.stop();
                        mVibrator.cancel();
                        finish();
                    }
                })*/
                .setNegativeButton(getString(R.string.alarm_close), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    	mMediaPlayer.stop();
                    	mVibrator.cancel();
                        finish();
                    }
                }).show();
    }
	
	/**
	 * 推迟10分钟提醒
	 */
	private void tenMRemind(){
	    //设置时间
	    Calendar calendar_now =Calendar.getInstance();


	    calendar_now.setTimeInMillis(System.currentTimeMillis());
	    calendar_now.set(Calendar.HOUR_OF_DAY, calendar_now.get(Calendar.HOUR_OF_DAY));
	    calendar_now.set(Calendar.MINUTE, calendar_now.get(Calendar.MINUTE)+5);
	    calendar_now.set(Calendar.SECOND, 0);
	    calendar_now.set(Calendar.MILLISECOND, 0);

	    //时间选择好了
	    Intent intent = new Intent(this, HealthAlarmReceiver.class);
	    intent.setAction(UtilHealthAlarm.ACTION_HEALTHALARM_SIT);
	    //注册闹钟广播
	    PendingIntent sender = PendingIntent.getBroadcast(
	            this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

	    AlarmManager am;
	    am = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
	    am.set(AlarmManager.RTC_WAKEUP, calendar_now.getTimeInMillis(), sender);
	}
}

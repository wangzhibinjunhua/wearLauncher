package com.sczn.wearlauncher.card.healthalarm;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import com.sczn.wearlauncher.LauncherApp;
import com.sczn.wearlauncher.db.provider.HealthAlarmProvider;
import com.sczn.wearlauncher.db.provider.Provider;
import com.sczn.wearlauncher.db.provider.Provider.HealthAlarmcolumns;
import com.sczn.wearlauncher.util.MxyLog;

import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;



public class UtilHealthAlarm {
	
	private static class Holder{
		private static UtilHealthAlarm instance = new UtilHealthAlarm();
	}
	public static UtilHealthAlarm getInstance(){
		return Holder.instance;
	} 
	public static final int MAX_ALARM_COUNT = 10;
	public static final String ACTION_HEALTHALARM_DRINK = "broadcast.alarm.healthalarm.drink";
	public static final String ACTION_HEALTHALARM_SIT = "broadcast.alarm.healthalarm.sit";
	
	public static final String MAP_KEY_DRINKS = "alarm_drinks";
	public static final String MAP_KEY_SITS = "alarm_sits";
	public static final String MAP_KEY_NEXT_DRINK = "alarm_next_sit";
	public static final String MAP_KEY_NEXT_SIT = "alarm_next_sit";
	public static final String MAP_KEY_NEXT_ALARM = "alarm_next_alarm";
	

	private HashMap<String, ArrayList<ModelAlarm>> mAlarmMap;
	private GetAllAlarmTask mGetAllAlarmTask;
	private HealAlarmDbObserver mHealAlarmDbObserver;
	private ArrayList<IHealthAlarmListen> mListens;
	
	private UtilHealthAlarm(){
		mAlarmMap = new HashMap<String, ArrayList<ModelAlarm>>();
	}
	
	public void InitAlarmIntent(){
		startGetAlarm();
	}
	
	public void addListen(IHealthAlarmListen listen){
		if(mListens == null){
			mListens = new ArrayList<UtilHealthAlarm.IHealthAlarmListen>();
		}
		if(!mListens.contains(listen)){
			mListens.add(listen);
		}
	}
	public void removeListen(IHealthAlarmListen listen){
		if(mListens == null){
			return;
		}
		mListens.remove(listen);
	}
	
	public ArrayList<ModelAlarm> getDrinkAlarms(){
		return mAlarmMap.get(MAP_KEY_DRINKS);
	}
	public ArrayList<ModelAlarm> getSitAlarms(){
		return mAlarmMap.get(MAP_KEY_SITS);
	}
	public ModelAlarm getNextAlarm(){
		final ArrayList<ModelAlarm> alarms = mAlarmMap.get(MAP_KEY_NEXT_ALARM);
		if(alarms != null && alarms.size() > 0){
			return alarms.get(0);
		}
		return null;
	}
	public ModelAlarm getNextDrinkAlarm(){
		final ArrayList<ModelAlarm> alarms = mAlarmMap.get(MAP_KEY_NEXT_DRINK);
		if(alarms != null && alarms.size() > 0){
			return alarms.get(0);
		}
		return null;
	}
	public ModelAlarm getNextSitAlarm(){
		final ArrayList<ModelAlarm> alarms = mAlarmMap.get(MAP_KEY_NEXT_SIT);
		if(alarms != null && alarms.size() > 0){
			return alarms.get(0);
		}
		return null;
	}
	
	public void startMgr(Context context){
		mHealAlarmDbObserver = new HealAlarmDbObserver(new Handler());
		mHealAlarmDbObserver.register(context);
		
		startGetAlarm();
	}
	
	public void stopMgr(Context context){
		mHealAlarmDbObserver.unReidter(context);
		mHealAlarmDbObserver = null;
		
		stopGetAlarm();
	}
	
	private void startGetAlarm(){
		stopGetAlarm();
		mGetAllAlarmTask = new GetAllAlarmTask();
		mGetAllAlarmTask.execute();
	}
	private void stopGetAlarm(){
		if(mGetAllAlarmTask != null){
			mGetAllAlarmTask.cancel(true);
			mGetAllAlarmTask = null;
		}
	}
	
	private void alarmChanged(HashMap<String, ArrayList<ModelAlarm>> alarms){
		if(alarms == null){
			alarms = new HashMap<String, ArrayList<ModelAlarm>>();
		}
		this.mAlarmMap = alarms;
	}
	
	private class HealAlarmDbObserver extends ContentObserver{
		
		private void register(Context context){
			context.getContentResolver().registerContentObserver(getUri(), true, this);
		}
		
		public void unReidter(Context context){
			context.getContentResolver().unregisterContentObserver(this);
		}
		
		private Uri getUri(){
			return HealthAlarmcolumns.CONTENT_URI;
		}

		public HealAlarmDbObserver(Handler handler) {
			super(handler);
			// TODO Auto-generated constructor stub
			startGetAlarm();
		}
		
	}
	
	private class GetAllAlarmTask extends AsyncTask<Void, Void, HashMap<String, ArrayList<ModelAlarm>>>{

		@Override
		protected HashMap<String, ArrayList<ModelAlarm>> doInBackground(Void... params) {
			// TODO Auto-generated method stub
			MxyLog.d(this, "GetAllAlarmTask start");
			Cursor cursor = LauncherApp.appContext.getContentResolver().query(HealthAlarmcolumns.CONTENT_URI,
					null, null, null, null);
			
			MxyLog.d(this, "GetAllAlarmTask cursor=" + cursor);
			
			if(isCancelled()){
				cursor.close();
				return null;
			}
			
			if(cursor == null){
				return null;
			}
			if(!cursor.moveToFirst()){
				cursor.close();
				return null;
			}
			
			final HashMap<String, ArrayList<ModelAlarm>> alarms = new HashMap<String, ArrayList<ModelAlarm>>();
			final ArrayList<ModelAlarm> drinkAlarms = new ArrayList<ModelAlarm>();
			final ArrayList<ModelAlarm> sitAlarms = new ArrayList<ModelAlarm>();
			final ArrayList<ModelAlarm> nextAlarm = new ArrayList<ModelAlarm>();
			final ArrayList<ModelAlarm> nextDrinkAlarm = new ArrayList<ModelAlarm>();
			final ArrayList<ModelAlarm> nextSitAlarm = new ArrayList<ModelAlarm>();
			
			final Calendar calendar = Calendar.getInstance();
			final long currTime = calendar.getTimeInMillis() + 10*1000;		//预留10S处理数据
			final int currWekkday = calendar.get(Calendar.DAY_OF_WEEK);
			
			boolean hasNextAlarm = false;
			boolean hasNextDrink = false;
			boolean hasMextSit = false;
			do{
				if(isCancelled()){
					cursor.close();
					return null;
				}
				
				final ModelAlarm alarm = new ModelAlarm();
				final int type = cursor.getInt(cursor.getColumnIndex(Provider.HealthAlarmcolumns.COLUMNS_TYPE));
				final long time = cursor.getLong(cursor.getColumnIndex(Provider.HealthAlarmcolumns.COLUMNS_TIME));
				final int repeat = cursor.getInt(cursor.getColumnIndex(Provider.HealthAlarmcolumns.COLUMNS_REPEAT));
				final boolean enable = cursor.getInt(cursor.getColumnIndex(Provider.HealthAlarmcolumns.COLUMNS_EBABLE)) == 1;
				
				alarm.setTimeInDay(time);
				alarm.setRepeatDay(repeat);
				alarm.setEnable(enable);
				
				switch (type) {
					case ModelAlarm.ALARM_TYPE_DRINK:
						if(!hasNextAlarm){
							if(alarm.isValue(currTime, currWekkday)){
								nextAlarm.add(alarm);
								alarms.put(MAP_KEY_NEXT_ALARM, nextAlarm);
								hasNextAlarm = true;
							}
						}
						if(!hasNextDrink){
							if(alarm.isValue(currTime, currWekkday)){
								nextDrinkAlarm.add(alarm);
								alarms.put(MAP_KEY_NEXT_DRINK, nextDrinkAlarm);
								hasNextDrink = true;
							}
						}
						drinkAlarms.add(alarm);
						break;
					case ModelAlarm.ALARM_TYPE_SIT:
						if(!hasNextAlarm){
							if(alarm.isValue(currTime, currWekkday)){
								nextAlarm.add(alarm);
								alarms.put(MAP_KEY_NEXT_ALARM, nextAlarm);
								hasNextAlarm = true;
							}
						}
						if(!hasMextSit){
							if(alarm.isValue(currTime, currWekkday)){
								nextSitAlarm.add(alarm);
								alarms.put(MAP_KEY_NEXT_SIT, nextSitAlarm);
								hasMextSit = true;
							}
						}
						sitAlarms.add(alarm);
						break;
					default:
						break;
				}
			}while(cursor.moveToNext());
			
			alarms.put(MAP_KEY_DRINKS, drinkAlarms);
			alarms.put(MAP_KEY_SITS, sitAlarms);
			
			return alarms;
		}
		
		@Override
		protected void onPostExecute(HashMap<String, ArrayList<ModelAlarm>> result) {
			// TODO Auto-generated method stub
			if(isCancelled()){
				return;
			}
			alarmChanged(result);
		}
	}
	
	public interface IHealthAlarmListen{
		public void onHealthAlarmChanged();
	}
}

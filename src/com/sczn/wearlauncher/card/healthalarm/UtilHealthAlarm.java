package com.sczn.wearlauncher.card.healthalarm;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;

import com.sczn.wearlauncher.LauncherApp;
import com.sczn.wearlauncher.activity.card.AlarmReceiveActivity;
import com.sczn.wearlauncher.db.provider.HealthAlarmProvider;
import com.sczn.wearlauncher.db.provider.Provider;
import com.sczn.wearlauncher.db.provider.Provider.HealthAlarmcolumns;
import com.sczn.wearlauncher.receivers.AbsBroadcastReceiver;
import com.sczn.wearlauncher.util.DateFormatUtil;
import com.sczn.wearlauncher.util.MxyLog;
import com.sczn.wearlauncher.util.SystemUtils;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.app.ProgressDialog;


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
	public static final String MAP_KEY_NEXT_DRINK = "alarm_next_drink";
	public static final String MAP_KEY_NEXT_SIT = "alarm_next_sit";
	public static final String MAP_KEY_NEXT_ALARM = "alarm_next_alarm";

	public static final String MAP_KEY_SITS_TEMP = "alarm_sits_temp";
	

	private HashMap<String, ArrayList<ModelAlarm>> mAlarmMap;
	private GetAllAlarmTask mGetAllAlarmTask;
	private HealAlarmDbObserver mHealAlarmDbObserver;
	private ArrayList<IHealthAlarmListen> mListens;
	private AlarmManager mAlarmManager;
	private HealthAlarmReceiver mHealthAlarmReceiver;
	private UtilHealthAlarm(){
		mAlarmMap = new HashMap<String, ArrayList<ModelAlarm>>();
		mHealthAlarmReceiver = new HealthAlarmReceiver();
	}
	private ProgressDialog progressDialog;
	public void InitAlarmIntent(){
		startGetAlarm();
		/*if(mListens!=null)
			{
				if(mListens.get(0)!=null)
				{
					mListens.get(0).onHealthAlarmChanged();
				}
			}*/
	}
	
	public void addListen(IHealthAlarmListen listen){
		if(mListens == null){
			mListens = new ArrayList<UtilHealthAlarm.IHealthAlarmListen>();
		}
		Log.e("mxy","addListen= "+listen+"mListens = "+mListens);
		if(!mListens.contains(listen)){
			mListens.add(listen);
		}
		Log.e("mxy","addListen= "+listen+"mListens = "+mListens);
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
		Log.e("mxy","getSitAlarms mAlarmMap "+mAlarmMap.get(MAP_KEY_SITS));
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
		Log.e("mxy","getNextSitAlarm " +alarms+"mAlarmMap " +mAlarmMap);
		if(alarms != null && alarms.size() > 0){
			Log.e("mxy","getNextSitAlarm " +alarms.get(0));
			return alarms.get(0);
		}
		return null;
	}
	
	public void startMgr(Context context){
		mHealAlarmDbObserver = new HealAlarmDbObserver(new Handler());
		mHealAlarmDbObserver.register(context);
		
		mHealthAlarmReceiver.register(context);
		startGetAlarm();
	}
	
	public void stopMgr(Context context){
		mHealAlarmDbObserver.unReidter(context);
		mHealAlarmDbObserver = null;
		
		mHealthAlarmReceiver.unRegister(context);
		stopGetAlarm();
	}
	
	private void startGetAlarm(){
		Log.e("mxy","startGetAlarm ");
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
	private void setSitAlarm(Context context){
			PendingIntent mSitAlarm;
		    mSitAlarm = PendingIntent.getBroadcast(context, 0, new Intent(ACTION_HEALTHALARM_SIT), PendingIntent.FLAG_CANCEL_CURRENT);
		    mAlarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
			Log.e("mxy","setSitAlarm  getNextSitAlarmTime ="+getNextSitAlarmTime());
			if(getNextSitAlarmTime()>0)
				{
				    mAlarmManager.cancel(mSitAlarm);
					mAlarmManager.setWindow(AlarmManager.RTC_WAKEUP,
					        		getNextSitAlarmTime(), 1000,mSitAlarm);
				}
			else
				{
					mAlarmManager.cancel(mSitAlarm);
				}
	}
   private void setDrinkAlarm(Context context){
			PendingIntent mDrinkAlarm;
		    mDrinkAlarm = PendingIntent.getBroadcast(context, 0, new Intent(ACTION_HEALTHALARM_DRINK), PendingIntent.FLAG_CANCEL_CURRENT);
		    mAlarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
			Log.e("mxy","setDrinkAlarm  getNextDrinkAlarmTime ="+getNextDrinkAlarmTime());
			if(getNextDrinkAlarmTime()>0)
				{
					mAlarmManager.cancel(mDrinkAlarm);
					mAlarmManager.setWindow(AlarmManager.RTC_WAKEUP,
					        		getNextDrinkAlarmTime(),1000, mDrinkAlarm);
				}
			else
				{
					mAlarmManager.cancel(mDrinkAlarm);
				}
	}
   public void setHealthAlarm(Context context)
   	{
   		setSitAlarm(context);
		setDrinkAlarm(context);
   	}
		public  long getNextDrinkAlarmTime() {
		    final SimpleDateFormat fmt = new SimpleDateFormat();
		    final Calendar c = Calendar.getInstance();
			final Calendar calendar = Calendar.getInstance();
			calendar.setTimeZone(TimeZone.getTimeZone(SystemUtils.getCurrentTimeZone()));
			final long currTime = calendar.getTimeInMillis();
			int mHours =calendar.get(Calendar.HOUR_OF_DAY);
			int mMinuts=calendar.get(Calendar.MINUTE); 
			final int currWekkday = calendar.get(Calendar.DAY_OF_WEEK);
			ModelAlarm drinkNextAlarm =getNextDrinkAlarm();
			long timeInDay=0;
			int repeatDay=0;
			final long currTimeshort = (long)(mHours*60*60*1000+mMinuts*60*1000);
			long ydTime=currTime-(long)(mHours*60*60*1000+mMinuts*60*1000);
			long nextTime=0;
	        if(drinkNextAlarm!=null)
	        	{
					repeatDay =drinkNextAlarm.getRepeatDay();
				if(drinkNextAlarm.isValue(currTimeshort,currWekkday)){
							nextTime = drinkNextAlarm.getTimeInDay()+ydTime;
							Log.e("mxy"," notDrinkRepeat nextTime"+ nextTime);
						}
				else if(drinkNextAlarm.isRepeat(currTimeshort,currWekkday))
					  {
							long time = drinkNextAlarm.getTimeInDay();
							nextTime=getNextTime(repeatDay,currWekkday,time,currTimeshort);
							Log.e("mxy"," isDrinkRepeat nextTime"+ nextTime);
						}
	        	}
				 return nextTime;
		}
	public  long getNextSitAlarmTime() {
	    final SimpleDateFormat fmt = new SimpleDateFormat();
	    //final Calendar c = Calendar.getInstance();
	    final long now = System.currentTimeMillis();
		ModelAlarm sitNextAlarm =getNextSitAlarm();
		final Calendar calendar = Calendar.getInstance();
		calendar.setTimeZone(TimeZone.getTimeZone(SystemUtils.getCurrentTimeZone()));
		final long currTime = calendar.getTimeInMillis();
		
		int mHours =calendar.get(Calendar.HOUR_OF_DAY);
		int mMinuts=calendar.get(Calendar.MINUTE); 
		final int currWekkday = calendar.get(Calendar.DAY_OF_WEEK);
		final long currTimeshort = (long)(mHours*60*60*1000+mMinuts*60*1000);
		long timeInDay=0;
		int repeatDay=0;
		long nextTime=0;
		long ydTime=currTime-(long)(mHours*60*60*1000+mMinuts*60*1000);
        if(sitNextAlarm!=null)
        	{
				repeatDay =sitNextAlarm.getRepeatDay();
				if(sitNextAlarm.isValue(currTimeshort,currWekkday))
					{
						nextTime = sitNextAlarm.getTimeInDay()+ydTime;
						Log.e("mxy"," notRepeat nextTime"+ nextTime);
					}
				else if(sitNextAlarm.isRepeat(currTimeshort,currWekkday))
					{
						long time = sitNextAlarm.getTimeInDay();
						nextTime=getNextTime(repeatDay,currWekkday,time,currTimeshort);
						Log.e("mxy"," isRepeat nextTime"+ nextTime);
					}
        	}
		 	return nextTime;
}

		private long getNextTime(int repeatDay,int currWekkday,long time,long currtime)
			{
				long nextTime=0;
				int nearWeekDay =getRepeaWeekDay(repeatDay,currWekkday,currtime,time);
				final Calendar calendar = Calendar.getInstance();
				final SimpleDateFormat fmt = new SimpleDateFormat();
				final long now = System.currentTimeMillis();
				calendar.setTimeZone(TimeZone.getTimeZone(SystemUtils.getCurrentTimeZone()));
				fmt.applyPattern("HH:mm");
				Date d = null;
				try {
					d = fmt.parse(DateFormatUtil.getDayTimeString(time));
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Log.e("mxy","getNextTime ,hours = "+d.getHours()+"minutes = "+d.getMinutes());
				calendar.add(Calendar.DATE,nearWeekDay);
				calendar.set(Calendar.HOUR_OF_DAY, d.getHours());
				calendar.set(Calendar.MINUTE, d.getMinutes());
				calendar.set(Calendar.SECOND, 0);
				calendar.set(Calendar.MILLISECOND, 0);
				//calendar.set(Calendar.DAY_OF_WEEK, nearWeekDay);
				nextTime=calendar.getTimeInMillis();
				Log.e("mxy"," nextTime"+ nextTime);
				return nextTime;
			}
		private int getRepeaWeekDay(int repeatDay,int currWekkday,long currtime,long indaytime)
			{
			    int[] weeks=null;
				weeks =new int[7];
				int k=0;
				int nearweek=7;
				int temp=0 ;
				int weekDay=0;
			    for(int i=0;i<7;i++)
			    	{
			    	    
			    		if(((repeatDay & 1<<(i+1))>>(i+1))==1)
			    			{
								weeks[k]=i+1;
								if(currWekkday>(i+1))
									{
										temp=7-currWekkday+i+1;
									}
								else if((currWekkday==(i+1)))
									{
										if(currtime<indaytime)
											{
											if(currWekkday==7)
												{
													temp =1;
												}
											else
												{
													temp =currWekkday+1;
												}
										}
									}
								else
									{
										temp=i+1-currWekkday;
									}
							if(nearweek>temp)
								{
									nearweek =temp;
									weekDay=i+1;
								}
								k++;
			    			}
			    	}
			    Log.e("mxy"," getRepeaWeekDay weekDay"+ weekDay);
				return nearweek;
			}
	
	private class GetAllAlarmTask extends AsyncTask<Void, Void, HashMap<String, ArrayList<ModelAlarm>>>{

		@Override
		protected HashMap<String, ArrayList<ModelAlarm>> doInBackground(Void... params) {
			// TODO Auto-generated method stub
			MxyLog.e("mxy", "GetAllAlarmTask start");
			Cursor cursor = LauncherApp.appContext.getContentResolver().query(HealthAlarmcolumns.CONTENT_URI,
					null, null, null, null);
			
			MxyLog.e("mxy", "GetAllAlarmTask cursor=" + cursor);
			
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
			//final long currTime = calendar.getTimeInMillis() + 10*1000;		//预留10S处理数据
			calendar.setTimeZone(TimeZone.getTimeZone(SystemUtils.getCurrentTimeZone()));
			int mHours =calendar.get(Calendar.HOUR_OF_DAY);
			int mMinuts=calendar.get(Calendar.MINUTE); 
			final long currTime =(long)(mHours*60*60*1000+mMinuts*60*1000);
			final int currWekkday = calendar.get(Calendar.DAY_OF_WEEK);
			MxyLog.e("mxy", "GetAllAlarmTask currTime=" + currTime +"currWekkday = "+currWekkday+"mHours = "+mHours+"mMinuts = "+mMinuts);
			boolean hasNextAlarm = false;
			boolean hasNextDrink = false;
			boolean hasMextSit = false;
			do{
				if(isCancelled()){
					cursor.close();
					return null;
				}
				
				final ModelAlarm alarm = new ModelAlarm();
				final int id = cursor.getInt(cursor.getColumnIndex(Provider.HealthAlarmcolumns._ID));
				final int type = cursor.getInt(cursor.getColumnIndex(Provider.HealthAlarmcolumns.COLUMNS_TYPE));
				final long time = cursor.getLong(cursor.getColumnIndex(Provider.HealthAlarmcolumns.COLUMNS_TIME));
				final int repeat = cursor.getInt(cursor.getColumnIndex(Provider.HealthAlarmcolumns.COLUMNS_REPEAT));
				final boolean enable = cursor.getInt(cursor.getColumnIndex(Provider.HealthAlarmcolumns.COLUMNS_EBABLE)) == 1;

				alarm.setID(id);
				alarm.setType(type);
				alarm.setTimeInDay(time);
				alarm.setRepeatDay(repeat);
				alarm.setEnable(enable);
				Log.e("mxy","id = "+id+" type = "+type+" time = "+time+" repeat = "+repeat+" enable = "+enable);
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
						Log.e("mxy","hasNextAlarm = "+hasNextAlarm);
						Log.e("mxy","alarm.isValue(currTime, currWekkday) = "+alarm.isValue(currTime, currWekkday));
						if(!hasNextAlarm){
							if(alarm.isValue(currTime, currWekkday)){
								nextAlarm.add(alarm);
								alarms.put(MAP_KEY_NEXT_ALARM, nextAlarm);
								hasNextAlarm = true;
								Log.e("mxy","alarms = "+alarms.get(0));
							}
						}
						if(!hasMextSit){
							if(alarm.isValue(currTime, currWekkday)){
								nextSitAlarm.add(alarm);
								alarms.put(MAP_KEY_NEXT_SIT, nextSitAlarm);
								hasMextSit = true;
							}
							//else if(alarm.isRepeat(currTime,currWekkday))
							//	
//{
									
							//	}
						}
						sitAlarms.add(alarm);
						break;
					default:
						break;
				}
			}while(cursor.moveToNext());
			alarms.put(MAP_KEY_DRINKS, drinkAlarms);
			alarms.put(MAP_KEY_SITS, sitAlarms);
			try {
				getRepeatNextSit(alarms);
				getRepeatNextDrink(alarms);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//if(alarms.get())
			MxyLog.e("mxy", "GetAllAlarmTask   alarms= " + alarms);
			return alarms;
		}
		private void getRepeatNextSit(HashMap<String, ArrayList<ModelAlarm>> hmalarms) throws ParseException
			{
				final Calendar calendar = Calendar.getInstance();
				final SimpleDateFormat fmt = new SimpleDateFormat();
				final long now = System.currentTimeMillis();
				calendar.setTimeZone(TimeZone.getTimeZone(SystemUtils.getCurrentTimeZone()));
				int mHours =calendar.get(Calendar.HOUR_OF_DAY);
				int mMinuts=calendar.get(Calendar.MINUTE); 
				final long currTime =(long)(mHours*60*60*1000+mMinuts*60*1000);
				final int currWekkday = calendar.get(Calendar.DAY_OF_WEEK);
				final ArrayList<ModelAlarm> nextSitAlarmTemp = new ArrayList<ModelAlarm>();
			    final ArrayList<ModelAlarm> nextSitalarm = hmalarms.get(MAP_KEY_NEXT_SIT);
				final ArrayList<ModelAlarm> sitAlarm = hmalarms.get(MAP_KEY_SITS);
				//boolean hasNextAlarm = false;
				Log.e("mxy","nextSitalarm " +nextSitalarm+"currTime " +currTime);
				if(nextSitalarm!=null && nextSitalarm.size()>0)
					{
						return;
					}
				else
					{
						if(sitAlarm!=null && sitAlarm.size()>0)
							{
							   boolean hasNextDrink = false;
				               boolean hasNextSit = false;
							   long nextTime = 0; 
							   long tempTime;
							   ModelAlarm alarm =new ModelAlarm();
								for(int i=0;i<sitAlarm.size();i++)
									{

												if(sitAlarm.get(i).isRepeat(currTime,currWekkday))
													{
														//int[] weeks = new int[7];
														//nextSitAlarmTemp.add(sitAlarm.get(i));
														int repeatDay =sitAlarm.get(i).getRepeatDay();
														long time = sitAlarm.get(i).getTimeInDay();
														int nearWeekDay =getRepeaWeekDay(repeatDay,currWekkday,currTime,time);
														Log.e("mxy","nearWeekDay " +nearWeekDay);
														fmt.applyPattern("HH:mm");
														Date d = fmt.parse(DateFormatUtil.getTimeString(DateFormatUtil.HM,
					time));
														calendar.add(Calendar.DATE,nearWeekDay);
														calendar.set(Calendar.HOUR_OF_DAY, d.getHours());
														calendar.set(Calendar.MINUTE, d.getMinutes());
														calendar.set(Calendar.SECOND, 0);
														calendar.set(Calendar.MILLISECOND, 0);
														//calendar.set(Calendar.DAY_OF_WEEK, nearWeekDay);
														tempTime=calendar.getTimeInMillis();
														Log.e("mxy","tempTime  " +tempTime);
														if(nearWeekDay!=0)
															{
																if(nextTime==0)
																	{
																		nextTime = tempTime;
																		alarm = sitAlarm.get(i);	
																	}
																if(nextTime>tempTime)
																	{
																		nextTime=tempTime;
																		alarm = sitAlarm.get(i);
																	}
															}
														Log.e("mxy","nearWeekDay alarm  " +alarm);
													}
									
									}
								nextSitAlarmTemp.add(alarm);
								hmalarms.put(MAP_KEY_NEXT_SIT, nextSitAlarmTemp);
							}							
					}
			}
		private void getRepeatNextDrink(HashMap<String, ArrayList<ModelAlarm>> hmalarms)
			{
				final Calendar calendar = Calendar.getInstance();
				final SimpleDateFormat fmt = new SimpleDateFormat();
				final long now = System.currentTimeMillis();
				calendar.setTimeZone(TimeZone.getTimeZone(SystemUtils.getCurrentTimeZone()));
				int mHours =calendar.get(Calendar.HOUR_OF_DAY);
				int mMinuts=calendar.get(Calendar.MINUTE); 
				final long currTime =(long)(mHours*60*60*1000+mMinuts*60*1000);
				final int currWekkday = calendar.get(Calendar.DAY_OF_WEEK);
				final ArrayList<ModelAlarm> nextSitAlarmTemp = new ArrayList<ModelAlarm>();
			    final ArrayList<ModelAlarm> nextSitalarm = hmalarms.get(MAP_KEY_NEXT_DRINK);
				final ArrayList<ModelAlarm> sitAlarm = hmalarms.get(MAP_KEY_DRINKS);
				//boolean hasNextAlarm = false;
				Log.e("mxy","nextDrinkalarm " +nextSitalarm+"currTime " +currTime);
				if(nextSitalarm!=null && nextSitalarm.size()>0)
					{
						return;
					}
				else
					{
						if(sitAlarm!=null && sitAlarm.size()>0)
							{
							   boolean hasNextDrink = false;
				               boolean hasNextSit = false;
							   long nextTime = 0; 
							   long tempTime;
							   ModelAlarm alarm =new ModelAlarm();
								for(int i=0;i<sitAlarm.size();i++)
									{

												if(sitAlarm.get(i).isRepeat(currTime,currWekkday))
													{
														//int[] weeks = new int[7];
														//nextSitAlarmTemp.add(sitAlarm.get(i));
														int repeatDay =sitAlarm.get(i).getRepeatDay();
														long time = sitAlarm.get(i).getTimeInDay();
														int nearWeekDay =getRepeaWeekDay(repeatDay,currWekkday,currTime,time);
														Log.e("mxy","nearWeekDay " +nearWeekDay);
														fmt.applyPattern("HH:mm");
														Date d = null;
														try {
															d = fmt.parse(DateFormatUtil.getTimeString(DateFormatUtil.HM,
time));
														} catch (ParseException e) {
															// TODO Auto-generated catch block
															e.printStackTrace();
														}
														calendar.add(Calendar.DATE,nearWeekDay);
														calendar.set(Calendar.HOUR_OF_DAY, d.getHours());
														calendar.set(Calendar.MINUTE, d.getMinutes());
														calendar.set(Calendar.SECOND, 0);
														calendar.set(Calendar.MILLISECOND, 0);
														//calendar.set(Calendar.DAY_OF_WEEK, nearWeekDay);
														tempTime=calendar.getTimeInMillis();
														Log.e("mxy","tempTime  " +tempTime);
														if(nearWeekDay!=0)
															{
																if(nextTime==0)
																	{
																		nextTime = tempTime;
																		alarm = sitAlarm.get(i);	
																	}
																if(nextTime>tempTime)
																	{
																		nextTime=tempTime;
																		alarm = sitAlarm.get(i);
																	}
															}
														Log.e("mxy","nearWeekDay alarm  " +alarm);
													}
									
									}
								nextSitAlarmTemp.add(alarm);
								hmalarms.put(MAP_KEY_NEXT_DRINK, nextSitAlarmTemp);
							}							
					}
			}
		@Override
		protected void onPostExecute(HashMap<String, ArrayList<ModelAlarm>> result) {
			// TODO Auto-generated method stub
			if(isCancelled()){
				return;
			}

			alarmChanged(result);
			MxyLog.e("mxy", "onPostExecute alarmChanged  result= " + result);
			if((mListens!=null)&&(mListens.size()!=0))
			{
				if(mListens.get(0)!=null)
				{
					mListens.get(0).onHealthAlarmChanged();
				}
			}
			else
				{
					setHealthAlarm(LauncherApp.appContext);
				}
		}
	}
	
	public interface IHealthAlarmListen{
		public void onHealthAlarmChanged();
	}

	public class HealthAlarmReceiver extends AbsBroadcastReceiver{
		
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			// step and sleep
			
			final String action = intent.getAction();
			Log.e("mxy","onReceive intent action = "+action);
			if(ACTION_HEALTHALARM_DRINK.equals(action)){
				Log.e("mxy","onReceive drink alarm ");
				Intent alaramIntent = new Intent(context, AlarmReceiveActivity.class);
			    alaramIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			    context.startActivity(alaramIntent);
				
				startGetAlarm();
				//setDrinkAlarm(context);
				return;
			}
		    if(ACTION_HEALTHALARM_SIT.equals(action)){
				
				Log.e("mxy","onReceive sit alarm  ");
			    Intent alaramIntent = new Intent(context, AlarmReceiveActivity.class);
			    alaramIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			    context.startActivity(alaramIntent);
			    
				startGetAlarm();
				//setSitAlarm(context);
				return;
			}
		}

		@Override
		public IntentFilter getIntentFilter() {
			// TODO Auto-generated method stub
			IntentFilter filter = new IntentFilter();
			filter.addAction(ACTION_HEALTHALARM_DRINK);
			filter.addAction(ACTION_HEALTHALARM_SIT);
			return filter;
		}
	}
	
	 public void showProgressDialog(String title, String message,Context context) {
		  Log.e("mxy","showProgressDialog progressDialog"+progressDialog);
		  if (progressDialog == null) {
			  if(!((Activity) context).isFinishing())
		  		{
				   	 progressDialog = ProgressDialog.show(context, title,
				     message, true, false);
		  		}
			else
				{
					return;
				}
		  } else if (progressDialog.isShowing()) {
		   progressDialog.setTitle(title);
		   progressDialog.setMessage(message);
		  }
			 Thread thread = new Thread() {
				 public void run() {
					 try {
						 sleep(6000);
					 } catch (InterruptedException e) {
					 // 
					 e.printStackTrace();
					 }
					 //progressDialog.dismiss();//
					 hideProgressDialog();
					 }
				 };
				 Log.e("mxy","showProgressDialog progressDialog"+progressDialog);
				 progressDialog.show();
		       //  thread.start();
		 }
		 
		 /*
		  * 
		  */
		 public void hideProgressDialog() {
		 
		  if (progressDialog != null && progressDialog.isShowing()) {
		   progressDialog.dismiss();
		  }
		 
		 }
}

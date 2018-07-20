package com.sczn.wearlauncher.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import android.content.Context;
import android.util.Log;

import com.sczn.wearlauncher.R;

public class DateFormatUtil {
	
	public static SimpleDateFormat YYYY_MM_DD = new SimpleDateFormat("yyyy-MM-dd",Locale.getDefault());
	public static SimpleDateFormat HM= new SimpleDateFormat("HH:mm",Locale.getDefault());
	public static SimpleDateFormat HMS= new SimpleDateFormat("HH:mm:ss",Locale.getDefault());
	public static SimpleDateFormat YYYY_MM_DD_HM = new SimpleDateFormat("yy/MM/dd HH:mm",Locale.getDefault());
	
	public static String FORMAT1 = "%1$02d:%2$02d";
	
	
	public static String getCurrTimeString(SimpleDateFormat format){
        Calendar cal;
        cal = Calendar.getInstance();
        cal.setTimeZone(TimeZone.getTimeZone(SystemUtils.getCurrentTimeZone()));
        //TimeZone tz = TimeZone.getTimeZone("GMT");
		return getTimeString(format, cal.getTimeInMillis());
	}
	
	public static String getTimeString(SimpleDateFormat format,long time){
		Date date =new Date(time);
		format.setTimeZone(TimeZone.getTimeZone(SystemUtils.getCurrentTimeZone()));
		return format.format(date);
	}

	public static String getDayTimeString(long ms){

	         int ss = 1000;  
             int mi = ss * 60;  
             int hh = mi * 60;  
             //int dd = hh * 24;  

             //long day = ms / dd;  
             long hour = ms / hh;  
             long minute = (ms - hour * hh) / mi;  
             long second = (ms -hour * hh - minute * mi) / ss;  
            // long milliSecond = ms - day * dd - hour * hh - minute * mi - second * ss;  

             //String strDay = day < 10 ? "0" + day : "" + day; //��  
             String strHour = hour < 10 ? "0" + hour : "" + hour;//Сʱ  
             String strMinute = minute < 10 ? "0" + minute : "" + minute;//����  
             String strSecond = second < 10 ? "0" + second : "" + second;//��  
             //String strMilliSecond = milliSecond < 10 ? "0" + milliSecond : "" + milliSecond;//����  
            // strMilliSecond = milliSecond < 100 ? "0" + strMilliSecond : "" + strMilliSecond;  

             return strHour + ":" + strMinute;
		}

	   /**
     * ��ʱ��(����)����ȡ��ʱ��(ʱ:��)
     * ʱ���ʽ:  ʱ:��
     *
     * @param millisecond
     * @return
     */
    public static String getTimeFromMillisecond(Long millisecond) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
        Date date = new Date(millisecond);
        String timeStr = simpleDateFormat.format(date);
        return timeStr;
    }

    /**
     * ������ת���ɹ̶���ʽ��ʱ��
     * ʱ���ʽ: yyyy-MM-dd HH:mm:ss
     *
     * @param millisecond
     * @return
     */
    public static String getDateTimeFromMillisecond(Long millisecond){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(millisecond);
        String dateStr = simpleDateFormat.format(date);
        return dateStr;
    }
	public static int getCurrWeekDayRes(){
		switch (Calendar.getInstance().get(Calendar.DAY_OF_WEEK)) {
			case Calendar.MONDAY:
				return R.string.weekday_mon;
			case Calendar.TUESDAY:
				return R.string.weekday_tue;
			case Calendar.WEDNESDAY:
				return R.string.weekday_wed;
			case Calendar.THURSDAY:
				return R.string.weekday_thu;
			case Calendar.FRIDAY:
				return R.string.weekday_fri;
			case Calendar.SATURDAY:
				return R.string.weekday_sat;
			default:
				return R.string.weekday_sun;
		}
	}

	public static int getCurrWeekDayRes(int week){
		switch (week) {
			case Calendar.MONDAY:
				return R.string.short_weekday_mon;
			case Calendar.TUESDAY:
				return R.string.short_weekday_tue;
			case Calendar.WEDNESDAY:
				return R.string.short_weekday_wed;
			case Calendar.THURSDAY:
				return R.string.short_weekday_thu;
			case Calendar.FRIDAY:
				return R.string.short_weekday_fri;
			case Calendar.SATURDAY:
				return R.string.short_weekday_sat;
			default:
				return R.string.short_weekday_sun;
		}
	}

	public static  String getRepeatWeekString(Context context,int repeatDay)
		{
			int[] weeks=null;
			weeks =new int[7];
			int k=0;
			int nearweek=7;
			int temp=0 ;
			int weekDay=0;
			String week="";
			for(int i=0;i<7;i++)
				{
					
					if(((repeatDay & 1<<(i+1))>>(i+1))==1)
						{
							weeks[k]=i+1;
							week =week+context.getString(getCurrWeekDayRes(i+1));
						}
				}
			Log.e("mxy"," getRepeatWeekString week"+ week);
			return week;
		}
}

package com.sczn.wearlauncher.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import com.sczn.wearlauncher.R;

public class DateFormatUtil {
	
	public static SimpleDateFormat YYYY_MM_DD = new SimpleDateFormat("yyyy-MM-dd",Locale.getDefault());
	public static SimpleDateFormat HM= new SimpleDateFormat("HH:mm",Locale.getDefault());
	public static SimpleDateFormat HMS= new SimpleDateFormat("HH:mm:ss",Locale.getDefault());
	public static SimpleDateFormat YYYY_MM_DD_HM = new SimpleDateFormat("yy/MM/dd HH:mm",Locale.getDefault());
	
	public static String FORMAT1 = "%1$02d:%2$02d";
	
	
	public static String getCurrTimeString(SimpleDateFormat format){
		return getTimeString(format, Calendar.getInstance().getTimeInMillis());
	}
	
	public static String getTimeString(SimpleDateFormat format,long time){
		return format.format(new Date(time));
	}
	
	public static int getCurrWeekDayRes(){
		switch (Calendar.getInstance().get(Calendar.WEEK_OF_MONTH)) {
			case Calendar.MONDAY:
				return R.string.weekday_mon;
			case Calendar.TUESDAY:
				return R.string.weekday_tue;
			case Calendar.WEDNESDAY:
				return R.string.weekday_wed;
			case Calendar.THURSDAY:
				return R.string.weekday_tue;
			case Calendar.FRIDAY:
				return R.string.weekday_fri;
			case Calendar.SATURDAY:
				return R.string.weekday_sat;
			default:
				return R.string.weekday_sun;
		}
	}
}

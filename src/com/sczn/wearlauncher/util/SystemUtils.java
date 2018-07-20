package com.sczn.wearlauncher.util;

import java.util.Locale;
import java.util.TimeZone;

import android.content.Context;

public class SystemUtils {
	/**
	 * 时区语言工具类
	 * @author Xiho
	 *
	 */

	    /**
	     * 获取当前时区
	     * @return
	     */
	    public static String getCurrentTimeZone() {
	        TimeZone tz = TimeZone.getDefault();
	        String strTz = tz.getDisplayName(false, TimeZone.SHORT);
	        return strTz;

	    }


	    /**
	     * 获取当前系统语言格式
	     * @param mContext
	     * @return
	     */
	    public static String getCurrentLanguage(Context mContext){
	        Locale locale =mContext.getResources().getConfiguration().locale;
	        String language=locale.getLanguage();
	        String country = locale.getCountry();
	        String lc=language+"_"+country;
	        return lc;
	    }

}

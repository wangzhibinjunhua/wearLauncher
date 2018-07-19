package com.sczn.wearlauncher.model;

import com.sczn.wearlauncher.util.DateFormatUtil;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 接收手机推�?�过来的内容
 * Created by mxy on 2016/3/30.
 */
public class PhoneMessage implements Parcelable {
    private String packageName;
    private String tickerText;
    private String id;
    private String time;
    private String appName;


//    public PhoneMessage(String packageName, String tickerText, String id, String time, String appName) {
//        this.packageName = packageName;
//        this.tickerText = tickerText;
//        this.id = id;
//        this.time = time;
//        this.appName = appName;
//    }
    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getTickerText() {
    	if(tickerText.isEmpty()){
    		return "--";
    	}
        return tickerText;
    }

    public void setTickerText(String tickerText) {
        this.tickerText = tickerText;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTime() {
    	try {
			return DateFormatUtil.getTimeString(DateFormatUtil.HM, Long.parseLong(time));
		} catch (Exception e) {
			// TODO: handle exception
		}
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(packageName);
        dest.writeString(tickerText);
        dest.writeString(id);
        dest.writeString(time);
        dest.writeString(appName);
    }

    public static final Parcelable.Creator<PhoneMessage> CREATOR = new Creator<PhoneMessage>() {
        @Override
        public PhoneMessage createFromParcel(Parcel source) {
            PhoneMessage phoneMessage = new PhoneMessage();
            phoneMessage.packageName = source.readString();
            phoneMessage.tickerText = source.readString();
            phoneMessage.id = source.readString();
            phoneMessage.time = source.readString();
            phoneMessage.appName = source.readString();
            return phoneMessage;
        }

        public PhoneMessage[] newArray(int size) {
            return new PhoneMessage[size];
        }
    };
}

package com.sczn.wearlauncher.card.healthalarm;

import java.util.Calendar;

import com.sczn.wearlauncher.model.PhoneMessage;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class ModelAlarm implements Parcelable{
	
	public static final int REPEAT_FLAG_ENABLE = 1;
	public static final int REPEAT_FLAG_SUNDAY = 1 << Calendar.SUNDAY;
	public static final int REPEAT_FLAG_MONDAY = 1 << Calendar.MONDAY;
	public static final int REPEAT_FLAG_TUESDAY = 1 << Calendar.TUESDAY;
	public static final int REPEAT_FLAG_WEDNESDAY = 1 << Calendar.WEDNESDAY;
	public static final int REPEAT_FLAG_THURSDAY = 1 << Calendar.THURSDAY;
	public static final int REPEAT_FLAG_FRIDAY  = 1 << Calendar.FRIDAY;
	public static final int REPEAT_FLAG_SATURDAY = 1 << Calendar.SATURDAY;
	

	public static final int ALARM_TYPE_SIT = 0;
	public static final int ALARM_TYPE_DRINK = 1;

	private int ID;
	private long timeInDay;
	private int repeatDay;
	private int type;
	private boolean enable;

	public int getID() {
		return ID;
	}
	public void setID(int iD) {
		ID = iD;
	}
	public long getTimeInDay() {
		return timeInDay;
	}
	public void setTimeInDay(long timeInDay) {
		this.timeInDay = timeInDay;
	}
	public int getRepeatDay() {
		return repeatDay;
	}
	public void setRepeatDay(int repeatDay) {
		this.repeatDay = repeatDay;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public boolean isEnable() {
		return enable;
	}
	public void setEnable(boolean enable) {
		this.enable = enable;
	}
	
	public boolean isValue(long currTime, int dayFlag){
		if(!enable){
			return false;
		}
		if(timeInDay <= currTime){
			return false;
		}
		if((repeatDay & REPEAT_FLAG_ENABLE) == 0){
			return true;
		}
		
		return (repeatDay & dayFlag) != 0;
	}
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeInt(ID);
		dest.writeLong(timeInDay);
		dest.writeInt(repeatDay);
		dest.writeInt(type);
		dest.writeInt(enable ? 1 :0);
	}
	
	public static final Parcelable.Creator<ModelAlarm> CREATOR = new Creator<ModelAlarm>() {
        @Override
        public ModelAlarm createFromParcel(Parcel source) {
        	ModelAlarm alarm = new ModelAlarm();
        	alarm.ID = source.readInt();
            alarm.timeInDay = source.readLong();
            alarm.repeatDay = source.readInt();
            alarm.type = source.readInt();
            alarm.enable = source.readInt() > 0;
            
            return alarm;
        }

        public ModelAlarm[] newArray(int size) {
            return new ModelAlarm[size];
        }
    };
}

package com.sczn.wearlauncher.db.bean;

import android.graphics.RectF;
import android.os.Parcel;
import android.os.Parcelable;

public class SleepState implements Parcelable{
	public static final long DURATION_IN_MILLLI = 12*60*60*1000;
	public static final long PER_HOUR = 60*60*1000;
	
		public static final int STATE_DEEP_SLEEP = 0;
		public static final int STATE_LIGHT_SLEEP = 1;
		
		public static long deepSleepTime;
		public static long lightSleepTime;
		private long startOffset;
		private long endOffset;
		private int state;
		
		public void setState(int state) {
			this.state = state;
		}

		public int getState() {
			return state;
		}
		
		public long getStartOffset() {
			return startOffset;
		}

		public void setStartOffset(long startOffset) {
			this.startOffset = startOffset > 0? startOffset : 0;
		}

		public long getEndOffset() {
			return endOffset = endOffset < DURATION_IN_MILLLI ? endOffset : DURATION_IN_MILLLI;
		}

		public void setEndOffset(long endOffset) {
			this.endOffset = endOffset;
		}

		public RectF getRectF(float width, float height, float paddingX, float paddingY){
			//MxyLog.d(this, "getRectF" + "width =" + width + "--startOffset=" + startOffset + "--width*startOffset/DURATION_IN_MILLLI="  + width*startOffset/DURATION_IN_MILLLI);
			return new RectF(paddingX + width*startOffset/DURATION_IN_MILLLI,
					STATE_DEEP_SLEEP == state? (paddingY) : (paddingY + height/2),
					paddingX + width*endOffset/DURATION_IN_MILLLI,
					paddingY + height);
		}

		@Override
		public int describeContents() {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public void writeToParcel(Parcel dest, int flags) {
			// TODO Auto-generated method stub
			dest.writeLong(startOffset);
			dest.writeLong(endOffset);
			dest.writeInt(state);
		}

		public static final Creator<SleepState> CREATOR = new Creator<SleepState>() {
			public SleepState createFromParcel(Parcel source) {
				final SleepState state = new SleepState();
				state.setStartOffset(source.readLong());
				state.setEndOffset(source.readLong());
				state.setState(source.readInt());
			    return state;
			}
			public SleepState[] newArray(int size) {
			    return new SleepState[size];
			}
		};
		
}
package com.sczn.wearlauncher.card.healthalarm;

import java.util.ArrayList;

import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sczn.wearlauncher.R;
import com.sczn.wearlauncher.model.AppMenu;
import com.sczn.wearlauncher.util.MxyToast;
import com.sczn.wearlauncher.view.ScrollerTextView;

public class AdapterAlarm extends Adapter<AdapterAlarm.HealthAlarmHolder> implements OnClickListener{

	private ArrayList<ModelAlarm> mAlarms;
	private IAlarmClickLiten mAlarmClickLiten;
	
	public AdapterAlarm(){
		mAlarms = new ArrayList<ModelAlarm>();
	}
	
	public void setOnClickListen(IAlarmClickLiten listen){
		this.mAlarmClickLiten = listen;
	}
	public void setData(ArrayList<ModelAlarm> alarms){
		mAlarms.clear();
		if(alarms != null){
			mAlarms.addAll(alarms);
		}
		notifyDataSetChanged();
	}
	
	@Override
	public int getItemCount() {
		// TODO Auto-generated method stub
		return mAlarms.size();
	}

	@Override
	public void onBindViewHolder(HealthAlarmHolder arg0, int arg1) {
		// TODO Auto-generated method stub
		if(arg1 >= mAlarms.size()){
			return;
		}
		ModelAlarm alarm = mAlarms.get(arg1);
		
		arg0.mTime.setText(Long.toString(alarm.getTimeInDay()));
		arg0.mRepeat.setText(Integer.toString(alarm.getRepeatDay()));
		
		arg0.mDelete.setTag(Integer.valueOf(arg1));
		arg0.mContent.setTag(alarm);
		arg0.mDelete.setOnClickListener(this);
		arg0.mContent.setOnClickListener(this);
	}

	@Override
	public HealthAlarmHolder onCreateViewHolder(ViewGroup arg0, int arg1) {
		// TODO Auto-generated method stub
		final ListItemAlarm item = (ListItemAlarm) LayoutInflater.from(
				arg0.getContext()).inflate(R.layout.list_item_healthalarm,arg0, false);
		return new HealthAlarmHolder(item, arg0.getMeasuredWidth());
	}
	
	public class HealthAlarmHolder extends ViewHolder{
	
		private TextView mDelete;
		private View mContent;
		private TextView mTime;
		private TextView  mRepeat;
		
		public HealthAlarmHolder(View arg0, int contentWidth) {
			super(arg0);
			
			final View scrollerContent = arg0.findViewById(R.id.scroller_content);
			scrollerContent.getLayoutParams().width = contentWidth - arg0.getPaddingLeft() - arg0.getPaddingRight();
			
			mDelete = (TextView) arg0.findViewById(R.id.alarm_delete);
			mContent = arg0.findViewById(R.id.alarm_content);
			mTime = (TextView) arg0.findViewById(R.id.alarm_time);
			mRepeat = (TextView) arg0.findViewById(R.id.alarm_repeat);
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		//MxyToast.showShort(v.getContext(), "on click");
		if(mAlarmClickLiten == null){
			return;
		}
		switch (v.getId()) {
			case R.id.alarm_delete:
				final int position = ((Integer) v.getTag()).intValue();
				if(position <= mAlarms.size()){
					mAlarmClickLiten.onDeleteClick(position, mAlarms.get(position));
				}
				break;
			case R.id.alarm_content:
				mAlarmClickLiten.onAlarmClick((ModelAlarm) v.getTag());
				break;
			default:
				break;
		}
	}
	
	public interface IAlarmClickLiten{
		public void onDeleteClick(int position,ModelAlarm alarm);
		public void onAlarmClick(ModelAlarm alarm);
	}
}

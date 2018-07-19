package com.sczn.wearlauncher.adapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import com.sczn.wearlauncher.R;
import com.sczn.wearlauncher.util.MxyLog;

import android.content.Context;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.TextView;


public class LetterIndexAdapter extends BaseAdapter {
	
	public static final int ITEM_TYPE_NORMAL = 0;
	public static final int ITEM_TYPE_EMPTY = 1;

	
	private  int SHOW_LETTER_COUNT = 5;
	private  int HEADER_COUNT = SHOW_LETTER_COUNT/2;
	private  int FOOTER_COUNT = SHOW_LETTER_COUNT/2;

	private Context mContext;
	private int LetterSizeNormal;
	private int LetterSizeSelected;
	private int letterColorNormal;
	private int letterColorSelected;
	private int mletterHeight = 30;
	private int mSelectedLetter = HEADER_COUNT;
	
	private boolean needLoop = false;
	private boolean isLoop = false;
	private List<CharSequence> mLetters;
	
	public LetterIndexAdapter(Context context, boolean needLoop, int showCount){
		this.mContext = context;
		this.needLoop = needLoop;
		
		LetterSizeNormal = context.getResources().getDimensionPixelSize(R.dimen.healthalarm_timer_pick_normal);
		LetterSizeSelected = context.getResources().getDimensionPixelSize(R.dimen.healthalarm_timer_pick_selected);
		letterColorNormal = context.getResources().getColor(R.color.healthalarm_timer_picker_normal);
		letterColorSelected = context.getResources().getColor(R.color.healthalarm_timer_picker_selected);
		
		SHOW_LETTER_COUNT = showCount;
		HEADER_COUNT = SHOW_LETTER_COUNT/2;
		FOOTER_COUNT = SHOW_LETTER_COUNT/2;
		
	}
	
	public int getShowCount(){
		return SHOW_LETTER_COUNT;
	}
	
	public int getHeaderCount(){
		if(isLoop){
			return 0;
		}
		return HEADER_COUNT;
	}
	
	public int getCenterIndex(){
		return HEADER_COUNT;
	}
	
	public int getLetterPosition(CharSequence letter){
		//MxyLog.i(this, "getLetterPosition--" + letter + "="  +mLetters.indexOf(letter));
		final int position = mLetters.indexOf(letter);
		if(isLoop){
			return position == -1 ? -1 :  mLetters.size() + position;
		}
		return position == -1 ? -1 : getHeaderCount() + position;
	}
	
	public boolean isNeedLoop() {
		return needLoop;
	}

	public boolean isLoop() {
		return isLoop;
	}
	
	public void setLetters(CharSequence[] letters){
		if(mLetters == null){
			mLetters = new ArrayList<CharSequence>();
		}
		mLetters.clear();
		if(letters == null){
			return;
		}
		mLetters.addAll(Arrays.asList(letters));
		
		if(needLoop && mLetters.size() > 3){
			
			isLoop = true;
		}else{
			isLoop = false;
		}
		notifyDataSetChanged();
	}
	
	public List<CharSequence> getLetters(){
		return mLetters;
	}
	
	
	public void initStyle(int sn, int sc, int cn, int cs){
		LetterSizeNormal = sn;
		LetterSizeSelected = sc;
		letterColorNormal = cn;
		letterColorSelected = cs;
	}
	

	public void setItemHeight(int height){
		this.mletterHeight = height;
	}
	public int getItenHeight(){
		return mletterHeight;
	}
	
	public void setSelectedPosition(int position){
		
		if(isLoop){
			position += mLetters.size();
		}else if(ITEM_TYPE_EMPTY == getItemViewType(position)){
			return;
		}else{
			position -= HEADER_COUNT;
		}
		
		if(mSelectedLetter != position){
			mSelectedLetter = position;
			if(mIndexSelected != null){
				mIndexSelected.OnIndexSelected(mLetters.get(position % mLetters.size()));
			}
		}
	}
	
	public int getSelectedLetter(){
		return mSelectedLetter;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if(isLoop){
			return mLetters.size() * 3;
		}
		return mLetters.size() + HEADER_COUNT + FOOTER_COUNT;
	}

	@Override
	public String getItem(int position) {
		// TODO Auto-generated method stub
		if(isLoop()){
			return mLetters.get(position%mLetters.size()).toString();
		}
		if(position < HEADER_COUNT || position >= (getCount() - FOOTER_COUNT)){
			return "";
		}
		return mLetters.get(position - HEADER_COUNT).toString();
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}
	
	
	@Override
	public int getItemViewType(int position) {
		// TODO Auto-generated method stub
		if(isLoop){
			return ITEM_TYPE_NORMAL;
		}
		
		if(position < HEADER_COUNT || position >= (getCount() - FOOTER_COUNT)){
			return ITEM_TYPE_EMPTY;
		}
		return ITEM_TYPE_NORMAL;
	}
	
	@Override
	public boolean isEnabled(int position) {
		// TODO Auto-generated method stub
		return ITEM_TYPE_EMPTY != getItemViewType(position);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		final ViewHolder holder;
		if(convertView == null){
			holder = new ViewHolder();
			convertView = holder.mLetter = new LetterText(mContext);
			holder.mLetter.setLayoutParams(new AbsListView.LayoutParams(LayoutParams.MATCH_PARENT,
					mletterHeight));
			
			holder.mLetter.setCenter();
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		holder.mLetter.setText(getItem(position));
		return convertView;
	}
	
	public class ViewHolder{
		public LetterText mLetter;
	}
	
	public class LetterText extends TextView{

		public LetterText(Context context) {
			super(context);
			// TODO Auto-generated constructor stub
			setBackgroundColor(Color.TRANSPARENT);
			getPaint().setFakeBoldText(true);
			setTextSize(TypedValue.COMPLEX_UNIT_PX, LetterSizeNormal);
			setTextColor(letterColorNormal);
		}
		
		public void setCenter(){
			setGravity(Gravity.CENTER);
		}
		
		public void setState(boolean selected){
			if(selected){
				setTextSize(TypedValue.COMPLEX_UNIT_PX, LetterSizeSelected);
				setTextColor(letterColorSelected);
				//setBackgroundColor(Color.RED);
			}else{
				setTextSize(TypedValue.COMPLEX_UNIT_PX, LetterSizeNormal);
				setTextColor(letterColorNormal);
			}
		}
	}
	
	private IIndexSelected mIndexSelected;
	public void setIndexSelected(IIndexSelected indexSelected){
		this.mIndexSelected = indexSelected;
	}
	public interface IIndexSelected{
		public void OnIndexSelected(CharSequence letter);
	}
}

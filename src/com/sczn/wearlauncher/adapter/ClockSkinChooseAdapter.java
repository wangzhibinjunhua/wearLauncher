package com.sczn.wearlauncher.adapter;

import java.util.ArrayList;
import java.util.List;

import com.sczn.wearlauncher.R;
import com.sczn.wearlauncher.util.ClockSkinUtil;
import com.sczn.wearlauncher.util.MxyLog;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.LayoutParams;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

public class ClockSkinChooseAdapter extends Adapter<ClockSkinChooseAdapter.ClockSkinHolder>{
	private static final String TAG = ClockSkinChooseAdapter.class.getSimpleName();
	
	private static final int ITEM_TYPE_NORMAL = 0;
	private static final int ITEM_TYPE_HEADER = 1;
	private static final int ITEM_TYPE_FOOTER = 2;
	
	private final int weightAll;
	private final int weightIem;
	private final int weightIemGap;
	private final int weightBoundGap;
	
	private Context mContext;
	private List<String> clockSkinFiles;
	private IClockSkinChooseClick mIClockSkinChooseClick;
	
	public ClockSkinChooseAdapter(Context mContext) {
		super();
		this.mContext = mContext;
		clockSkinFiles = new ArrayList<String>();
		
		weightAll = mContext.getResources().getInteger(
				R.integer.item_clock_skin_choose_all);
		weightIem = mContext.getResources().getInteger(
				R.integer.item_clock_skin_choose_item);
		weightIemGap = mContext.getResources().getInteger(
				R.integer.item_clock_skin_choose_itemGap);
		weightBoundGap = mContext.getResources().getInteger(
				R.integer.item_clock_skin_choose_boundGap);
		
	}
	
	public int getBoundGap(int sizeAll){
		return sizeAll*weightBoundGap/weightAll;
	}
	
	public void setOnItemClickListen(IClockSkinChooseClick listen){
		this.mIClockSkinChooseClick = listen;
	}
	
	@Override
	public void onAttachedToRecyclerView(RecyclerView recyclerView) {
		// TODO Auto-generated method stub
		super.onAttachedToRecyclerView(recyclerView);
	}

	@Override
	public int getItemCount() {
		// TODO Auto-generated method stub
		return clockSkinFiles.size();
	}
	
	public void setClockSkinFiles(String[] files){
		clockSkinFiles.clear();
		if(files == null){
			notifyDataSetChanged();
			return;
		}
		for(int i = 0; i < files.length; i ++){
			clockSkinFiles.add(files[i]);
		}
		notifyDataSetChanged();
	}
	
	@Override
	public int getItemViewType(int position) {
		// TODO Auto-generated method stub
		if(0 == position){
			return ITEM_TYPE_HEADER;
		}
		if(position == getItemCount() - 1){
			return ITEM_TYPE_FOOTER;
		}
		return ITEM_TYPE_NORMAL;
	}

	@Override
	public void onBindViewHolder(ClockSkinHolder arg0, int arg1) {
		// TODO Auto-generated method stub
		if(arg1 < getItemCount()){
			arg0.mClockSkinModle.setImageDrawable(ClockSkinUtil.
					getClockSkinModelByName(mContext, clockSkinFiles.get(arg1)));
			arg0.mClockSkinModle.setOnClickListener(new OnItemClick(arg1));
		}
	}

	@Override
	public ClockSkinHolder onCreateViewHolder(ViewGroup arg0, int arg1) {
		// TODO Auto-generated method stub
		final View view = LayoutInflater.from(arg0.getContext()).inflate(R.layout.item_clock_skin_choose,
				arg0, false);
		final int sizeAll = Math.min(arg0.getHeight(), arg0.getWidth());
		final int sizeItem = sizeAll * weightIem/weightAll;
		final int sizeGapItem = sizeAll * weightIemGap/weightAll;
		final int sizeGapBound = sizeAll * weightBoundGap/weightAll;
		RecyclerView.LayoutParams params =  new LayoutParams(arg0.getLayoutParams());
		switch (arg1) {
			case ITEM_TYPE_HEADER:
				params.setMargins(sizeGapBound, sizeGapItem, sizeGapItem, sizeGapItem);
				break;
			case ITEM_TYPE_FOOTER:
				params.setMargins(sizeGapItem, sizeGapItem, sizeGapBound, sizeGapItem);
				break;
			default:
				params.setMargins(sizeGapItem, sizeGapItem, sizeGapItem, sizeGapItem);
				break;
		}
		view.setLayoutParams(params);
		return new ClockSkinHolder(view, sizeItem);
	}
	
	public class ClockSkinHolder extends ViewHolder{
		private ImageView mClockSkinModle;
		
		public ClockSkinHolder(View arg0, int sizeItem) {
			super(arg0);
			mClockSkinModle = (ImageView) arg0.findViewById(R.id.clock_skin_choose);
			mClockSkinModle.getLayoutParams().width = 
					mClockSkinModle.getLayoutParams().height = sizeItem;
		}

	}
	private class OnItemClick implements OnClickListener{
		private int position;
		
		public OnItemClick(int position) {
			super();
			this.position = position;
		}

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if(mIClockSkinChooseClick != null){
				mIClockSkinChooseClick.onItemClick(v, position);
			}
		}
		
	}
	
	public interface IClockSkinChooseClick{
		public void onItemClick(View view, int position);
	}
}

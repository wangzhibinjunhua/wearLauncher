package com.sczn.wearlauncher.view.status;

import com.sczn.wearlauncher.R;
import com.sczn.wearlauncher.util.MxyLog;
import com.sczn.wearlauncher.view.ClickIcon;
import com.sczn.wearlauncher.view.ScrollerTextView;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;

public class StatusIconWithText extends LinearLayout {

	protected ClickIcon mIcon;
	protected TextView mText;
	private int StringId;
	private int ImageId;
	
	private float TextSize;
	private int TextPadding;
	
	public StatusIconWithText(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		setOrientation(VERTICAL);
		setGravity(Gravity.CENTER);
	
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.StatusIcon);
		
		TextSize = a.getDimensionPixelSize(R.styleable.StatusIcon_textsize, getResources().getDimensionPixelSize(R.dimen.text_size_status));
		TextPadding = a.getDimensionPixelSize(R.styleable.StatusIcon_textpadding, getResources().getDimensionPixelSize(R.dimen.status_icon_text_padding));
		StringId = a.getResourceId(R.styleable.StatusIcon_stringId, R.string.Unkown);
		ImageId = a.getResourceId(R.styleable.StatusIcon_imageId, R.drawable.statu_icon_setting);
		a.recycle();
		
		mIcon = getIcon();
		mText = getTextView();
	}
	
	protected void onLocalChanged(){
		mText.setText(StringId);
	}
	
	protected TextView getTextView(){
		if(mText == null){
			mText = new ScrollerTextView(getContext());
			mText.setText(StringId);
			mText.setTextSize(TextSize);
			mText.setPadding(0, TextPadding, 0, 0);
			LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			
			addView(mText, params);
		}
		return mText;
	}
	protected ClickIcon getIcon(){
		if(mIcon == null){
			mIcon = new ClickIcon(getContext());
			mIcon.setImageResource(ImageId);
			mIcon.setScaleType(ScaleType.CENTER_INSIDE);
			LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			mIcon.setLayoutParams(params);
			addView(mIcon, params);
		}
		return mIcon;
	}
	
	@Override
	public void setOnClickListener(OnClickListener l) {
		// TODO Auto-generated method stub
		mIcon.setOnClickListener(l);
	}
	
	public void startFresh() {
		// TODO Auto-generated method stub
	}
	public void stopFresh() {
		// TODO Auto-generated method stub
	}
}

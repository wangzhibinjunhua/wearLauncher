package com.sczn.wearlauncher.view;

import com.sczn.wearlauncher.LauncherApp;
import com.sczn.wearlauncher.R;
import com.sczn.wearlauncher.util.MxyLog;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.TextView;

public class LanTingTextView extends ScrollerTextView {
	private final static String TAG = LanTingTextView.class.getSimpleName();
	
	//public static final int LANTIN_STYLE_MIDDLE = R.string.lantin_middle;
	//public static final int LANTIN_STYLE_SMALL = R.string.lantin_small;

	public LanTingTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		//TypedArray a =  context.obtainStyledAttributes(null, null);
		//MxyLog.d(TAG, "LanTinTextView_style=" + a.getString(R.styleable.LanTinTextView_style));
		//a.recycle();
		init(context);
	}
	
	private void init(Context context){
		setTypeface(LauncherApp.lanTingBoldBlackTypeface);
	}
}

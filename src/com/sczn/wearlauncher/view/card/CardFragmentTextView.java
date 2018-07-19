package com.sczn.wearlauncher.view.card;

import com.sczn.wearlauncher.LauncherApp;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

public class CardFragmentTextView extends TextView{

	public CardFragmentTextView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		init();
	}
	
	public CardFragmentTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		init();
	}
	
	public CardFragmentTextView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		init();
	}
	
	private void init(){
		setTypeface(LauncherApp.cardFragmentTypeface);
	}

}

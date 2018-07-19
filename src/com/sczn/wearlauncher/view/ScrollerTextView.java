package com.sczn.wearlauncher.view;

import com.sczn.wearlauncher.util.MxyLog;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.TextView;

/**
 * Created by mengxiaoyu on 2016/10/25.
 */
public class ScrollerTextView extends TextView {
	
	private boolean needFit = true;
	
	public ScrollerTextView(Context context) {
		this(context, null);
		// TODO Auto-generated constructor stub
	}

	public ScrollerTextView(Context context, AttributeSet attrs) {
        super(context, attrs);

        initView();
    }
	
    protected void initView(){
        setEllipsize(TextUtils.TruncateAt.MARQUEE);
        setSingleLine(true);
        setMarqueeRepeatLimit(-1);
        
        setFocusable(true);
        setFocusableInTouchMode(false);
    }
	
    @Override
    public boolean isFocused() {
    	//MxyLog.d(this, "isFocused");
    	if(isInTouchMode()){
    		//return false;
    	}
        return isAttachedToWindow();
    }
    
    @Override
    protected void onDraw(Canvas canvas) {
    	// TODO Auto-generated method stub
    	//autoFit();
    	//MxyLog.d(this, "onDraw--" + getText());
    	super.onDraw(canvas);
    }
    
    private synchronized void autoFit(){
    	if(!needFit){
    		return;
    	}
    	float[] charsWidthArr = new float[getText().toString().length()];  
        Rect boundsRect = new Rect();  
        final int availableTextViewHeight = getHeight() - getPaddingTop() - getPaddingBottom(); 
        getPaint().getTextBounds(getText().toString(), 0, getText().length(), boundsRect);  
        int textHeight = boundsRect.height();  
        float textSize = getTextSize();  
        while (textHeight > availableTextViewHeight) {  
        	textSize -= 3;  
        	getPaint().setTextSize(textSize);  
        	getPaint().getTextBounds(getText().toString(), 0, getText().length(), boundsRect); 
        	textHeight = boundsRect.height(); 
        } 
        needFit = false;
    }
    
    @Override
    protected void onTextChanged(CharSequence text, int start,
    		int lengthBefore, int lengthAfter) {
    	// TODO Auto-generated method stub
    	super.onTextChanged(text, start, lengthBefore, lengthAfter);
    	needFit = true;
    }
}
